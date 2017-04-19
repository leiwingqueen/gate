package com.elend.gate.sdk;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.elend.gate.sdk.exception.CheckSignatureException;
import com.elend.gate.sdk.exception.ParamErrorException;
import com.elend.gate.sdk.vo.AuthParam;
import com.elend.gate.sdk.vo.ChargeResponse;
import com.elend.gate.sdk.vo.PartnerChargeResponse;
import com.elend.gate.sdk.vo.PartnerPayAgentChargeResponse;
import com.elend.gate.sdk.vo.PartnerPayAgentCheckCodeResponse;
import com.elend.gate.sdk.vo.PartnerPayAgentGetCodeResponse;
import com.elend.gate.sdk.vo.PartnerWithdrawResponse;
import com.elend.gate.sdk.vo.PayAgentChargeResponse;
import com.elend.gate.sdk.vo.PayAgentCheckCodeResponse;
import com.elend.gate.sdk.vo.PayAgentGetCodeResponse;
import com.elend.gate.sdk.vo.SBankPayLimitVO;
import com.elend.gate.sdk.vo.WithdrawResponse;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.Result;
import com.elend.p2p.gson.JSONUtils;
import com.elend.p2p.util.DateUtil;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;
import com.google.gson.reflect.TypeToken;
import com.yeepay.DigestUtil;
/**
 * 网关接入辅助类
 * @author liyongquan 2015年6月8日
 *
 */
public class GateHelper {
    /**
     * 生成签名
     * @param partnerId--接入方ID
     * @param partnerOrderId--接入方订单号
     * @param amount--金额
     * @param redirectUrl--跳转地址
     * @param notifyUrl--点对点通知地址
     * @param channel--支付渠道
     * @param priKey--接入方秘钥
     * @param timestamp--访问时间戳
     * @param ip--访问IP
     * @return
     */
    private static String genSignature(String partnerId,String partnerOrderId
            ,BigDecimal amount,String redirectUrl,String notifyUrl,
            ChannelIdConstant channel,String priKey,String timestamp,String ip){
        StringBuffer sValue = new StringBuffer();
        sValue.append(partnerId);
        sValue.append(partnerOrderId);
        sValue.append(amount);
        sValue.append(channel.name());
        sValue.append(redirectUrl);
        sValue.append(notifyUrl);
        sValue.append(timestamp);
        sValue.append(ip);
        String hmac= DigestUtil.hmacSign(sValue.toString(),priKey);
        return hmac;
    }
    
    /**
     * 生成签名
     * @param priKey    密钥
     * @param params    签名参数
     * @return
     */
    private static String genSignature(String priKey, String ... params){
        if(params == null) {
            return "";
        }
        StringBuffer sValue = new StringBuffer();
        for(String param : params) {
            if(StringUtils.isBlank(param)) {
                continue;
            }
            sValue.append(param); 
        }
        String hmac= DigestUtil.hmacSign(sValue.toString(), priKey);
        return hmac;
    }
    
    
    /**
     * 生成请求参数
     * @param partnerId--接入方ID
     * @param partnerOrderId--接入方订单号
     * @param amount--金额
     * @param redirectUrl--跳转地址
     * @param notifyUrl--点对点通知地址
     * @param channel--支付渠道
     * @param priKey--接入方秘钥
     * @param requestUrl--支付网关请求地址
     * @param ip--用户请求访问IP
     * @param userId--用户ID
     * @param payType--个人网银或企业网银
     * @return
     */
    public static RequestFormData chargeRequest(String partnerId,
            String partnerOrderId,BigDecimal amount,String redirectUrl,String notifyUrl,
            ChannelIdConstant channel,String priKey,String requestUrl,String ip, long userId, PayType payType){
        return chargeRequest(partnerId, partnerOrderId, amount, "", 
                             redirectUrl, notifyUrl, channel, priKey, requestUrl, ip, userId, payType);
    }
    
