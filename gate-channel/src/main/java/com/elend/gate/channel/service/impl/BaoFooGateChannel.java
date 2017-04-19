package com.elend.gate.channel.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.BankIdConstant;
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.ChargeQueryStatus;
import com.elend.gate.channel.constant.PartnerResultCode;
import com.elend.gate.channel.exception.CheckSignatureException;
import com.elend.gate.channel.facade.vo.ChargeCallbackData;
import com.elend.gate.channel.facade.vo.PartnerChargeData;
import com.elend.gate.channel.facade.vo.PartnerSingleQueryChargeData;
import com.elend.gate.channel.service.PayChannelService;
import com.elend.gate.channel.util.baofoo.SecurityUtil;
import com.elend.gate.conf.facade.BaofooGateConfig;
import com.elend.gate.util.GateHttpUtil;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.ServiceException;
import com.elend.p2p.gson.JSONUtils;
import com.elend.p2p.util.DateUtil;

/**
 * 连连网关支付
 * @author mgt
 * @date 2016年8月17日
 */
@Service
public class BaoFooGateChannel extends PayChannelService {
    private final static Logger logger = LoggerFactory.getLogger(BaoFooGateChannel.class);

    @Autowired
    private BaofooGateConfig config;
    
    /**
     * 支付状态
     * @author mgt
     *
     */
    public final static class Result {
        /**
         * 成功
         */
        public static final String SUCCESS = "1";
        /**
         * 失败
         */
        public static final String FAILURE = "0";
        
    }
    
    
    /**
     * 支付状态描述
     * @author mgt
     *
     */
    public final static class ResultDesc {
        /**
         * 成功
         */
        public static final String SUCCESS = "01";
        
    }
    
    /**
     * 订单查询返回码
     * @author mgt
     */
    public final static class QueryResult {
        /**
         * 订单不存在
         */
        public final static String NOT_EXIST = "N";
        /**
         * 订单成功
         */
        public final static String SUCCESS = "Y";
        /**
         * 订单失败
         */
        public final static String FAILURE = "F";
        /**
         * 订单处理中
         */
        public final static String HANDLING = "P";
    }
    
    @Override
    public RequestFormData charge(String orderId, PartnerChargeData chargeData) {
        
        logger.info("宝付网银充值orderId:{}, chargeData:{}", orderId, chargeData);

        //订单金额单位分
        String amount = chargeData.getAmount().multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN).toString(); // 支付金额
        String now = DateUtil.timeToStr(new Date(), DateUtil.DATE_FORMAT_PATTEN4);
        
        String bankCode = "";
        //是否前置选择银行
        if(chargeData.getBankId() != null && chargeData.getBankId() != BankIdConstant.NO_DESIGNATED) {
            bankCode = this.getChannelBankId(chargeData.getBankId());
        }
        
        //宝付卡类支付的ID都是3位， 有可能订单金额不等于实际的支付金额
        if(!"".equals(bankCode) && StringUtils.trimToEmpty(bankCode).length() < 4) {
            logger.error("Exception:宝付非法的渠道银行代码,尝试使用卡类充值,orderId:{}, bankCode:{}", orderId, bankCode);
            throw new ServiceException("非法的银行代码");
        }
        
        Map<String, String> map = new HashMap<String, String>();
        
        map.put("MemberID", config.getMerId());
        map.put("TerminalID", config.getTerminalID());
        map.put("InterfaceVersion", "4.0");
        map.put("KeyType", "1");
        map.put("PayID", bankCode);
        map.put("TradeDate", now);
        map.put("TransID", orderId);
        map.put("dt_order", now);
        map.put("OrderMoney", amount); 
        map.put("ProductName", BaofooGateConfig.PRODUCT_NAME);
        map.put("Amount", "1"); //商品数量
        //map.put("Username", ""); // 用户名
        //map.put("AdditionalInfo", ""); //附加字段
        map.put("NoticeType", "1"); //通知类型
        map.put("PageUrl", config.getReturnUrl());
        map.put("ReturnUrl", config.getNotifyUrl());
        
        String mark = "|";
        
        String signStr =new String(map.get("MemberID")
                               + mark 
                               + map.get("PayID")
                               + mark
                               + map.get("TradeDate")
                               + mark
                               + map.get("TransID")
                               + mark
                               + map.get("OrderMoney")
                               + mark
                               + map.get("PageUrl")
                               + mark
                               + map.get("ReturnUrl")
                               + mark
                               + map.get("NoticeType")
                               + mark);//MD5签名格式
        
        String sign = SecurityUtil.MD5(signStr + config.getPriKey());//计算MD5值
        logger.info("签名原串:{}, 签名:{}", signStr, sign);
        
        map.put("Signature", sign);
        
        RequestFormData form = new RequestFormData();
        
        form.setRequestUrl(config.getRequestUrl());
        form.setParams(map);
        
