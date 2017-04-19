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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.elend.gate.channel.util.LLPayUtil;
import com.elend.gate.conf.facade.LianlianGateConfig;
import com.elend.gate.util.GateHttpUtil;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.ServiceException;
import com.elend.p2p.gson.JSONUtils;
import com.elend.p2p.util.DateUtil;
import com.google.gson.reflect.TypeToken;

/**
 * 连连网关支付
 * @author mgt
 * @date 2016年8月17日
 */
@Service
public class LianLianGateChannel extends PayChannelService {
    private final static Logger logger = LoggerFactory.getLogger(LianLianGateChannel.class);

    @Autowired
    private LianlianGateConfig config;
    
    /**
     * 支付状态
     * @author mgt
     *
     */
    public final static class PayCode {
        /**
         * 成功
         */
        public static final String SUCCESS = "SUCCESS";
        /**
         * 处理中
         */
        public static final String PROCESSING = "PROCESSING";
        /**
         * 失败
         */
        public static final String FAILURE = "FAILURE";
        /**
         * 等待支付
         */
        public static final String WAITING = "WAITING";
        /**
         * 退款
         */
        public static final String REFUND = "REFUND";
        
        
    }
    
    /**
     * 返回码
     * @author mgt
     */
    public final static class RetCode {
        /**
         * 连连支付查询交易结果 ：订单不存在
         */
        public final static String RET_CODE_NOT_EXIST = "5002";
        /**
         * 连连支付查询交易结果 ：订单不存在
         */
        public final static String RET_CODE_NOT_EXIST2 = "8901";
        /**
         * 连连支付查询交易结果 ：查询成功
         */
        public final static String RET_CODE_SUCCESS = "0000";
    }
    
    /**
     * 支付方式
     * 1：网银支付（借记卡）
        8：网银支付（信用卡）
        9：B2B 企业网银支付
     */
    public static enum PayType {
        
        /**
         * 个人网银
         */
        PERSON(1, "网银支付（借记卡）", com.elend.gate.channel.constant.PayType.PERSON),
        /**
         * 企业网银
         */
        ENTERPRISE(9, "B2B 企业网银支付", com.elend.gate.channel.constant.PayType.ENTERPRISE),
        
        ;
        
        /**
         * 类型
         * @return 
         */
        private int type;
        /**
         * 描述
         */
        private String desc;
        /**
         * 公共的支付类型
         */
        private com.elend.gate.channel.constant.PayType payType;
        
        private PayType(int type, String desc, com.elend.gate.channel.constant.PayType payType) {
            this.type = type;
            this.desc = desc;
            this.payType = payType;
            
        }
        
        public int getType() {
            return type;
        }
        public void setType(int type) {
            this.type = type;
        }
        public String getDesc() {
            return desc;
        }
        public void setDesc(String desc) {
            this.desc = desc;
        }

        public com.elend.gate.channel.constant.PayType getPayType() {
            return payType;
        }

        public void setPayType(com.elend.gate.channel.constant.PayType payType) {
            this.payType = payType;
        }
        
        public static PayType from(int type) {
            for (PayType one : values()) {
                if (one.getType() == type) {
                    return one;
                }
                
            }
            throw new IllegalArgumentException("illegal status:"+ type);
        }
        
        public static PayType from(com.elend.gate.channel.constant.PayType payType) {
            for (PayType one : values()) {
                if (one.getPayType() == payType) {
                    return one;
                }
                
            }
            throw new IllegalArgumentException("illegal status:"+ payType);
        }
        
    }
    