    /**
     * 生成请求参数
     * @param partnerId--接入方ID
     * @param partnerOrderId--接入方订单号
     * @param amount--金额
     * @param bankId--银行ID
     * @param redirectUrl--跳转地址
     * @param notifyUrl--点对点通知地址
     * @param channel--支付渠道
     * @param priKey--接入方秘钥
     * @param requestUrl--支付网关请求地址
     * @param ip--用户请求访问IP
     * @return
     */
    public static RequestFormData chargeRequest(String partnerId,
            String partnerOrderId,BigDecimal amount,String bankId,String redirectUrl,String notifyUrl,
            ChannelIdConstant channel,String priKey,String requestUrl,String ip, long userId, PayType payType){
        
        //参数校验
        if(isBlank(partnerId)) {
            throw new ParamErrorException("partnerId不能为空");
        }
        
        if(isBlank(partnerOrderId)) {
            throw new ParamErrorException("partnerOrderId不能为空");
        }
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ParamErrorException("amount必须大于0");
        }
        if(isBlank(priKey)) {
            throw new ParamErrorException("priKey不能为空");
        }
        if(isBlank(requestUrl)) {
            throw new ParamErrorException("requestUrl不能为空");
        }
        
        String timeStamp=new Date().getTime()+"";
        
        String payTypeStr = payType == null ? "" : payType.name();
        
        String hmac = genSignature(priKey, partnerId, partnerOrderId, amount.toString(), 
                                 channel.name(), redirectUrl, notifyUrl, bankId, timeStamp, ip, userId + "", payTypeStr);
        
