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
import com.elend.gate.channel.facade.vo.AuthPartnerChargeData;
import com.elend.gate.channel.facade.vo.ChargeCallbackData;
import com.elend.gate.channel.facade.vo.PartnerChargeData;
import com.elend.gate.channel.facade.vo.PartnerSingleQueryChargeData;
import com.elend.gate.channel.service.PayChannelService;
import com.elend.gate.channel.util.LLPayUtil;
import com.elend.gate.conf.facade.LianlianMobileConfig;
import com.elend.gate.util.GateHttpUtil;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.ServiceException;
import com.elend.p2p.gson.JSONUtils;
import com.elend.p2p.util.DateUtil;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;
import com.google.gson.reflect.TypeToken;

/**
 * 连连手机端认证支付
 * @author liyongquan 2015年7月10日
 *
 */
@Service
public class LianLianMobileChannel extends PayChannelService{
    private final static Logger logger = LoggerFactory.getLogger(LianLianMobileChannel.class);
    
    private static final String SIGN_TYPE="MD5";
    //连连支付成功返回码
    private final static String SUCCESS_CODE="SUCCESS";
    //连连支付查询交易结果 ：订单不存在
    private final static String RET_CODE_NOT_EXIST = "5002";
    private final static String RET_CODE_NOT_EXIST2 = "8901";
    //连连支付查询交易结果 ：查询成功
    private final static String RET_CODE_SUCCESS = "0000";
    
    private DesPropertiesEncoder encoder = new DesPropertiesEncoder();
    
    @Autowired
    private LianlianMobileConfig config;
    @Override
    public RequestFormData charge(String orderId, PartnerChargeData chargeData){
        AuthPartnerChargeData authChargeData=(AuthPartnerChargeData)chargeData;
        String amount = String.format("%.2f",
                                      chargeData.getAmount().setScale(2,
                                                      BigDecimal.ROUND_DOWN)); // 支付金额
        String now=DateUtil.timeToStr(new Date(), DateUtil.DATE_FORMAT_PATTEN4);
        Map<String,String> riskMap=new HashMap<String, String>();
        riskMap.put("frms_ware_category", "2009");
        riskMap.put("user_info_mercht_userno", authChargeData.getUserId()+"");
        riskMap.put("user_info_dt_register",DateUtil.timeToStr(authChargeData.getRegisterTime(), DateUtil.DATE_FORMAT_PATTEN4));
        riskMap.put("user_info_full_name",authChargeData.getRealName());
        riskMap.put("user_info_id_no",authChargeData.getIdCard());
        riskMap.put("user_info_identify_state","1");
        riskMap.put("user_info_identify_type","3");
        riskMap.put("user_info_bind_phone", authChargeData.getMobilePhone());
        
        /*
        riskMap.put("user_info_bind_phone", "13958069593");
        riskMap.put("user_info_dt_register", "201407251110120");
        riskMap.put("frms_ware_category", "4.0");
        riskMap.put("request_imei", "1122111221");*/
        String risk_item=JSONUtils.toJson(riskMap, false);
        
        JSONObject reqObj = new JSONObject();
        reqObj.put("oid_partner", config.getMerId());
        reqObj.put("sign_type", SIGN_TYPE);
        reqObj.put("busi_partner", "101001");
        reqObj.put("no_order", orderId);
        reqObj.put("dt_order", now);
        reqObj.put("name_goods", "广州易贷充值");
        reqObj.put("money_order", amount);
        reqObj.put("notify_url", config.getNotifyUrl());
        reqObj.put("risk_item", risk_item);
        //TODO 临时写死，要通知客户端去掉
        reqObj.put("valid_order", 100);
        String sign = LLPayUtil.addSign(reqObj,"",
                                        config.getPriKey());
        reqObj.put("sign", sign);
        RequestFormData form = new RequestFormData();
        form.setRequestUrl("");
        for(String key:reqObj.keySet()){
            form.addParam(key, reqObj.getString(key));
        }
        form.addParam("user_id", authChargeData.getUserId()+"");
        form.addParam("force_bank", "0");
        form.addParam("id_type", "0");//证件类型
        form.addParam("id_no", authChargeData.getIdCard());
        form.addParam("acct_name", authChargeData.getRealName());
        form.addParam("flag_modify", "0");
        if(StringUtils.isBlank(authChargeData.getContractNo())){//协议号为空，则需传入用户的银行卡等信息
            form.addParam("card_no", authChargeData.getBankAccount());
            //form.addParam("no_agree", "");
        }else{
            form.addParam("no_agree", authChargeData.getContractNo());
            //form.addParam("card_no", "");
        }
        logger.info("连连支付充值请求表单生成,form:{}", encoder.encode(JSONUtils.toJson(form, false)));
        return form;
    }