        logger.info("宝付网银支付充值请求表单生成,form:{}", JSONUtils.toJson(form, false));
        
        return form;
    }

    @Override
    public PartnerResult<ChargeCallbackData> chargeCallback(
            ChannelIdConstant channelId, Map<String, String> params)
            throws CheckSignatureException, ServiceException {
        
        logger.info("lianlian gate charge callback, channelId:{}, params:{}", channelId, JSONUtils.toJson(params, false));
        
        //判断是否有返回的参数
        if(params == null || params.keySet().size() < 1) {
            logger.info("回调的参数为空，不处理，直接返回, params:{}", params);
            throw new ServiceException("未知的支付结果");
        }
        
        String resJson = JSONUtils.toJson(params, false);
        logger.info("返回回调参数json：{}", resJson);
        
        //校验签名
        String mark = "~|~";
        
        String signStr =new String(
                                 "MemberID=" + config.getMerId()
                               + mark 
                               + "TerminalID=" + config.getTerminalID()
                               + mark
                               + "TransID=" + StringUtils.trimToEmpty(params.get("TransID"))
                               + mark
                               + "Result=" + StringUtils.trimToEmpty(params.get("Result"))
                               + mark
                               + "ResultDesc=" + StringUtils.trimToEmpty(params.get("ResultDesc"))
                               + mark
                               + "FactMoney=" + StringUtils.trimToEmpty(params.get("FactMoney"))
                               + mark
                               + "AdditionalInfo=" + StringUtils.trimToEmpty(params.get("AdditionalInfo"))
                               + mark
                               + "SuccTime=" + StringUtils.trimToEmpty(params.get("SuccTime"))
                               + mark);//MD5签名格式
        
        String sign = SecurityUtil.MD5(signStr + "Md5Sign=" + config.getPriKey());//计算MD5值
        
        String signBack = StringUtils.trimToEmpty(params.get("Md5Sign"));
        
        logger.info("签名原串:{}, 本地签名:{}, 回传签名:{}", signStr, sign, signBack);
        
        if(!sign.equals(signBack)) {
            logger.error("交易签名被篡改.channelId:{}", channelId);
            throw new CheckSignatureException("交易签名被篡改");
        }
        
        ChargeCallbackData callbackData = new ChargeCallbackData();
        
        callbackData.setChannelOrderId(StringUtils.trimToEmpty(params.get("TransID"))); //渠道订单号
        callbackData.setAmount(new BigDecimal(StringUtils.trimToEmpty(params.get("FactMoney"))).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_DOWN));

        callbackData.setOrderId(StringUtils.trimToEmpty(params.get("TransID"))); //商户订单号
        Date date = null;
        try {
            date = DateUtil.strToTime(params.get("SuccTime"), "yyyyMMddHHmm");
        } catch (Exception e) {
        }
        callbackData.setPayTime(date); //支付时间
        callbackData.setNoticeTime(new Date());
        //转换成平台的bankId
        String bankId=getBankId(callbackData.getChannelBankId());
        callbackData.setBankId(bankId);
        com.elend.gate.channel.constant.PayType payType = null;
        callbackData.setPayType(payType);
        
        //判断是同步还是异步
        boolean sync = Boolean.parseBoolean(params.get("sync"));
        
        //同步返回的成功，不代表银行已经扣款，只是表示受理成功
        if(sync) {
            if(Result.SUCCESS.equals(params.get("Result")) && ResultDesc.SUCCESS.equals(params.get("ResultDesc"))) {  //成功
                logger.info("宝付网银支付受理成功, callbackData:{}", callbackData);
                return new PartnerResult<ChargeCallbackData>(PartnerResultCode.ORDER_HANDLING, callbackData, "订单正在处理，如扣款成功，扣款金额将会自动到账！");
            } else {
                logger.error("宝付网银支付支付失败, callbackData:{}", callbackData);
                return new PartnerResult<ChargeCallbackData>(PartnerResultCode.FAILURE, callbackData, "支付失败:" + params.get("ResultDesc"));
            } 
        }
        
        //判断是成功还是失败
        if(Result.SUCCESS.equals(params.get("Result")) && ResultDesc.SUCCESS.equals(params.get("ResultDesc"))) {  //成功
            logger.info("宝付网银支付支付成功, callbackData:{}", callbackData);
            return new PartnerResult<ChargeCallbackData>(PartnerResultCode.SUCCESS, callbackData, "支付成功");
        } else {
            logger.error("宝付网银支付支付失败, callbackData:{}", callbackData);
            return new PartnerResult<ChargeCallbackData>(PartnerResultCode.FAILURE, callbackData, "支付失败:" + params.get("ResultDesc"));
        } 
    }

    @Override
    public ChannelIdConstant getChannelId() {
        return ChannelIdConstant.BAOFOO_GATE;
    }

    @Override
    public PartnerResult<PartnerSingleQueryChargeData> singleQueryCharge(
            ChannelIdConstant channelId, String orderId)
            throws CheckSignatureException, ServiceException {
        
        logger.info("baofoo gate singleQueryCharge, channelId:{}, orderId:{}", channelId, orderId);
        if (StringUtils.isBlank(orderId)
                || StringUtils.trimToEmpty(orderId).length() > 32) {
            logger.error("无效的订单号:" + orderId);
            throw new ServiceException("无效的订单号:" + orderId);
        }
        
        Map<String, String> reParams = new HashMap<>();
        reParams.put("MemberID", config.getMerId());
        reParams.put("TerminalID", config.getTerminalID());
        reParams.put("TransID", orderId);
        
        //签名
        String mark = "|";
        
        String signStr =new String(
                                 reParams.get("MemberID")
                               + mark 
                               + reParams.get("TerminalID")
                               + mark
                               + reParams.get("TransID")
                               + mark
                               );//MD5签名格式
        
        String sign = SecurityUtil.MD5(signStr + config.getPriKey());//计算MD5值
        
        logger.info("签名原串:{}, 签名:{}", signStr, sign);
        
        reParams.put("MD5Sign", sign);
        
        String responseStr = null; 
        
        try {
            logger.info("请求数据:{}", reParams.toString());
            responseStr = GateHttpUtil.doPost(config.getQueryUrl(), reParams);
            logger.info("返回数据:{}", responseStr);
        } catch (Exception e) {
            throw new ServiceException("宝付订单查询异常通讯异常", e);
        }
        
        if (StringUtils.isBlank(responseStr)) {
            throw new ServiceException("宝付订单查询异常通讯异常, 返回字符串为空");
        }
        
        //去掉换行
        responseStr = responseStr.replaceAll("\n", "");

        String[] params = responseStr.split("\\|");
        
        String MemberID = null;
        String TerminalID = null;
        String TransID = null;
        String CheckResult = null;
        String succMoney = null;
        String SuccTime = null;
        String Md5Sign = null;
        try {
            MemberID = params[0];
            TerminalID = params[1];
            TransID = params[2];
            CheckResult = params[3];
            succMoney = params[4];
            SuccTime = params[5];
            Md5Sign = params[6];
        } catch (Exception e) {
            logger.error("返回参数格式错误， 解析失败, orderId:{}", orderId);
            throw new ServiceException("宝付订单查询返回参数格式错误， 解析失败", e);
        }
        
        //校验签名
        mark = "|";
        
        signStr =new String(MemberID
                               + mark 
                               + TerminalID
                               + mark
                               + TransID
                               + mark
                               + CheckResult
                               + mark
                               + succMoney
                               + mark
                               + SuccTime
                               + mark
                               );//MD5签名格式
        
        sign = SecurityUtil.MD5(signStr + config.getPriKey());//计算MD5值
        
        logger.info("签名原串:{}, 本地签名：{}, 返回签名:{}", signStr, sign, Md5Sign);
        
        if(!sign.equals(Md5Sign)) {
            logger.info("校验签名失败, 本地签名：{}, 返回签名:{}", sign, Md5Sign);
            throw new ServiceException("宝付订单查询校验签名失败");
        }
        
        PartnerSingleQueryChargeData backDate = new PartnerSingleQueryChargeData();
        backDate.setOrderId(orderId);
        
        backDate.setAmount(new BigDecimal(succMoney).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_DOWN));
        backDate.setOrderId(TransID);
        backDate.setChannelOrderId(TransID);

        if (QueryResult.SUCCESS.equals(CheckResult)) { // 成功
            
            backDate.setStatus(ChargeQueryStatus.SUCCESS);
            
        } else if (QueryResult.HANDLING.equals(CheckResult)) { // 处理中
            
            backDate.setStatus(ChargeQueryStatus.HANDLING);
            
        } else if (QueryResult.FAILURE.equals(CheckResult) || QueryResult.NOT_EXIST.equals(CheckResult)) { //失败
            
            backDate.setStatus(ChargeQueryStatus.FAILURE);
            
        } else { // 未知
            logger.error("orderId:{}, 未知的支付状态:{}", orderId, CheckResult);
            throw new ServiceException("宝付订单查询，未知的支付状态:" + CheckResult);
        }
        
        logger.info("查询结束");
        
        return new PartnerResult<PartnerSingleQueryChargeData>(PartnerResultCode.SUCCESS, backDate);
    }

    @Override
    public BigDecimal feeCalculate(BigDecimal amount, com.elend.gate.channel.constant.PayType payType) {
        BigDecimal fee = config.getFeePercentage().multiply(amount);
        
        if(payType == com.elend.gate.channel.constant.PayType.ENTERPRISE) {
            fee = config.getEnterpriseFee();
        }
        
        fee = fee.setScale(2, BigDecimal.ROUND_HALF_UP);
        
        return fee;
    }
}
