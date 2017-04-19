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
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.ChargeQueryStatus;
import com.elend.gate.channel.constant.PartnerResultCode;
import com.elend.gate.channel.constant.PayType;
import com.elend.gate.channel.exception.CheckSignatureException;
import com.elend.gate.channel.exception.PayFailureException;
import com.elend.gate.channel.facade.vo.AuthPartnerChargeData;
import com.elend.gate.channel.facade.vo.ChargeCallbackData;
import com.elend.gate.channel.facade.vo.PartnerChargeData;
import com.elend.gate.channel.facade.vo.PartnerSingleQueryChargeData;
import com.elend.gate.channel.service.PayChannelService;
import com.elend.gate.channel.util.LLPayUtil;
import com.elend.gate.conf.facade.LianlianWapConfig;
import com.elend.gate.util.GateHttpUtil;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.ServiceException;
import com.elend.p2p.gson.JSONUtils;
import com.elend.p2p.util.DateUtil;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;
import com.google.gson.reflect.TypeToken;

/**
 * 连连wap快捷支付
 * 
 * @author mgt
 */
@Service
public class LianLianWapChannel extends PayChannelService {
    private final static Logger logger = LoggerFactory.getLogger(LianLianWapChannel.class);

    private static final String SIGN_TYPE = "MD5";

    @Autowired
    private LianlianWapConfig config;
    
    private DesPropertiesEncoder encoder = new DesPropertiesEncoder();
    
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
    

    @Override
    public RequestFormData charge(String orderId, PartnerChargeData chargeData) {
        
        AuthPartnerChargeData authChargeData=(AuthPartnerChargeData)chargeData;
        
        
        String amount = String.format("%.2f", chargeData.getAmount().setScale(2, BigDecimal.ROUND_DOWN)); // 支付金额
        String now = DateUtil.timeToStr(new Date(), DateUtil.DATE_FORMAT_PATTEN4);
        
        Map<String,String> riskMap=new HashMap<String, String>();
        riskMap.put("frms_ware_category", "2009");      //商品类目 P2P小额贷款
        riskMap.put("user_info_mercht_userno", authChargeData.getUserId() + ""); //用户唯一标识
        riskMap.put("user_info_dt_register",DateUtil.timeToStr(authChargeData.getRegisterTime(), DateUtil.DATE_FORMAT_PATTEN4));
        riskMap.put("user_info_full_name",authChargeData.getRealName());    //注册名称       
        riskMap.put("user_info_id_no",authChargeData.getIdCard());  //身份证
        riskMap.put("user_info_identify_state","1");
        riskMap.put("user_info_identify_type","3");
        riskMap.put("user_info_bind_phone", authChargeData.getMobilePhone());
        
        String risk_item=JSONUtils.toJson(riskMap, false);
        
        JSONObject reqObj = new JSONObject();
        reqObj.put("app_request", 3);  //请求应用标示  1-Android 2-ios 3-WAP
        reqObj.put("busi_partner", "101001"); //商户业务类型  101001 - 虚拟商品
        reqObj.put("dt_order", now); //订单时间
        reqObj.put("money_order", amount);
        reqObj.put("no_order", orderId);
        reqObj.put("notify_url", config.getNotifyUrl());
        reqObj.put("oid_partner", config.getMerId());
        reqObj.put("name_goods", "广州易贷充值");
        reqObj.put("risk_item", risk_item);
        reqObj.put("sign_type", SIGN_TYPE);
        reqObj.put("url_return", config.getReturnUrl());
        reqObj.put("valid_order", 100); //订单有效时间  单位分钟
        reqObj.put("user_id", authChargeData.getUserId());
        reqObj.put("id_type", 0);  //证件类型  0 身份证
        reqObj.put("id_no", authChargeData.getIdCard());  // 身份证
        reqObj.put("acct_name", authChargeData.getRealName());  // 身份证姓名
        reqObj.put("syschnotify_flag", 1);  // 是否主动同步通知
        if(StringUtils.isBlank(authChargeData.getContractNo())){//协议号为空，则需传入用户的银行卡等信息
            reqObj.put("card_no", authChargeData.getBankAccount());
        }else{
            reqObj.put("no_agree", authChargeData.getContractNo());
        }
        
        String sign = LLPayUtil.addSign(reqObj,"", config.getPriKey());
        reqObj.put("sign", sign);
        
        RequestFormData form = new RequestFormData();
        form.setRequestUrl(config.getRequestUrl());
        form.addParam("req_data", JSONUtils.toJson(reqObj));
        
        logger.info("连连WAP认证支付充值请求表单生成,form:{}", encoder.encode(JSONUtils.toJson(form, false)));
        return form;
    }