    @Override
    public PartnerResult<ChargeCallbackData> chargeCallback(
            ChannelIdConstant channelId, Map<String, String> params)
            throws CheckSignatureException, ServiceException {
        logger.info("lianlian mobile sdk charge callback,channelId:{},params:{}", channelId,
                    encoder.encode(JSONUtils.toJson(params, false)));
        String no_order=formatString(params.get("no_order"));
        String oid_paybill=formatString(params.get("oid_paybill"));
        String money_order=formatString(params.get("money_order"));
        String result_pay=formatString(params.get("result_pay"));
        JSONObject reqObj = new JSONObject();
        for(String paramKey:params.keySet()){
            reqObj.put(paramKey, formatString(params.get(paramKey)));
        }
        // 校验返回数据包
        boolean check= LLPayUtil.checkSign(JSONUtils.toJson(params, false), "", config.getPriKey());
        if (check) {
            ChargeCallbackData callbackData = new ChargeCallbackData();
            callbackData.setAmount(new BigDecimal(money_order));
            callbackData.setCallbackStr("{\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"}");
            callbackData.setChannelOrderId(oid_paybill);
            callbackData.setOrderId(no_order);
            if (SUCCESS_CODE.equals(result_pay)) {
                callbackData.setChannelBankId(params.get("bank_code"));
                String bankId=getBankId(callbackData.getChannelBankId());
                callbackData.setBankId(bankId);
                callbackData.setPayTime(DateUtil.strToTime(params.get("settle_date"),DateUtil.DATE_FORMAT_PATTEN3));
                callbackData.setNoticeTime(new Date());
                callbackData.setNotify(true);
                callbackData.setContractNo(params.get("no_agree"));
                logger.info("连连认证支付交易成功,callbackData:{}", callbackData);
                return new PartnerResult<ChargeCallbackData>(PartnerResultCode.SUCCESS,
                                                      callbackData);
            } else {
                logger.error("连连认证支付失败,失败返回码:{},callbackData:{}", result_pay,
                             callbackData);
                return new PartnerResult<ChargeCallbackData>(PartnerResultCode.FAILURE,
                                                      callbackData, "错误码:"
                                                              + result_pay);
            }
        } else {
            logger.error("交易签名被篡改.channelId:{}", channelId);
            throw new CheckSignatureException("交易签名被篡改");
        }
    }
    
    /**
     * 格式化字符串
     * 
     * @param text
     *            --输入文本
     * @return
     */
    private String formatString(String text) {
        if (text == null) {
            return "";
        }
        return text.trim();
    }

    @Override
    public ChannelIdConstant getChannelId() {
        return ChannelIdConstant.LIANLIAN_MOBILE;
    }