        RequestFormData formData=new RequestFormData();
        formData.setRequestUrl(requestUrl);
        formData.addParam("partnerId", partnerId);
        formData.addParam("partnerOrderId",partnerOrderId);
        formData.addParam("amount", amount.toString());
        formData.addParam("payChannel", channel.name());
        formData.addParam("redirectUrl", redirectUrl);
        formData.addParam("notifyUrl", notifyUrl);
        formData.addParam("hmac", hmac);
        formData.addParam("timeStamp", timeStamp);
        formData.addParam("ip", ip);
        formData.addParam("bankId", bankId);
        formData.addParam("userId", userId + "");
        formData.addParam("payType", payTypeStr);
        return formData;
    }
    
    /**
     * 认证支付生成请求参数
     * @param partnerId--接入方ID
     * @param partnerOrderId--接入方订单号
     * @param amount--金额
     * @param redirectUrl--跳转地址
     * @param notifyUrl--点对点通知地址
     * @param authParam--认证支付参数
     * @param channel--支付渠道
     * @param priKey--接入方秘钥
     * @param requestUrl--支付网关请求地址
     * @param ip--用户请求访问IP
     * @return
     */
    public static RequestFormData authChargeRequest(String partnerId,
            String partnerOrderId,BigDecimal amount,String redirectUrl,String notifyUrl,
            AuthParam authParam,
            ChannelIdConstant channel,String priKey,String requestUrl,String ip){
        String timeStamp=new Date().getTime()+"";
        
        //敏感信息进行加密
        DesPropertiesEncoder encoder = new DesPropertiesEncoder();
        String idCard = encoder.encode(StringUtils.trimToEmpty(authParam.getIdCard()));
        String cardNo = encoder.encode(StringUtils.trimToEmpty(authParam.getBankAccount()));
        String realName = encoder.encode(StringUtils.trimToEmpty(authParam.getRealName()));
        String mobilePhone = encoder.encode(StringUtils.trimToEmpty(authParam.getMobilePhone()));
        
        String registerTime = DateUtil.timeToStr(authParam.getRegisterTime(),DateUtil.DATE_FORMAT_PATTEN4);

        String hmac = genSignature(priKey, partnerId, partnerOrderId, amount.toString(), 
                                   channel.name(), redirectUrl, notifyUrl, timeStamp, ip, authParam.getUserId() + "", 
                                   cardNo, realName, idCard, registerTime, authParam.getContractNo(), mobilePhone);
        
        
        RequestFormData formData=new RequestFormData();
        formData.setRequestUrl(requestUrl);
        formData.addParam("partnerId", partnerId);
        formData.addParam("partnerOrderId",partnerOrderId);
        formData.addParam("amount", amount.toString());
        formData.addParam("payChannel", channel.name());
        formData.addParam("redirectUrl", redirectUrl);
        formData.addParam("notifyUrl", notifyUrl);
        formData.addParam("hmac", hmac);
        formData.addParam("timeStamp", timeStamp);
        formData.addParam("ip", ip);
        /************认证支付参数*********/
        formData.addParam("userId", authParam.getUserId()+"");
        formData.addParam("bankAccount", cardNo);
        formData.addParam("idCard", idCard);
        formData.addParam("realName", realName);
        formData.addParam("registerTime", registerTime);
        formData.addParam("contractNo",authParam.getContractNo());
        formData.addParam("mobilePhone",mobilePhone);
        
        return formData;
    }
    /**
     * 验签并进行参数解析
     * @param response--网关回调参数
     * @param priKey--接入方秘钥
     * @return
     */
    public static ChargeResponse getResponse(PartnerChargeResponse response,String priKey)throws CheckSignatureException{
        response.setBankId(response.getBankId()==null ? "" : response.getBankId());
        response.setPartnerId(response.getPartnerId() == null ? "" : response.getPartnerId());
        response.setResultCode(response.getResultCode() == null? "" : response.getResultCode());
        response.setMessage(response.getMessage() == null? "" : response.getMessage());
        response.setPartnerOrderId(response.getPartnerOrderId() == null? "" : response.getPartnerOrderId());
        response.setOrderId(response.getOrderId() == null ? "" : response.getOrderId());
        response.setAmount(response.getAmount() == null ? "" : response.getAmount());
        response.setChannelId(response.getChannelId() == null ? "" : response.getChannelId());
        response.setChannelOrderId(response.getChannelOrderId() == null ? "" : response.getChannelOrderId());
        response.setChannelPayTime(response.getChannelPayTime() == null ? "" : response.getChannelPayTime());
        response.setChannelNoticeTime(response.getChannelNoticeTime() == null ? "" : response.getChannelNoticeTime());
        response.setGateNoticeTime(response.getGateNoticeTime() == null ? "" : response.getGateNoticeTime());
        response.setIsNotify(response.getIsNotify() == null ? "" : response.getIsNotify());
        response.setContractNo(response.getContractNo() == null ? "" : response.getContractNo());
        
        StringBuffer sValue = new StringBuffer();
        sValue.append(response.getPartnerId());
        sValue.append(response.getResultCode());
        sValue.append(response.getMessage());
        sValue.append(response.getPartnerOrderId());
        sValue.append(response.getOrderId());
        sValue.append(response.getAmount());
        
        sValue.append(response.getChannelId());
        sValue.append(response.getChannelOrderId());
        sValue.append(response.getBankId());
        sValue.append(response.getChannelPayTime());
        sValue.append(response.getChannelNoticeTime());
        sValue.append(response.getGateNoticeTime());
        String sNewString = null;
        sNewString = DigestUtil.hmacSign(sValue.toString(),priKey);
        if(StringUtils.isBlank(sNewString)||!sNewString.equals(response.getHmac())){
            throw new CheckSignatureException("签名认证失败");
        }
        ChargeResponse chargeResponse=new ChargeResponse();
        chargeResponse.setPartnerId(response.getPartnerId());
        chargeResponse.setResultCode(response.getResultCode());
        chargeResponse.setMessage(response.getMessage());
        
        chargeResponse.setPartnerOrderId(response.getPartnerOrderId());
        chargeResponse.setOrderId(response.getOrderId());
        chargeResponse.setAmount(new BigDecimal(response.getAmount()));
        chargeResponse.setChannelId(ChannelIdConstant.from(response.getChannelId()));
        chargeResponse.setChannelOrderId(response.getChannelOrderId());
        chargeResponse.setBankId(response.getBankId());
        Date payTime=DateUtil.strToTime(response.getChannelPayTime(), DateUtil.DATE_FORMAT_PATTEN4);
        Date channelNoticeTime=DateUtil.strToTime(response.getChannelNoticeTime(), DateUtil.DATE_FORMAT_PATTEN4);
        Date gateNoticeTime=DateUtil.strToTime(response.getGateNoticeTime(), DateUtil.DATE_FORMAT_PATTEN4);
        chargeResponse.setChannelPayTime(payTime);
        chargeResponse.setChannelNoticeTime(channelNoticeTime);
        chargeResponse.setGateNoticeTime(gateNoticeTime);
        chargeResponse.setNotify(Boolean.parseBoolean(response.getIsNotify()));
        chargeResponse.setContractNo(response.getContractNo());
        chargeResponse.setFee(new BigDecimal(response.getFee()));
        return chargeResponse;
    }
    
    /**
     * 提现请求参数
     * @param partnerId         接入方ID（P2P）
     * @param partnerOrderId    借入方订单号
     * @param amount            金额
     * @param notifyUrl         通知地址
     * @param channel           渠道
     * @param priKey            接入方密钥    
     * @param requestUrl        网关的请求地址
     * @param ip                IP
     * @param bankId            银行编号
     * @param bankAccount       银行卡号
     * @param userName          用户名
     * @param bankCityName      银行卡所在城市名称
     * @param bankProvinceName  银行卡所在省份名称
     * @param bankBranchName    银行卡开户行支行名称
     * @param accountType       对公对私标识（1对公， 2对私）
     * @param identityCard
     * 身份证号，企业账号此字段可为空
     * @return
     */
    public static RequestFormData withdrawRequest(
            String partnerId,
            String partnerOrderId,
            BigDecimal amount,
            String notifyUrl,
            WithdrawChannel channel,
            String priKey,
            String requestUrl,
            String ip,
            String bankId,
            String bankAccount,
            String userName,
            String bankCityId,
            String bankProvinceId,
            String bankBranchName,
            int accountType,String identityCard
            ){
        
        //对敏感信息进行加密(使用的时候再进行解密)
        DesPropertiesEncoder encoder = new DesPropertiesEncoder();
        userName = encoder.encode(userName);
        bankAccount = encoder.encode(bankAccount);
        identityCard = StringUtils.isBlank(identityCard)?"":encoder.encode(identityCard);
        
        String timeStamp=new Date().getTime()+"";
        String hmac = genSignature(priKey, partnerId, partnerOrderId, amount.toString(), channel.name(), timeStamp, ip, notifyUrl, bankId, bankAccount, userName, bankCityId, bankProvinceId, bankBranchName, accountType + "");
        RequestFormData formData=new RequestFormData();
        formData.setRequestUrl(requestUrl);
        formData.addParam("partnerId", partnerId);
        formData.addParam("partnerOrderId",partnerOrderId);
        formData.addParam("amount", amount.toString());
        formData.addParam("channel", channel.name());
        formData.addParam("notifyUrl", notifyUrl);
        formData.addParam("timeStamp", timeStamp);
        formData.addParam("ip", ip);
        formData.addParam("bankId", bankId);
        formData.addParam("bankAccount", bankAccount);
        formData.addParam("userName", userName);
        formData.addParam("bankCityId", bankCityId);
        formData.addParam("bankProvinceId", bankProvinceId);
        formData.addParam("bankBranchName", bankBranchName);
        formData.addParam("accountType", accountType + "");
        formData.addParam("hmac", hmac);
        formData.addParam("identityCard", identityCard);
        return formData;
    }
    
    /**
     * 验签并进行参数解析
     * @param response--网关回调参数
     * @param priKey--接入方秘钥
     * @return
     */
    public static WithdrawResponse getwithdrawResponse(PartnerWithdrawResponse response, String priKey)throws CheckSignatureException{
        response.setPartnerId(response.getPartnerId() == null ? "" : response.getPartnerId());
        response.setMessage(response.getMessage() == null? "" : response.getMessage());
        response.setPartnerOrderId(response.getPartnerOrderId() == null? "" : response.getPartnerOrderId());
        response.setOrderId(response.getOrderId() == null ? "" : response.getOrderId());
        response.setAmount(response.getAmount() == null ? "" : response.getAmount());
        response.setChannelId(response.getChannelId() == null ? "" : response.getChannelId());
        response.setChannelOrderId(response.getChannelOrderId() == null ? "" : response.getChannelOrderId());
        response.setChannelNoticeTime(response.getChannelNoticeTime() == null ? "" : response.getChannelNoticeTime());
        response.setGateNoticeTime(response.getGateNoticeTime() == null ? "" : response.getGateNoticeTime());
        response.setFee(response.getFee() == null ? "" : response.getFee());
        response.setWithdrawStatus(response.getWithdrawStatus() == null ? "" : response.getWithdrawStatus());
        
        StringBuffer sValue = new StringBuffer();
        sValue.append(response.getPartnerId());
        sValue.append(response.getMessage());
        sValue.append(response.getPartnerOrderId());
        sValue.append(response.getOrderId());
        sValue.append(response.getAmount());
        
        sValue.append(response.getChannelId());
        sValue.append(response.getChannelOrderId());
        sValue.append(response.getChannelNoticeTime());
        sValue.append(response.getGateNoticeTime());
        sValue.append(response.getFee());
        sValue.append(response.getWithdrawStatus());
        
        String sNewString = null;
        sNewString = DigestUtil.hmacSign(sValue.toString(),priKey);
        if(StringUtils.isBlank(sNewString)||!sNewString.equals(response.getHmac())){
            throw new CheckSignatureException("签名认证失败");
        }
        
        WithdrawResponse withdrawResponse=new WithdrawResponse();
        withdrawResponse.setPartnerId(response.getPartnerId());
        withdrawResponse.setMessage(response.getMessage());
        
        withdrawResponse.setPartnerOrderId(response.getPartnerOrderId());
        withdrawResponse.setMessage(response.getMessage());
        withdrawResponse.setPartnerOrderId(response.getPartnerOrderId());
        withdrawResponse.setOrderId(response.getOrderId());
        withdrawResponse.setAmount(new BigDecimal(StringUtils.isBlank(response.getAmount())?"0":response.getAmount()));
        withdrawResponse.setChannel(WithdrawChannel.from(response.getChannelId()));
        withdrawResponse.setChannelOrderId(response.getChannelOrderId());
        
        Date channelNoticeTime = null;
        if(StringUtils.isNotBlank(response.getChannelNoticeTime())) {
            channelNoticeTime=DateUtil.strToTime(response.getChannelNoticeTime(), DateUtil.DATE_FORMAT_PATTEN4);
        }
        Date gateNoticeTime = null;
        if(StringUtils.isNotBlank(response.getGateNoticeTime())) {
            gateNoticeTime=DateUtil.strToTime(response.getGateNoticeTime(), DateUtil.DATE_FORMAT_PATTEN4);
        }
        withdrawResponse.setChannelNoticeTime(channelNoticeTime);
        withdrawResponse.setGateNoticeTime(gateNoticeTime);
        
        if(StringUtils.isNotBlank(response.getFee())) {
            withdrawResponse.setFee(new BigDecimal(response.getFee()));
        }
        
        withdrawResponse.setOrderId(response.getOrderId());
        withdrawResponse.setWithdrawStatus(WithdrawStatus.from(response.getWithdrawStatus()));
        
        return withdrawResponse;
    }
    
    /**
     * 代收鉴权获取验证码请求参数
     * @param partnerId         合作方ID P2P
     * @param partnerOrderId    合作方订单号
     * @param amount            鉴权收取的金额
     * @param channel           鉴权的渠道
     * @param priKey            key
     * @param requestUrl        请求地址
     * @param ip                ip
     * @param bankId            银行ID
     * @param cardNo            银行卡号
     * @param realName          真实姓名
     * @param idNo              身份证号
     * @param cellphone         手机号码
     * @return
     */
    public static RequestFormData payAgentGetCodeRequest(
            String partnerId,
            String partnerOrderId,
            BigDecimal amount,
            PayAgentChannel channel,
            String priKey,
            String requestUrl,
            String ip,
            String bankId,
            String cardNo,
            String realName,
            String idNo,
            String cellphone
            ) {
        
        //对敏感信息进行加密(使用的时候再进行解密)
        DesPropertiesEncoder encoder = new DesPropertiesEncoder();
        cardNo = encoder.encode(cardNo);
        realName = encoder.encode(realName);
        idNo = encoder.encode(idNo);
        cellphone = encoder.encode(cellphone);
        
        String timeStamp=new Date().getTime()+"";
        String hmac = genSignature(priKey, partnerId, partnerOrderId, amount.toString(), channel.name(), timeStamp, ip, bankId, cardNo, realName, idNo, cellphone);
        RequestFormData formData=new RequestFormData();
        formData.setRequestUrl(requestUrl);
        formData.addParam("partnerId", partnerId);
        formData.addParam("partnerOrderId",partnerOrderId);
        formData.addParam("amount", amount.toString());
        formData.addParam("channel", channel.name());
        formData.addParam("timeStamp", timeStamp);
        formData.addParam("ip", ip);
        formData.addParam("bankId", bankId);
        formData.addParam("cardNo", cardNo);
        formData.addParam("realName", realName);
        formData.addParam("idNo", idNo);
        formData.addParam("cellphone", cellphone);
        formData.addParam("hmac", hmac);
        return formData;
    }
    
    /**
     * 获取代收鉴权的返回参数
     * @param response--网关回调参数
     * @param priKey--接入方秘钥
     * @return
     */
    public static PayAgentGetCodeResponse getPayAgentGetCodeResponse(PartnerPayAgentGetCodeResponse response, String priKey)throws CheckSignatureException{

        // null to ""
        response = fieldNullToEmpty(response);
        
        StringBuffer sValue = new StringBuffer();
        sValue.append(response.getPartnerId());
        sValue.append(response.getMessage());
        sValue.append(response.getPartnerOrderId());
        sValue.append(response.getOrderId());
        sValue.append(response.getAmount());
        
        sValue.append(response.getChannelId());
        sValue.append(response.getChannelOrderId());
        
        String sNewString = null;
        sNewString = DigestUtil.hmacSign(sValue.toString(),priKey);
        if(StringUtils.isBlank(sNewString)||!sNewString.equals(response.getHmac())){
            throw new CheckSignatureException("签名认证失败");
        }
        
        PayAgentGetCodeResponse agentGetCodeResponse=new PayAgentGetCodeResponse();
        agentGetCodeResponse.setPartnerId(response.getPartnerId());
        agentGetCodeResponse.setMessage(response.getMessage());
        
        agentGetCodeResponse.setPartnerOrderId(response.getPartnerOrderId());
        agentGetCodeResponse.setMessage(response.getMessage());
        agentGetCodeResponse.setPartnerOrderId(response.getPartnerOrderId());
        agentGetCodeResponse.setOrderId(response.getOrderId());
        agentGetCodeResponse.setAmount(new BigDecimal(StringUtils.isBlank(response.getAmount())?"0":response.getAmount()));
        agentGetCodeResponse.setChannel(PayAgentChannel.from(response.getChannelId()));
        agentGetCodeResponse.setChannelOrderId(response.getChannelOrderId());
        
        return agentGetCodeResponse;
    }
    
    /**
     * 代收鉴权校验校验码请求参数
     * @param partnerId         合作方ID P2P
     * @param partnerOrderId    合作方订单号
     * @param partnerApplyOrderId    合作方鉴权申请订单号
     * @param priKey            key
     * @param requestUrl        请求地址
     * @param ip                IP
     * @param authCode          手机校验码
     * @param notifyUrl         异步结果通知地址
     * @return
     */
    public static RequestFormData payAgentCheckCodeRequest(
            String partnerId,
            String partnerOrderId,
            String partnerApplyOrderId,
            String priKey,
            String requestUrl,
            String ip,
            String authCode,
            String notifyUrl
            ) {
        
        String timeStamp = new Date().getTime() + "";
        String hmac = genSignature(priKey, partnerId, partnerOrderId, timeStamp, ip, authCode, notifyUrl, partnerApplyOrderId);
        RequestFormData formData = new RequestFormData();
        formData.setRequestUrl(requestUrl);
        formData.addParam("partnerId", partnerId);
        formData.addParam("partnerOrderId",partnerOrderId);
        formData.addParam("timeStamp", timeStamp);
        formData.addParam("ip", ip);
        formData.addParam("authCode", authCode);
        formData.addParam("notifyUrl", notifyUrl);
        formData.addParam("partnerApplyOrderId", partnerApplyOrderId);
        formData.addParam("hmac", hmac);
        return formData;
    }
    
    /**
     * 验签并进行参数解析
     * @param response--网关回调参数
     * @param priKey--接入方秘钥
     * @return
     */
    public static PayAgentCheckCodeResponse getPayAgentCheckCodeResponse(PartnerPayAgentCheckCodeResponse response, String priKey)throws CheckSignatureException{

        // null to ""
        response = fieldNullToEmpty(response);
        
        StringBuffer sValue = new StringBuffer();
        sValue.append(response.getPartnerId());
        sValue.append(response.getMessage());
        sValue.append(response.getPartnerOrderId());
        sValue.append(response.getOrderId());
        sValue.append(response.getAmount());
        
        sValue.append(response.getChannelId());
        sValue.append(response.getChannelNoticeTime());
        sValue.append(response.getFee());
        sValue.append(response.getAgentStatus());
        
        
        String sNewString = null;
        sNewString = DigestUtil.hmacSign(sValue.toString(),priKey);
        if(StringUtils.isBlank(sNewString)||!sNewString.equals(response.getHmac())){
            throw new CheckSignatureException("签名认证失败");
        }
        
        PayAgentCheckCodeResponse dataResponse = new PayAgentCheckCodeResponse();
        
        dataResponse.setPartnerId(response.getPartnerId());
        dataResponse.setMessage(response.getMessage());
        dataResponse.setPartnerOrderId(response.getPartnerOrderId());
        dataResponse.setOrderId(response.getOrderId());
        dataResponse.setAmount(new BigDecimal(StringUtils.isBlank(response.getAmount())?"0":response.getAmount()));
        dataResponse.setChannel(PayAgentChannel.from(response.getChannelId()));
        if(StringUtils.isNotBlank(response.getChannelNoticeTime())) {
            dataResponse.setChannelNoticeTime(DateUtil.strToTime(response.getChannelNoticeTime(), DateUtil.DATE_FORMAT_PATTEN));
        }
        dataResponse.setFee(new BigDecimal(StringUtils.isBlank(response.getFee())? "0" : response.getFee()));
        dataResponse.setAgentStatus(AgentStatus.valueOf(response.getAgentStatus()));
        
        return dataResponse;
    }
    
    /**
     * 代收请求参数
     * @param partnerId         合作平台ID（P2P）
     * @param partnerOrderId    合作平台订单号
     * @param priKey            key
     * @param requestUrl        请求地址
     * @param ip                ip
     * @param bankId            银行ID
     * @param cardNo            银行卡号
     * @param realName          真实姓名
     * @param idNo              身份证号
     * @param cellphone         银行预留手机          
     * @param amount            金额
     * @param notifyUrl         异步结果通知地址
     * @param channel           渠道
     * @param accountType       对公对私标识（1对公， 2对私）
     * @return
     */
    public static RequestFormData payAgentChargeRequest(
            String partnerId,
            String partnerOrderId,
            String priKey,
            String requestUrl,
            String ip,
            String bankId,
            String cardNo,
            String realName,
            String idNo,
            String cellphone,
            String amount,
            String notifyUrl,
            PayAgentChannel channel,
            BankAccountType accountType
            ) {
        
        //敏感信息加密
        
        DesPropertiesEncoder encoder = new DesPropertiesEncoder();
        cardNo = encoder.encode(cardNo);
        realName = encoder.encode(realName);
        idNo = encoder.encode(idNo);
        cellphone = encoder.encode(cellphone);
        
        String timeStamp = new Date().getTime() + "";
        String hmac = genSignature(priKey, partnerId, partnerOrderId, timeStamp, ip, bankId, cardNo, realName, amount, notifyUrl, channel.name(), accountType.name(), idNo, cellphone);
        RequestFormData formData = new RequestFormData();
        formData.setRequestUrl(requestUrl);
        formData.addParam("partnerId", partnerId);
        formData.addParam("partnerOrderId",partnerOrderId);
        formData.addParam("timeStamp", timeStamp);
        formData.addParam("ip", ip);
        formData.addParam("bankId", bankId);
        formData.addParam("cardNo", cardNo);
        formData.addParam("realName", realName);
        formData.addParam("amount", amount);
        formData.addParam("notifyUrl", notifyUrl);
        formData.addParam("channel", channel.name());
        formData.addParam("accountType", accountType.name());
        formData.addParam("idNo", idNo);
        formData.addParam("cellphone", cellphone);
        
        formData.addParam("hmac", hmac);
        return formData;
    }
    
    /**
     * 验签并进行参数解析
     * @param response--网关回调参数
     * @param priKey--接入方秘钥
     * @return
     */
    public static PayAgentChargeResponse getPayAgentChargeResponse(PartnerPayAgentChargeResponse response, String priKey)throws CheckSignatureException{

        // null to ""
        response = fieldNullToEmpty(response);
        
        StringBuffer sValue = new StringBuffer();
        sValue.append(response.getPartnerId());
        sValue.append(response.getMessage());
        sValue.append(response.getPartnerOrderId());
        sValue.append(response.getOrderId());
        sValue.append(response.getAmount());
        
        sValue.append(response.getChannelId());
        sValue.append(response.getChannelOrderId());
        sValue.append(response.getChannelNoticeTime());
        sValue.append(response.getGateNoticeTime());
        sValue.append(response.getFee());
        sValue.append(response.getAgentStatus());
        
        
        String sNewString = null;
        sNewString = DigestUtil.hmacSign(sValue.toString(),priKey);
        if(StringUtils.isBlank(sNewString)||!sNewString.equals(response.getHmac())){
            throw new CheckSignatureException("签名认证失败");
        }
        
        PayAgentChargeResponse dataResponse = new PayAgentChargeResponse();
        
        dataResponse.setPartnerId(response.getPartnerId());
        dataResponse.setMessage(response.getMessage());
        dataResponse.setPartnerOrderId(response.getPartnerOrderId());
        dataResponse.setOrderId(response.getOrderId());
        dataResponse.setAmount(new BigDecimal(StringUtils.isBlank(response.getAmount())?"0":response.getAmount()));
        dataResponse.setChannel(PayAgentChannel.from(response.getChannelId()));
        dataResponse.setChannelOrderId(response.getChannelOrderId());
        if(StringUtils.isNotBlank(response.getChannelNoticeTime())) {
            dataResponse.setChannelNoticeTime(DateUtil.strToTime(response.getChannelNoticeTime(), DateUtil.DATE_FORMAT_PATTEN));
        }
        if(StringUtils.isNotBlank(response.getGateNoticeTime())) {
            dataResponse.setGateNoticeTime(DateUtil.strToTime(response.getGateNoticeTime(), DateUtil.DATE_FORMAT_PATTEN));
        }
        dataResponse.setFee(new BigDecimal(StringUtils.isBlank(response.getFee())? "0" : response.getFee()));
        dataResponse.setAgentStatus(AgentStatus.valueOf(response.getAgentStatus()));
        
        return dataResponse;
    }
    
    /**
     * 获取指定银行的限额信息返回信息
     * @param rspStr
     * @return
     * @throws CheckSignatureException
     */
    public static Result<List<SBankPayLimitVO>> getCyberBankPayLimitResponse(String rspStr) {
        Result<List<SBankPayLimitVO>> result = JSONUtils.fromJson(rspStr, new TypeToken<Result<List<SBankPayLimitVO>>>() {});
        return result;
    }
    
    /**
     * 查询网银充值的所有已经配置的限额信息返回参数
     * @param rspStr
     * @return
     * @throws CheckSignatureException
     */
    public static Result<List<List<SBankPayLimitVO>>> getCyberBankPayLimitListResponse(String rspStr) {
        Result<List<List<SBankPayLimitVO>>> result = JSONUtils.fromJson(rspStr, new TypeToken<Result<List<List<SBankPayLimitVO>>>>() {});
        return result;
    }
    
    /**
     * 对象字符串的属性null变成空串
     * @param obj
     * @return
     */
    private static <T> T fieldNullToEmpty(T obj) {
        if(obj == null) {
            return null;
        }

        try {
            Class<? extends Object> classz = obj.getClass();
            // 获取所有该对象的属性值
            Field fields[] = classz.getDeclaredFields();

            // 遍历属性值，取得所有属性为 null 值的
            for (Field field : fields) {
                if (field.getType().getSimpleName().equalsIgnoreCase("String")) {
                    Method m = classz.getMethod("get" + change(field.getName()));
                    Object name = m.invoke(obj);// 调用该字段的get方法
                    if (name == null) {
                        Method mtd = classz.getMethod("set" + change(field.getName()), new Class[] { String.class });// 取得所需类的方法对象
                        mtd.invoke(obj, new Object[] {""});// 执行相应赋值方法
                    }
                }
            }
        } catch (Exception e) {
        } 
        
        return obj;
    }
        
    private static String change(String src) {
        if (src != null) {
            StringBuffer sb = new StringBuffer(src);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            return sb.toString();
        } else {
            return null;
        }
    }
    
    /**
     * 字符串是否为空
     * @param cs
     * @return
     */
    private static boolean isBlank(String cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
    
    public static void main(String[] args) {
        PartnerPayAgentGetCodeResponse fieldNullToEmpty = GateHelper.fieldNullToEmpty(new PartnerPayAgentGetCodeResponse());
        System.out.println(fieldNullToEmpty);
        System.out.println(new PartnerPayAgentGetCodeResponse());
        
        
    }
}
