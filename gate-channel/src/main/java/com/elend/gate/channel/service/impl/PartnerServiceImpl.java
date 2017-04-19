package com.elend.gate.channel.service.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.annotation.Param;
import com.elend.gate.channel.constant.BankAccountType;
import com.elend.gate.channel.constant.BankIdConstant;
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.PayType;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.exception.CheckSignatureException;
import com.elend.gate.channel.exception.ParamException;
import com.elend.gate.channel.facade.vo.ChargeCallbackData;
import com.elend.gate.channel.facade.vo.PartnerBaseRequest;
import com.elend.gate.channel.facade.vo.PartnerBaseResponse;
import com.elend.gate.channel.facade.vo.PartnerChargeData;
import com.elend.gate.channel.facade.vo.PartnerChargeRequest;
import com.elend.gate.channel.facade.vo.PartnerChargeResponse;
import com.elend.gate.channel.facade.vo.PartnerPayAgentChargeData;
import com.elend.gate.channel.facade.vo.PartnerPayAgentChargeRequest;
import com.elend.gate.channel.facade.vo.PartnerPayAgentChargeResponse;
import com.elend.gate.channel.facade.vo.PartnerPayAgentCheckCodeData;
import com.elend.gate.channel.facade.vo.PartnerPayAgentCheckCodeRequest;
import com.elend.gate.channel.facade.vo.PartnerPayAgentCheckCodeResponse;
import com.elend.gate.channel.facade.vo.PartnerPayAgentGetCodeData;
import com.elend.gate.channel.facade.vo.PartnerPayAgentGetCodeRequest;
import com.elend.gate.channel.facade.vo.PartnerWithdrawData;
import com.elend.gate.channel.facade.vo.PartnerWithdrawRequest;
import com.elend.gate.channel.facade.vo.PartnerWithdrawResponse;
import com.elend.gate.channel.facade.vo.WithdrawCallbackData;
import com.elend.gate.channel.service.PartnerService;
import com.elend.gate.channel.util.IChannelStrategy;
import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.conf.facade.PartnerConfig;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.Result;
import com.elend.p2p.ServiceException;
import com.elend.p2p.gson.JSONUtils;
import com.elend.p2p.util.DateUtil;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;
import com.elend.pay.agent.sdk.constant.PayAgentChannel;
import com.elend.pay.agent.sdk.vo.UmbpayAgentVO;
import com.elend.pay.agent.sdk.vo.UmbpaySignWhiteListOneVO;
import com.yeepay.DigestUtil;