    @Override
    public RequestFormData charge(String orderId, PartnerChargeData chargeData) {
        
        logger.info("连连网银充值orderId:{}, chargeData:{}", orderId, chargeData);

        String amount = String.format("%.2f", chargeData.getAmount().setScale(2, BigDecimal.ROUND_DOWN)); // 支付金额
        String now = DateUtil.timeToStr(new Date(), DateUtil.DATE_FORMAT_PATTEN4);
        
        String bankCode = "";
        //是否前置选择银行
        if(chargeData.getBankId() != null && chargeData.getBankId() != BankIdConstant.NO_DESIGNATED) {
            bankCode = this.getChannelBankId(chargeData.getBankId());
        }
        
        Map<String, String> map = new HashMap<String, String>();
        
        map.put("version", LianlianGateConfig.VERSION);
        //map.put("charset_name", ""); //默认utf-8
        map.put("oid_partner", config.getMerId());
        map.put("user_id", chargeData.getUserId() + "");
        map.put("timestamp", now);
        map.put("sign_type", LianlianGateConfig.SIGN_TYPE);
        map.put("busi_partner", LianlianGateConfig.BUSI_PARTNER);
        map.put("no_order", orderId);
        map.put("dt_order", now);
        map.put("name_goods", LianlianGateConfig.NAME_GOODS);
        //map.put("info_order", "订单描述");//订单描述,非必填
        map.put("money_order", amount);
        map.put("notify_url", config.getNotifyUrl());
        map.put("url_return", config.getReturnUrl());
        map.put("userreq_ip", chargeData.getIp().replaceAll("\\.", "_")); //请转换为 122_11_37_211
        //map.put("url_order", ""); //订单详情链接地址,非必填
        map.put("valid_order", LianlianGateConfig.VALID_TIME + ""); //订单有效时间
        map.put("bank_code", bankCode);
        
        map.put("pay_type", PayType.from(chargeData.getPayType()).getType() + "");
        //map.put("risk_item", ""); //风控参数非必填
        
        String sign = LLPayUtil.addSign(JSON.parseObject(JSON.toJSONString(map)), "", config.getPriKey());
        map.put("sign", sign);
        
        RequestFormData form = new RequestFormData();
        
        form.setRequestUrl(config.getRequestUrl());
        form.setParams(map);
        
        logger.info("连连网银支付充值请求表单生成,form:{}", JSONUtils.toJson(form, false));
        
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
        boolean check = LLPayUtil.checkSign(resJson, "", config.getPriKey());
        if (!check) {
            logger.error("交易签名被篡改.channelId:{}", channelId);
            throw new CheckSignatureException("交易签名被篡改");
        }
        
        ChargeCallbackData callbackData = new ChargeCallbackData();
        
        callbackData.setChannelOrderId(StringUtils.trimToEmpty(params.get("oid_paybill"))); //渠道订单号
        callbackData.setAmount(new BigDecimal(StringUtils.trimToEmpty(params.get("money_order"))));

        callbackData.setOrderId(StringUtils.trimToEmpty(params.get("no_order"))); //商户订单号
        Date date = null;
        try {
            date = DateUtil.strToTime(params.get("settle_date"), DateUtil.DATE_FORMAT_PATTEN3);
        } catch (Exception e) {
        }
        callbackData.setPayTime(date); //支付时间
        callbackData.setNoticeTime(new Date());
        //转换成平台的bankId
        String bankId=getBankId(callbackData.getChannelBankId());
        callbackData.setBankId(bankId);
        com.elend.gate.channel.constant.PayType payType = null;
        try {
            payType = PayType.from(Integer.parseInt(params.get("pay_type"))).getPayType();
        } catch (Exception e) {
        }
        callbackData.setPayType(payType);
        
        //判断是成功还是失败
        if(PayCode.SUCCESS.equals(params.get("result_pay"))) {  //成功
            logger.info("连连网银支付支付成功, callbackData:{}", callbackData);
            return new PartnerResult<ChargeCallbackData>(PartnerResultCode.SUCCESS, callbackData, "支付成功");
            //return new PartnerResult<ChargeCallbackData>(PartnerResultCode.FAILURE, callbackData, "支付成功");
        } else if(PayCode.FAILURE.equals(params.get("result_pay"))) { //失败
            logger.error("连连网银支付支付失败, callbackData:{}", callbackData);
            return new PartnerResult<ChargeCallbackData>(PartnerResultCode.FAILURE, callbackData, "支付失败");
        } else if(PayCode.PROCESSING.equals(params.get("result_pay"))) { //处理中
            logger.error("连连网银支付支付处理中, callbackData:{}", callbackData);
            return new PartnerResult<ChargeCallbackData>(PartnerResultCode.ORDER_HANDLING, callbackData, "订单已接收");
        } else { //未知状态
            logger.info("连连网银支付支付回调, 未知状态, result:{}, callbackData:{}", params.get("result_pay"), callbackData);
            throw new ServiceException("未知状态：" + params.get("result_pay"));
        }
    }

    @Override
    public ChannelIdConstant getChannelId() {
        return ChannelIdConstant.LIANLIAN_GATE;
    }