    @Override
    public PartnerResult<ChargeCallbackData> chargeCallback(
            ChannelIdConstant channelId, Map<String, String> params)
            throws CheckSignatureException, ServiceException {
        
        logger.info("lianlian wap charge callback, channelId:{}, params:{}", channelId, encoder.encode(JSONUtils.toJson(params, false)));

        //获取返回的参数
        String resData = params.get("res_data");
        if(StringUtils.isBlank(resData)) { //失败和异常不返回参数
            logger.error("连连wap认证支付返回，返回的参数为空，暂时判断为失败");
            throw new PayFailureException("订单支付失败");
        }
        
        //校验签名
        boolean check = LLPayUtil.checkSign(resData, "", config.getPriKey());
        if (!check) {
            logger.error("交易签名被篡改.channelId:{}", channelId);
            throw new CheckSignatureException("交易签名被篡改");
        }
        
        ChargeCallbackData callbackData = new ChargeCallbackData();
        
        JSONObject jsonMap = JSON.parseObject(resData);
        
        callbackData.setChannelOrderId(StringUtils.trimToEmpty(jsonMap.getString("oid_paybill"))); //渠道订单号
        
        callbackData.setAmount(new BigDecimal(StringUtils.trimToEmpty(jsonMap.getString("money_order")))); //金额
        callbackData.setOrderId(StringUtils.trimToEmpty(jsonMap.getString("no_order"))); //商户订单号
        Date date = null;
        try {
            date = DateUtil.strToTime(jsonMap.getString("settle_date"), DateUtil.DATE_FORMAT_PATTEN3);
        } catch (Exception e) {
        }
        callbackData.setPayTime(date); //支付时间
        callbackData.setNoticeTime(new Date());
        callbackData.setChannelBankId(StringUtils.trimToEmpty(jsonMap.getString("bank_code")));
        callbackData.setContractNo(StringUtils.trimToEmpty(jsonMap.getString("no_agree")));
        //转换成平台的bankId
        String bankId=getBankId(callbackData.getChannelBankId());
        callbackData.setBankId(bankId);
        
        //判断是成功还是失败
        if(PayCode.SUCCESS.equals(jsonMap.getString("result_pay"))) {  //成功
            logger.info("连连WAP支付成功, callbackData:{}", callbackData);
            return new PartnerResult<ChargeCallbackData>(PartnerResultCode.SUCCESS, callbackData, "支付成功");
            //return new PartnerResult<ChargeCallbackData>(PartnerResultCode.FAILURE, callbackData, "支付成功");
        } else if(PayCode.FAILURE.equals(jsonMap.getString("result_pay"))) { //失败
            logger.error("连连WAP支付失败, callbackData:{}", callbackData);
            return new PartnerResult<ChargeCallbackData>(PartnerResultCode.FAILURE, callbackData, "支付失败");
        } else if(PayCode.PROCESSING.equals(jsonMap.getString("result_pay"))) { //处理中
            logger.error("连连WAP支付处理中, callbackData:{}", callbackData);
            return new PartnerResult<ChargeCallbackData>(PartnerResultCode.ORDER_HANDLING, callbackData, "订单已接收");
        } else { //未知状态
            logger.info("连连WAP支付回调, 未知状态, result:{}, callbackData:{}", jsonMap.getString("result_pay"), callbackData);
            throw new ServiceException("未知状态：" + jsonMap.getString("result_pay"));
        }
    }

    @Override
    public ChannelIdConstant getChannelId() {
        return ChannelIdConstant.LIANLIAN_WAP;
    }

    @Override
    public PartnerResult<PartnerSingleQueryChargeData> singleQueryCharge(
            ChannelIdConstant channelId, String orderId)
            throws CheckSignatureException, ServiceException {
        logger.info("lianlian wap singleQueryCharge, channelId:{}, orderId:{}", channelId, orderId);
        if (StringUtils.isBlank(orderId)
                || StringUtils.trimToEmpty(orderId).length() > 32) {
            logger.error("无效的订单号:" + orderId);
            throw new ServiceException("无效的订单号:" + orderId);
        }
        
        JSONObject reqObj = new JSONObject();
        reqObj.put("oid_partner", config.getMerId());
        reqObj.put("sign_type", SIGN_TYPE);
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
            rspStr = GateHttpUtil.doPost(config.getQueryUrl(), reqStr, "utf-8", null, 0, 0);
            logger.info("返回数据:{}", rspStr);
        } catch (Exception e) {
            throw new ServiceException("发送查询请求异常", e);
        }
        
        if (StringUtils.isBlank(rspStr)) {
            throw new ServiceException("lianlian wap sigleQuery No response.");
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
        //卡号不参与签名
        rspMap.remove("card_no");
        boolean check = LLPayUtil.checkSign(JSONUtils.toJson(rspMap), "", config.getPriKey());
        if (!check) {
            logger.error("lianlianwap query 交易签名被篡改.channelId:{}, params:{}", channelId, rspStr);
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
    public BigDecimal feeCalculate(BigDecimal amount, PayType payType) {
        BigDecimal fee = config.getFeePercentage().multiply(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
        return fee.compareTo(BigDecimal.ONE) < 0 ? BigDecimal.ONE : fee;
    }
}