	@Override
	public PartnerResult<PartnerSingleQueryChargeData> singleQueryCharge(
			ChannelIdConstant channelId, String orderId)
			throws CheckSignatureException, ServiceException {
		logger.info("singleQueryCharge, channelId:{}, orderId:{}", channelId, orderId);
		if(StringUtils.isBlank(orderId) || StringUtils.trimToEmpty(orderId).length() > 32) {
			logger.error("无效的订单号:" + orderId);
			throw new ServiceException("无效的订单号:" + orderId);
		}
		
        Map<String,String> reqMap=new HashMap<String, String>();
        reqMap.put("oid_partner", config.getMerId());			//商户编号
        reqMap.put("sign_type", SIGN_TYPE);						//签名类型
        reqMap.put("no_order", orderId);						//商户订单号
        //reqMap.put("dt_order", "20150808124525");				//商户订单时间  YYYYMMDDH24MISS

        JSONObject reqObj = new JSONObject();
        reqObj.put("no_order", reqMap.get("no_order"));
        reqObj.put("oid_partner", reqMap.get("oid_partner"));
        reqObj.put("sign_type", reqMap.get("sign_type"));
        //reqObj.put("dt_order", reqMap.get("dt_order"));
        String sign = LLPayUtil.addSign(reqObj, null, config.getPriKey());
        reqMap.put("sign", sign);
        String reqStr = JSONUtils.toJson(reqMap, false);
        
        String rspStr = "";
        try {
			logger.info("发送请求...");
			logger.info("请求数据:{}", reqStr.toString());
			rspStr = GateHttpUtil.doPost(config.getQueryRefundReqURL(), reqStr, "utf-8", null, 0, 0);
			logger.info("返回数据:{}", rspStr);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		if (StringUtils.isBlank(rspStr)) {
			throw new ServiceException("No response.");
		}
		
		HashMap<String, String> rspMap = JSONUtils.fromJson(rspStr, new TypeToken<HashMap<String, String>>(){});
		
		PartnerSingleQueryChargeData backDate = new PartnerSingleQueryChargeData();
		backDate.setOrderId(orderId);
		
		if (RET_CODE_NOT_EXIST.equals(rspMap.get("ret_code")) || RET_CODE_NOT_EXIST2.equals(rspMap.get("ret_code"))) {
			backDate.setStatus(ChargeQueryStatus.NOT_EXIST);
			logger.error("订单不存在:{}", orderId);
			return new PartnerResult<PartnerSingleQueryChargeData>(PartnerResultCode.SUCCESS, backDate, "错误码：" + rspMap.get("ret_code"));
		} 
		
		if(!RET_CODE_SUCCESS.equals(rspMap.get("ret_code"))) {
			logger.error("查询失败:{}", rspMap.get("ret_code"));
			return new PartnerResult<PartnerSingleQueryChargeData>(PartnerResultCode.FAILURE, backDate, "错误码：" + rspMap.get("ret_code"));
		} 
		
		//订单查询卡号不参与签名
		JSONObject rspObj = JSON.parseObject(rspStr);
		if(rspObj != null) {
			rspObj.remove("card_no");
		}
        
		// 校验返回数据包
        boolean check= LLPayUtil.checkSign(rspObj.toString(), "", config.getPriKey());
        
        if (check) {
        	backDate.setAmount(new BigDecimal(rspMap.get("money_order")));
    		backDate.setOrderId(rspMap.get("no_order"));
    		backDate.setChannelOrderId(rspMap.get("oid_paybill"));
    		
    		logger.info("result_pay:{}", rspMap.get("result_pay"));
    		
    		if("SUCCESS".equals(rspMap.get("result_pay"))) {		//成功
    			backDate.setStatus(ChargeQueryStatus.SUCCESS);
    		} else if("WAITING".equals(rspMap.get("result_pay")) || "PROCESSING".equals(rspMap.get("result_pay"))) { //等待支付或银行处理中
    			backDate.setStatus(ChargeQueryStatus.HANDLING);
    		} else if("REFUND".equals(rspMap.get("result_pay")) || "FAILURE".equals(rspMap.get("result_pay"))) { //退款或失败
    			backDate.setStatus(ChargeQueryStatus.FAILURE);
    		} else { //未知
    			logger.error("未知的支付状态:{}", rspMap.get("result_pay"));
    			throw new ServiceException("未知的支付状态, result_pay:" + rspMap.get("result_pay"));
    		}
    		logger.info("查询结束");
    		return new PartnerResult<PartnerSingleQueryChargeData>(PartnerResultCode.SUCCESS, backDate);
        } else {
            logger.error("充值订单查询签名被篡改.channelId:{},rspStr:{}", channelId, rspStr);
            throw new CheckSignatureException("交易签名被篡改");
        }
	}

    @Override
    public BigDecimal feeCalculate(BigDecimal amount, PayType payType) {
        BigDecimal fee=config.getFeePercentage().multiply(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
        return fee.compareTo(BigDecimal.ONE)<0?BigDecimal.ONE:fee;
    }
}