    @Override
    public PartnerResult<PartnerSingleQueryChargeData> singleQueryCharge(
            ChannelIdConstant channelId, String orderId)
            throws CheckSignatureException, ServiceException {
        logger.info("lianlian gate singleQueryCharge, channelId:{}, orderId:{}", channelId, orderId);
        if (StringUtils.isBlank(orderId)
                || StringUtils.trimToEmpty(orderId).length() > 32) {
            logger.error("无效的订单号:" + orderId);
            throw new ServiceException("无效的订单号:" + orderId);
        }
        
        JSONObject reqObj = new JSONObject();
        reqObj.put("oid_partner", config.getMerId());
        reqObj.put("sign_type", LianlianGateConfig.SIGN_TYPE);
        reqObj.put("no_order", orderId);
        reqObj.put("query_version ", "1.1");
        //reqObj.put("dt_order", ""); //订单时间
        String sign = LLPayUtil.addSign(reqObj, null, config.getPriKey());
        reqObj.put("sign", sign);
        String reqStr = JSONUtils.toJson(reqObj, false);

        String rspStr = "";
        try {
            logger.info("发送请求...");
            logger.info("请求数据:{}", reqStr.toString());
            rspStr = GateHttpUtil.doPost(config.getQueryUrl(), reqStr, "utf-8", null, 3000, 3000);
            logger.info("返回数据:{}", rspStr);
        } catch (Exception e) {
            throw new ServiceException("发送查询请求异常", e);
        }
        
        if (StringUtils.isBlank(rspStr)) {
            throw new ServiceException("lianlian gate sigleQuery No response.");
        }

        HashMap<String, String> rspMap = JSONUtils.fromJson(rspStr, new TypeToken<HashMap<String, String>>() {
        });

        if(rspMap == null) {
            throw new ServiceException("解析返回报文失败:" + rspStr);
        }
        
        PartnerSingleQueryChargeData backDate = new PartnerSingleQueryChargeData();
        backDate.setOrderId(orderId);
        
        if (RetCode.RET_CODE_NOT_EXIST.equals(rspMap.get("ret_code"))
                || RetCode.RET_CODE_NOT_EXIST2.equals(rspMap.get("ret_code"))) {
            backDate.setStatus(ChargeQueryStatus.NOT_EXIST);
            logger.error("订单不存在:{}", orderId);
            return new PartnerResult<PartnerSingleQueryChargeData>( 
                    PartnerResultCode.SUCCESS,
                    backDate,
                    "错误码：" + rspMap.get("ret_code"));
        }

        if (!RetCode.RET_CODE_SUCCESS.equals(rspMap.get("ret_code"))) {
            logger.error("查询失败:{}", rspMap.get("ret_code"));
            return new PartnerResult<PartnerSingleQueryChargeData>(
                    PartnerResultCode.FAILURE,
                    backDate,
                    "错误码：" + rspMap.get("ret_code"));
        }

        //校验签名,  ret_code 不是0000  时，不返回签名
        boolean check = LLPayUtil.checkSign(JSONUtils.toJson(rspMap), "", config.getPriKey());
        if (!check) {
            logger.error("lianliangate query 交易签名被篡改.channelId:{}, params:{}", channelId, rspStr);
            throw new CheckSignatureException("交易签名被篡改");
        }

        backDate.setAmount(new BigDecimal(rspMap.get("money_order")));
        backDate.setOrderId(rspMap.get("no_order"));
        backDate.setChannelOrderId(rspMap.get("oid_paybill"));

        logger.info("result_pay:{}", rspMap.get("result_pay"));

        if (PayCode.SUCCESS.equals(rspMap.get("result_pay"))) { // 成功
            backDate.setStatus(ChargeQueryStatus.SUCCESS);
        } else if (PayCode.WAITING.equals(rspMap.get("result_pay"))
                || PayCode.PROCESSING.equals(rspMap.get("result_pay"))) { // 等待支付或银行处理中
            backDate.setStatus(ChargeQueryStatus.HANDLING);
        } else if (PayCode.REFUND.equals(rspMap.get("result_pay"))
                || PayCode.FAILURE.equals(rspMap.get("result_pay"))) { // 退款或失败
            backDate.setStatus(ChargeQueryStatus.FAILURE);
        } else { // 未知
            logger.error("未知的支付状态:{}", rspMap.get("result_pay"));
            throw new ServiceException("未知的支付状态, result_pay:" + rspMap.get("result_pay"));
        }
        logger.info("查询结束");
        return new PartnerResult<PartnerSingleQueryChargeData>(
                                                               PartnerResultCode.SUCCESS,
                                                               backDate);
    }

    @Override
    public BigDecimal feeCalculate(BigDecimal amount, com.elend.gate.channel.constant.PayType payType) {
        BigDecimal fee = config.getFeePercentage().multiply(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
        
        fee = fee.compareTo(config.getFeeBottom()) < 0 ? config.getFeeBottom() : fee;
        if(payType == com.elend.gate.channel.constant.PayType.ENTERPRISE) {
            fee = fee.compareTo(config.getFeeTop()) > 0 ? config.getFeeTop() : fee;
        }
        
        return fee;
    }
}