@Service("partnerService")
public class PartnerServiceImpl implements PartnerService{
    private final static Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);
    @Autowired
    private PartnerConfig config;

    @Autowired
    @Qualifier("weightChannelStrategyImpl")
    private IChannelStrategy channelStrategy;
    
    private DesPropertiesEncoder desEncoder = new DesPropertiesEncoder();
    
    @Override
    public PartnerChargeData chargeRequest(PartnerChargeRequest params)throws CheckSignatureException,ServiceException{
        logger.info("接入方充值请求,请求参数:{}",JSONUtils.toJson(params,false).toString());
        //1.非空验证
        List<Field> fields = getFields(params);
        for(Field field : fields){
            field.setAccessible(true);
            String value="";
            try {
                value=field.get(params)==null?"":field.get(params).toString();
            } catch (IllegalArgumentException|IllegalAccessException e) {
                logger.error("参数获取失败",e);
            }
            Param require=field.getAnnotation(Param.class);
            if(require!=null&&require.required()&&StringUtils.isBlank(value)){
                throw new ParamException("参数"+field.getName()+"不能为空");
            }
        }
        String partnerId=formatStr(params.getPartnerId());
        String partnerOrderId=formatStr(params.getPartnerOrderId());
        String amount=formatStr(params.getAmount());
        String payChannel=formatStr(params.getPayChannel());
        
        if (ChannelIdConstant.NO_DESIGNATED.name().equals(payChannel)) {
            // 渠道和银行同时不选择
            if (StringUtils.isBlank(params.getBankId())
                    || BankIdConstant.NO_DESIGNATED.getBankId().equals(params.getBankId())) {
                throw new ParamException("渠道和银行必须选择一项");
            }
            // 根据对应的策略选择渠道
            String channel = channelStrategy.getChannel(params.getBankId());
            if (StringUtils.isBlank(channel)) {
                throw new ParamException("暂不支持该银行：bankId:"
                        + params.getBankId());
            }
            payChannel = channel;
        }
        
        String redirectUrl=formatStr(params.getRedirectUrl());
        String notifyUrl=formatStr(params.getNotifyUrl());
        //2.格式验证
        BigDecimal amt=BigDecimal.ZERO;
        try{
            amt=new BigDecimal(amount);
        }catch(NumberFormatException  e){
            logger.error("金额格式错误,amount:{}",amount);
            throw new ParamException("金额格式错误");
        }
        if(amt.setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(amt)!=0){
            logger.error("金额格式错误,小数点只能保留两位小数,amount:{},调整后的金额:{}",amt,amt.setScale(2, BigDecimal.ROUND_HALF_UP));
            amt=amt.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        ChannelIdConstant channel=null;
        try{
            channel=ChannelIdConstant.from(payChannel);
        }catch(IllegalArgumentException e){
            throw new ParamException("支付渠道:"+payChannel+"不存在");
        }
        PartnerConstant partner=null;
        try{
            partner=PartnerConstant.from(partnerId);
        }catch(IllegalArgumentException e){
            throw new ParamException("商户ID:"+partnerId+"不存在");
        }
        BankIdConstant bankId=BankIdConstant.NO_DESIGNATED;
        try{
            if(!StringUtils.isBlank(params.getBankId())){
                bankId=BankIdConstant.from(params.getBankId());
            }
        }catch(IllegalArgumentException e){
            throw new ParamException("银行编码错误:"+params.getBankId());
        }
        long userId = 0;
        try {
            userId = Long.parseLong(params.getUserId());
        } catch (Exception e) {
            throw new ParamException("userId格式不正确:"+params.getUserId());
        }
        
        PayType payType = PayType.PERSON;
        try{
            if(!StringUtils.isBlank(params.getPayType())){
                payType = PayType.valueOf(params.getPayType());
            }
        }catch(IllegalArgumentException e){
            throw new ParamException("银行编码错误:"+params.getBankId());
        }
        
        //3.签名验证
        boolean check=verifySignature(params);
        //boolean check=verifySignature(hmac, partnerId, partnerOrderId, amount, payChannel, redirectUrl, notifyUrl);
        if(!check){
            throw new CheckSignatureException("签名验证失败");
        }
        PartnerChargeData chargeData=new PartnerChargeData();
        chargeData.setPartnerOrderId(partnerOrderId);
        chargeData.setAmount(amt);
        chargeData.setPayChannel(channel);
        chargeData.setRedirectUrl(redirectUrl);
        chargeData.setNotifyUrl(notifyUrl);
        chargeData.setPartnerId(partner);
        chargeData.setBankId(bankId);
        chargeData.setUserId(userId);
        chargeData.setIp(params.getIp());
        chargeData.setPayType(payType);
        return chargeData;
    }
    
    protected String formatStr(String value){
        return value==null?"":value;
    }
    
    @Override
    public RequestFormData getChargeCallbackForm(ChannelIdConstant channelId,PartnerConstant partnerId,String partnerOrderId,
            PartnerResult<? extends ChargeCallbackData> result,String redirectUrl){
        RequestFormData formData=new RequestFormData();
        formData.setRequestUrl(redirectUrl);
        ChargeCallbackData callbackData=result.getObject();
        Date now=new Date();
        String payTime=DateUtil.timeToStr(callbackData.getPayTime(), DateUtil.DATE_FORMAT_PATTEN4);
        String noticeTime=DateUtil.timeToStr(callbackData.getNoticeTime(), DateUtil.DATE_FORMAT_PATTEN4);
        String gateNoticeTime=DateUtil.timeToStr(now, DateUtil.DATE_FORMAT_PATTEN4);
        //组装支付网关的回调数据
        PartnerChargeResponse response=new PartnerChargeResponse();
        //response.setHmac(hmac);
        response.setPartnerId(partnerId.name());
        response.setResultCode(result.getCode());
        response.setMessage(result.getMessage());
        response.setPartnerOrderId(partnerOrderId);
        response.setOrderId(callbackData.getOrderId());
        response.setAmount(callbackData.getAmount().toString());
        response.setChannelId(channelId.name());
        response.setChannelOrderId(callbackData.getChannelOrderId());
        response.setBankId(callbackData.getBankId());
        response.setChannelPayTime(payTime);
        response.setChannelNoticeTime(noticeTime);
        response.setGateNoticeTime(gateNoticeTime);
        response.setIsNotify(result.getObject().isNotify()+"");
        response.setContractNo(result.getObject().getContractNo());
        response.setFee(result.getObject().getFee().toString());
        //生成签名
        String hmac=genSignature(response);
        response.setHmac(hmac);
        Map<String,String> paramMap=objToMap(response);
        formData.setParams(paramMap);
        logger.info("支付网关生成回调的form表单,{}",formData);
        return formData;
    }
    
    private <T> Map<String,String> objToMap(T obj){
        Map<String,String> paramMap=new HashMap<String, String>(30);
        List<Field> list = getFields(obj);
        for(Field field : list){
            field.setAccessible(true);
            String value="";
            try {
                value = field.get(obj)==null?"":field.get(obj).toString();
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error("获取参数失败",e);
            }
            paramMap.put(field.getName(), value);
        }
        return paramMap;
    }
    
    /**
     * 签名验证
     * @param hmac--签名
     * @param partnerId--接入方ID
     * @param partnerOrderId--接入方订单号
     * @param amount--金额
     * @param payChannel--支付渠道
     * @param redirectUrl--重定向地址
     * @param notifyUrl--点对点通知地址
     * @return 验签成功/失败
     */
    @SuppressWarnings("unused")
    private boolean verifySignature(String hmac,String partnerId,String partnerOrderId,String amount,
            String payChannel,String redirectUrl,String notifyUrl) {
        PartnerConstant partner=PartnerConstant.P2P;
        try{
            partner=PartnerConstant.from(partnerId);
        }catch(IllegalArgumentException e){
            logger.error("接入方ID错误,{}",partnerId);
            return false;
        }
        StringBuffer sValue = new StringBuffer();
        sValue.append(partnerId);
        sValue.append(partnerOrderId);
        sValue.append(amount);
        sValue.append(payChannel);
        sValue.append(redirectUrl);
        sValue.append(notifyUrl);
        String sNewString = null;
        sNewString = DigestUtil.hmacSign(sValue.toString(),config.getPriKey(partner));
        if (hmac.equals(sNewString)) {
            return (true);
        }
        return (false);
    }
    /**
     * 签名验证
     * @param params--请求参数
     * @return
     */
    private boolean verifySignature(PartnerChargeRequest params) {
        PartnerConstant partner=PartnerConstant.P2P;
        try{
            partner=PartnerConstant.from(params.getPartnerId());
        }catch(IllegalArgumentException e){
            logger.error("接入方ID错误,{}",params.getPartnerId());
            return false;
        }
        //签名为空直接返回失败
        if(StringUtils.isBlank(params.getHmac())){
            return false;
        }
        List<Field> fields=getFields(params);
        //顺序存放加密参数
        String[] valueArray=new String[fields.size()];
        //参与加密的参数数量
        int size=0;
        //遍历所有注解得到加密后的签名
        for(Field field:fields){
            field.setAccessible(true);
            Param param=field.getAnnotation(Param.class);
            if(param!=null&&param.sequence()>=0){
                String value="";
                try {
                    value = field.get(params)==null?"":field.get(params).toString();
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    logger.error("获取参数失败",e);
                }
                valueArray[param.sequence()]=value;
                size++;
            }
        }
        StringBuffer sValue = new StringBuffer();
        for(int i=0;i<size;i++){
            sValue.append(valueArray[i]==null?"":valueArray[i]);
        }
        String sNewString = null;
        sNewString = DigestUtil.hmacSign(sValue.toString(),config.getPriKey(partner));
        if (params.getHmac().equals(sNewString)) {
            return (true);
        }
        return (false);
    }
    
    /**
     * 签名验证
     * @param params--请求参数
     * @return
     */
    private boolean verifySignature(PartnerBaseRequest params) {
        PartnerConstant partner=PartnerConstant.P2P;
        try{
            partner=PartnerConstant.from(params.getPartnerId());
        }catch(IllegalArgumentException e){
            logger.error("接入方ID错误,{}",params.getPartnerId());
            return false;
        }
        //签名为空直接返回失败
        if(StringUtils.isBlank(params.getHmac())){
            return false;
        }
        List<Field> fields=getFields(params);
        //顺序存放加密参数
        String[] valueArray=new String[fields.size()];
        //参与加密的参数数量
        int size=0;
        //遍历所有注解得到加密后的签名
        for(Field field:fields){
            field.setAccessible(true);
            Param param=field.getAnnotation(Param.class);
            if(param!=null&&param.sequence()>=0){
                String value="";
                try {
                    value = field.get(params)==null?"":field.get(params).toString();
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    logger.error("获取参数失败",e);
                }
                valueArray[param.sequence()]=value;
                size++;
            }
        }
        StringBuffer sValue = new StringBuffer();
        for(int i=0;i<size;i++){
            sValue.append(valueArray[i]==null?"":valueArray[i]);
        }
        String sNewString = null;
        sNewString = DigestUtil.hmacSign(sValue.toString(),config.getPriKey(partner));
        if (params.getHmac().equals(sNewString)) {
            return (true);
        }
        return (false);
    }
    
    /**
     * 获取类的所有的属性
     * @param list
     * @param c
     */
    @SuppressWarnings("rawtypes")
    private void getFields(List<Field> list,Class c){
        if(list==null)list=new ArrayList<Field>();
        list.addAll(Arrays.asList(c.getDeclaredFields()));
        if(c.getSuperclass()==null)return;
        getFields(list, c.getSuperclass());
    }
    /**
     * 获取类的所有的属性
     * @param params
     */
    private List<Field> getFields(PartnerChargeRequest params){
        List<Field> list=new ArrayList<Field>();
        getFields(list, params.getClass());
        return list;
    }
    
    /**
     * 获取类的所有的属性
     * @param params
     */
    private List<Field> getFields(Object o){
        List<Field> list=new ArrayList<Field>();
        getFields(list, o.getClass());
        return list;
    }

    /**
     * 生成签名
     * @param partnerId--接入方ID
     * @param resultCode--返回码
     * @param message--返回消息
     * @param partnerOrderId--接入方订单号
     * @param orderId--网关订单号
     * @param amount--金额(支付渠道返回为准)
     * 下面的参数接入方可以根据自己需要
     * @param payChannelId--支付渠道
     * @param channelOrderId--支付渠道订单号
     * @param bankId--银行编号(这里暂时还没做转换，直接返回支付渠道的)
     * @param channelPayTime--渠道支付时间
     * @param channelNoticeTime--渠道通知时间
     * @param gateNoticeTime--网关通知时间
     * @return
     */
    @SuppressWarnings("unused")
    private String genSignature(String partnerId,String resultCode,String message,
            String partnerOrderId, String orderId,String amount,String payChannelId,
            String channelOrderId,String bankId,String channelPayTime,String channelNoticeTime,String gateNoticeTime) {
        PartnerConstant partner=PartnerConstant.P2P;
        try{
            partner=PartnerConstant.from(partnerId);
        }catch(IllegalArgumentException e){
            logger.error("接入方ID错误,{}",partnerId);
            return "";
        }
        StringBuffer sValue = new StringBuffer();
        sValue.append(partnerId);
        sValue.append(resultCode);
        sValue.append(message);
        sValue.append(partnerOrderId);
        sValue.append(orderId);
        sValue.append(amount);
        
        sValue.append(payChannelId);
        sValue.append(channelOrderId);
        sValue.append(bankId);
        sValue.append(channelPayTime);
        sValue.append(channelNoticeTime);
        sValue.append(gateNoticeTime);
        String sNewString = null;
        sNewString = DigestUtil.hmacSign(sValue.toString(),config.getPriKey(partner));
        return sNewString;
    }
    
    /**
     * 生成签名
     * @param response--返回给接入方的参数
     * @return
     */
    private String genSignature(PartnerChargeResponse response) {
        PartnerConstant partner=PartnerConstant.P2P;
        try{
            partner=PartnerConstant.from(response.getPartnerId());
        }catch(IllegalArgumentException e){
            logger.error("接入方ID错误,{}",response.getPartnerId());
            return "";
        }
        //顺序存放加密参数
        String[] valueArray=new String[response.getClass().getDeclaredFields().length];
        //参与加密的参数数量
        int size=0;
        //遍历所有注解得到加密后的签名
        for(Field field:response.getClass().getDeclaredFields()){
            field.setAccessible(true);
            Param param=field.getAnnotation(Param.class);
            if(param!=null&&param.sequence()>=0){
                String value="";
                try {
                    value = field.get(response)==null?"":field.get(response).toString();
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    logger.error("获取参数失败",e);
                }
                valueArray[param.sequence()]=value;
                size++;
            }
        }
        StringBuffer sValue = new StringBuffer();
        for(int i=0;i<size;i++){
            sValue.append(valueArray[i]==null?"":valueArray[i]);
        }
        String sNewString = null;
        sNewString = DigestUtil.hmacSign(sValue.toString(),config.getPriKey(partner));
        return sNewString;
    }
    
    /**
     * 生成签名
     * @param response--返回给接入方的参数
     * @return
     */
    public String genSignature(PartnerBaseResponse response) {
        PartnerConstant partner=PartnerConstant.P2P;
        try{
            partner=PartnerConstant.from(response.getPartnerId());
        }catch(IllegalArgumentException e){
            logger.error("接入方ID错误,{}",response.getPartnerId());
            return "";
        }
        //顺序存放加密参数
        int length = response.getClass().getDeclaredFields().length;
        if(response.getClass().getSuperclass() != null) {
            length += response.getClass().getSuperclass().getDeclaredFields().length;
        }
        String[] valueArray=new String[length];
        //参与加密的参数数量
        int size=0;
        //遍历所有注解得到加密后的签名
        for(Field field:response.getClass().getDeclaredFields()){
            field.setAccessible(true);
            Param param=field.getAnnotation(Param.class);
            if(param!=null&&param.sequence()>=0){
                String value="";
                try {
                    value = field.get(response)==null?"":field.get(response).toString();
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    logger.error("获取参数失败",e);
                }
                valueArray[param.sequence()]=value;
                size++;
            }
        }
        //遍历父类注解
        if(response.getClass().getSuperclass() != null) {
            for(Field field : response.getClass().getSuperclass().getDeclaredFields()){
                field.setAccessible(true);
                Param param=field.getAnnotation(Param.class);
                if(param!=null&&param.sequence()>=0){
                    String value="";
                    try {
                        value = field.get(response)==null?"":field.get(response).toString();
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        logger.error("获取参数失败",e);
                    }
                    valueArray[param.sequence()]=value;
                    size++;
                }
            }
        }
        StringBuffer sValue = new StringBuffer();
        for(int i=0;i<size;i++){
            sValue.append(valueArray[i]==null?"":valueArray[i]);
        }
        String sNewString = null;
        sNewString = DigestUtil.hmacSign(sValue.toString(),config.getPriKey(partner));
        return sNewString;
    }

    @Override
    public PartnerWithdrawData checkWithdrawParams(PartnerWithdrawRequest request)throws ParamException, CheckSignatureException{
        logger.info("接入方提现请求,请求参数:{}",JSONUtils.toJson(request, false).toString());
        //1.非空验证
        for(Field field:request.getClass().getDeclaredFields()){
            field.setAccessible(true);
            String value="";
            try {
                value=field.get(request)==null?"":field.get(request).toString();
            } catch (IllegalArgumentException|IllegalAccessException e) {
                logger.error("参数获取失败",e);
            }
            Param require=field.getAnnotation(Param.class);
            if(require!=null&&require.required()&&StringUtils.isBlank(value)){
                throw new ParamException("参数"+field.getName()+"不能为空");
            }
        }
        
        String partnerId=formatStr(request.getPartnerId());
        String amount=formatStr(request.getAmount());
        String channelStr=formatStr(request.getChannel());
        
        //2.格式验证
        BigDecimal amt=BigDecimal.ZERO;
        try{
            amt=new BigDecimal(amount);
        }catch(NumberFormatException  e){
            logger.error("金额格式错误,amount:{}",amount);
            throw new ParamException("金额格式错误");
        }
        WithdrawChannel channel=null;
        try{
            channel = WithdrawChannel.from(channelStr);
        }catch(IllegalArgumentException e){
            throw new ParamException("提现渠道:"+channelStr+"不存在");
        }
        PartnerConstant partner=null;
        try{
            partner=PartnerConstant.from(partnerId);
        }catch(IllegalArgumentException e){
            throw new ParamException("商户ID:"+partnerId+"不存在");
        }
        BankIdConstant bankId=BankIdConstant.NO_DESIGNATED;
        try{
            bankId=BankIdConstant.from(request.getBankId());
        }catch(IllegalArgumentException e){
            throw new ParamException("银行编码错误:"+request.getBankId());
        }
        
        //3.签名验证
        boolean check = verifySignature(request);

        if(!check){
            throw new CheckSignatureException("签名验证失败");
        }
        //4.加密参数解密
        String userNameDecode= desEncoder.decode(request.getUserName());
        String bankAccountDecode=desEncoder.decode(request.getBankAccount());
        String identityCardDecode= StringUtils.isBlank(request.getIdentityCard())?"":desEncoder.decode(request.getIdentityCard());
        PartnerWithdrawData data = new PartnerWithdrawData();
        data.setPartnerId(partner);
        data.setPartnerOrderId(request.getPartnerOrderId());
        data.setAmount(amt);
        data.setChannel(channel);
        data.setNotifyUrl(request.getNotifyUrl());
        data.setBankId(bankId);
        data.setBankAccount(bankAccountDecode);
        data.setUserName(userNameDecode);
        data.setBankCityId(request.getBankCityId());
        data.setBankProvinceId(request.getBankProvinceId());
        data.setBankBranchName(request.getBankBranchName());
        data.setAccountType(request.getAccountType());
        data.setIp(request.getIp());
        data.setIdentityCard(identityCardDecode);
        
        return data;
    }

    @Override
    public RequestFormData getWithdrawCallbackForm(WithdrawChannel channel,
            String partnerId, String partnerOrderId,
            PartnerResult<WithdrawCallbackData> result,
            String notifyUrl) {
        
        RequestFormData formData=new RequestFormData();
        formData.setRequestUrl("");
        WithdrawCallbackData callbackData = result.getObject();
        Date now=new Date();
        
        //String payTime=DateUtil.timeToStr(callbackData.getPayTime(), DateUtil.DATE_FORMAT_PATTEN4);
        String noticeTime=DateUtil.timeToStr(callbackData.getCallbackTime(), DateUtil.DATE_FORMAT_PATTEN4);
        String gateNoticeTime=DateUtil.timeToStr(now, DateUtil.DATE_FORMAT_PATTEN4);
        //组装支付网关的回调数据
        PartnerWithdrawResponse response=new PartnerWithdrawResponse();
        response.setPartnerId(partnerId);
        response.setMessage(result.getObject()==null?result.getMessage():result.getObject().getMessage());
        response.setPartnerOrderId(partnerOrderId);
        response.setOrderId(callbackData.getOrderId());
        if(callbackData.getAmount() != null) {
            response.setAmount(callbackData.getAmount().toString());
        }
        response.setChannelId(channel.name());
        response.setChannelOrderId(callbackData.getChannelOrderId());
        response.setChannelNoticeTime(noticeTime);
        response.setGateNoticeTime(gateNoticeTime);
        response.setFee(result.getObject().getFee().toString());
        response.setWithdrawStatus(result.getObject().getWithdrawStatus().name());
        //生成签名
        String hmac=genSignature(response);
        response.setHmac(hmac);
        Map<String,String> paramMap=objToMap(response);
        formData.setParams(paramMap);
        logger.info("支付网关生成提现回调的form表单,{}",formData);
        return formData;
    }

    @Override
    public PartnerPayAgentGetCodeData payAgentGetCode(
            PartnerPayAgentGetCodeRequest request) {
        logger.info("接入方获取代扣验证码,请求参数:{}",JSONUtils.toJson(request, false).toString());
        //1.非空验证
        for(Field field:request.getClass().getDeclaredFields()){
            field.setAccessible(true);
            String value="";
            try {
                value=field.get(request)==null?"":field.get(request).toString();
            } catch (IllegalArgumentException|IllegalAccessException e) {
                logger.error("参数获取失败",e);
            }
            Param require=field.getAnnotation(Param.class);
            if(require!=null&&require.required()&&StringUtils.isBlank(value)){
                throw new ParamException("参数"+field.getName()+"不能为空");
            }
        }
        
        String partnerId=formatStr(request.getPartnerId());
        String amount=formatStr(request.getAmount());
        String channelStr=formatStr(request.getChannel());
        
        //2.格式验证
        BigDecimal amt=BigDecimal.ZERO;
        try{
            amt=new BigDecimal(amount);
        }catch(NumberFormatException  e){
            logger.error("金额格式错误,amount:{}",amount);
            throw new ParamException("金额格式错误");
        }
        PayAgentChannel channel=null;
        try{
            channel = PayAgentChannel.from(channelStr);
        }catch(IllegalArgumentException e){
            throw new ParamException("代扣渠道:"+channelStr+"不存在");
        }
        PartnerConstant partner=null;
        try{
            partner = PartnerConstant.from(partnerId);
        }catch(IllegalArgumentException e){
            throw new ParamException("商户ID:"+partnerId+"不存在");
        }
        BankIdConstant bankId = BankIdConstant.NO_DESIGNATED;
        try{
            bankId = BankIdConstant.from(request.getBankId());
        }catch(IllegalArgumentException e){
            throw new ParamException("银行编码错误:"+request.getBankId());
        }
        
        //3.签名验证
        boolean check = verifySignature(request);

        if(!check){
            throw new CheckSignatureException("签名验证失败");
        }
        
        //4.加密参数解密
        String realNameDecode = desEncoder.decode(request.getRealName());
        String cardNoDecode = desEncoder.decode(request.getCardNo());
        String idNoDecode = desEncoder.decode(request.getIdNo());
        String cellphoneDecode = desEncoder.decode(request.getCellphone());
        
        PartnerPayAgentGetCodeData data = new PartnerPayAgentGetCodeData();
        data.setPartnerId(partner);
        data.setPartnerOrderId(request.getPartnerOrderId());
        data.setAmount(amt);
        data.setChannel(channel);
        data.setBankId(bankId);
        data.setCardNo(cardNoDecode);
        data.setRealName(realNameDecode);
        data.setIp(request.getIp());
        data.setIdNo(idNoDecode);
        data.setCellphone(cellphoneDecode);
        
        logger.info("参数检查完毕, data:{}",  desEncoder.encode(data.toString()));
        
        return data;
    }

    @Override
    public PartnerPayAgentCheckCodeData payAgentCheckCode(
            PartnerPayAgentCheckCodeRequest request) {
        
        logger.info("接入方校验代扣验证码,请求参数:{}", JSONUtils.toJson(request, false).toString());
        
        //1.非空验证
        for(Field field:request.getClass().getDeclaredFields()){
            field.setAccessible(true);
            String value="";
            try {
                value=field.get(request)==null?"":field.get(request).toString();
            } catch (IllegalArgumentException|IllegalAccessException e) {
                logger.error("参数获取失败",e);
            }
            Param require=field.getAnnotation(Param.class);
            if(require!=null&&require.required()&&StringUtils.isBlank(value)){
                throw new ParamException("参数"+field.getName()+"不能为空");
            }
        }
        
        String partnerId=formatStr(request.getPartnerId());
        
        //2.格式验证
        PartnerConstant partner=null;
        try{
            partner = PartnerConstant.from(partnerId);
        }catch(IllegalArgumentException e){
            throw new ParamException("商户ID:"+partnerId+"不存在");
        }

        //3.签名验证
        boolean check = verifySignature(request);

        if(!check){
            throw new CheckSignatureException("签名验证失败");
        }
        
        //4.加密参数解密
        
        PartnerPayAgentCheckCodeData data = new PartnerPayAgentCheckCodeData();
        data.setPartnerId(partner);
        data.setPartnerOrderId(request.getPartnerOrderId());
        data.setAuthCode(request.getAuthCode());
        data.setIp(request.getIp());
        data.setNotifyUrl(request.getNotifyUrl());
        data.setPartnerApplyOrderId(request.getPartnerApplyOrderId());
        
        logger.info("参数检查完毕, data:{}",  data.toString());
        
        return data;
    }

    @Override
    public RequestFormData getPayAgentAuthCallbackForm(String channel,
            String orderId, String notifyUrl, BigDecimal authAmt,
            String partnerId, String partnerOrderId,
            Result<UmbpaySignWhiteListOneVO> requestResult) {
        
        RequestFormData formData = new RequestFormData();
        formData.setRequestUrl("");
        
        PartnerPayAgentCheckCodeResponse response = new PartnerPayAgentCheckCodeResponse();
        response.setPartnerId(partnerId);
        response.setMessage(requestResult.isSuccess() ? requestResult.getObject().getErrorMsg() : requestResult.getMessage());
        response.setPartnerOrderId(partnerOrderId);
        response.setOrderId(orderId);
        response.setAmount(authAmt.toString());
        response.setChannelId(channel);
        response.setChannelNoticeTime(DateUtil.timeToStr(new Date(), DateUtil.DATE_FORMAT_PATTEN));
        response.setFee(requestResult.getObject().getFee() == null ? "" : requestResult.getObject().getFee().toString());
        response.setAgentStatus(requestResult.getObject().getSignState().name());
        
        String hmac = genSignature(response);
        response.setHmac(hmac);
        
        Map<String,String> paramMap=objToMap(response);
        formData.setParams(paramMap);
        
        logger.info("支付网关生成待收鉴权回调的form表单,{}", formData);
        return formData;
    }

    @Override
    public PartnerPayAgentChargeData payAgentCharge(
            PartnerPayAgentChargeRequest request) {
        
        logger.info("接入方代收, 请求参数:{}", JSONUtils.toJson(request, false).toString());
        
        //1.非空验证
        for(Field field:request.getClass().getDeclaredFields()){
            field.setAccessible(true);
            String value="";
            try {
                value=field.get(request)==null?"":field.get(request).toString();
            } catch (IllegalArgumentException|IllegalAccessException e) {
                logger.error("参数获取失败",e);
            }
            Param require=field.getAnnotation(Param.class);
            if(require!=null&&require.required()&&StringUtils.isBlank(value)){
                throw new ParamException("参数"+field.getName()+"不能为空");
            }
        }
        
        String partnerId=formatStr(request.getPartnerId());
        
        //2.格式验证
        PartnerConstant partner=null;
        try{
            partner = PartnerConstant.from(partnerId);
        }catch(IllegalArgumentException e){
            throw new ParamException("商户ID:"+partnerId+"不存在");
        }
        
        BankIdConstant bankId = null;
        try{
            bankId = BankIdConstant.from(request.getBankId());
        }catch(IllegalArgumentException e){
            throw new ParamException("银行ID:" + request.getBankId() + "不存在");
        }
        
        PayAgentChannel channel = null;
        try{
            channel = PayAgentChannel.from(request.getChannel());
        }catch(IllegalArgumentException e){
            throw new ParamException("渠道:" + request.getChannel() + "不存在");
        }
        
        BankAccountType accountType = null;
        try{
            accountType = BankAccountType.from(request.getAccountType());
        }catch(IllegalArgumentException e){
            throw new ParamException("银行卡类型:" + request.getAccountType() + "不存在");
        }

        //3.签名验证
        boolean check = verifySignature(request);

        if(!check){
            throw new CheckSignatureException("签名验证失败");
        }
        
        //4.加密参数解密
        String cardNo = desEncoder.decode(request.getCardNo());
        String realName = desEncoder.decode(request.getRealName());
        String idNo = desEncoder.decode(request.getIdNo());
        String cellphone = desEncoder.decode(request.getCellphone());
        
        PartnerPayAgentChargeData data = new PartnerPayAgentChargeData();
        data.setPartnerId(partner);
        data.setPartnerOrderId(request.getPartnerOrderId());
        data.setBankId(bankId);
        data.setCardNo(cardNo);
        data.setRealName(realName);
        data.setAmount(new BigDecimal(request.getAmount()));
        data.setNotifyUrl(request.getNotifyUrl());
        data.setChannel(channel);
        data.setIp(request.getIp());
        data.setAccountType(accountType);
        data.setIdNo(idNo);
        data.setCellphone(cellphone);
        
        logger.info("参数检查完毕, data:{}",  desEncoder.encode(data.toString()));
        
        return data;
    }

    @Override
    public RequestFormData getPayAgentChargeCallbackForm(
            PayAgentChannel channel, String orderId, String notifyUrl,
            BigDecimal amount, String channelOrderId, String partnerId,
            String partnerOrderId, Result<UmbpayAgentVO> requestResult) {
        
        RequestFormData formData = new RequestFormData();
        formData.setRequestUrl("");
        
        PartnerPayAgentChargeResponse response = new PartnerPayAgentChargeResponse();
        response.setPartnerId(partnerId);
        response.setMessage(requestResult.isSuccess() ? requestResult.getObject().getErrorMsg() : requestResult.getMessage());
        response.setPartnerOrderId(partnerOrderId);
        response.setOrderId(orderId);
        response.setAmount(amount.toString());
        response.setChannelId(channel.name());
        response.setChannelOrderId(channelOrderId);
        response.setFee(requestResult.getObject().getFee() == null ? "" : requestResult.getObject().getFee().toString());
        response.setChannelNoticeTime(DateUtil.timeToStr(new Date(), DateUtil.DATE_FORMAT_PATTEN));
        response.setAgentStatus(requestResult.getObject().getSignState().name());
        
        String hmac = genSignature(response);
        response.setHmac(hmac);
        
        Map<String,String> paramMap=objToMap(response);
        formData.setParams(paramMap);
        
        logger.info("支付网关生成待收回调的form表单,{}", formData);
        return formData;
    }
}
