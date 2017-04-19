package com.elend.gate.web.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.PayType;
import com.elend.gate.channel.exception.CheckSignatureException;
import com.elend.gate.channel.exception.ParamException;
import com.elend.gate.channel.facade.PartnerFacade;
import com.elend.gate.channel.facade.PayChannelFacade;
import com.elend.gate.channel.facade.vo.ChargeCallbackData;
import com.elend.gate.channel.facade.vo.PartnerChargeData;
import com.elend.gate.channel.facade.vo.PartnerChargeRequest;
import com.elend.gate.conf.facade.SystemConfig;
import com.elend.gate.context.AccessContext;
import com.elend.gate.notify.facade.NotifyFacade;
import com.elend.gate.order.constant.RequestSource;
import com.elend.gate.order.facade.OrderFacade;
import com.elend.gate.order.facade.vo.DepositRequest;
import com.elend.gate.risk.RiskHandlerChain;
import com.elend.gate.risk.vo.RiskParam;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.gate.web.service.PayService;
import com.elend.p2p.Result;
import com.elend.p2p.ServiceException;
import com.elend.p2p.constant.ResultCode;
import com.elend.p2p.util.OrderIdHelper;

@Service
public class PayServiceImpl implements PayService{
    private final static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
    @Autowired
    private PayChannelFacade payChannelFacade;
    @Autowired
    private PartnerFacade partnerFacade;
    @Autowired
    private OrderFacade orderFacade;
    @Autowired
    private NotifyFacade notifyFacade;
    @Autowired
    private RiskHandlerChain riskHandlerChain;
    @Autowired
    private SystemConfig systemConfig;
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<RequestFormData> chargeRequest(PartnerChargeRequest params) {
        //风控
        RiskParam riskParam=new RiskParam();
        riskParam.setIp(params.getIp());
        riskParam.setReferer(AccessContext.getReferer());
        if(StringUtils.isBlank(params.getTimeStamp())){
            return new Result<RequestFormData>(ResultCode.FAILURE,null,"时间戳不能为空");
        }
        long timeStamp=0L;
        try{
            timeStamp=Long.parseLong(params.getTimeStamp());
        }catch(NumberFormatException e){
            logger.error("时间戳不为长整形,timeStamp:"+params.getTimeStamp());
            return new Result<RequestFormData>(ResultCode.FAILURE,null,"时间戳格式错误"); 
        }
        riskParam.setTimeStamp(timeStamp);
        Result<String> r=riskHandlerChain.handle(riskParam);
        if(!r.isSuccess()){
            return new Result<RequestFormData>(ResultCode.FAILURE,null,r.getMessage());
        }
        //1.签名和参数格式验证(约定和P2P通讯格式)
        PartnerChargeData chargeData=null;
        try{
            chargeData=partnerFacade.chargeRequest(params);
        }catch(ParamException e){
            logger.error("参数错误,{}",e.getMessage());
            return new Result<RequestFormData>(ResultCode.FAILURE,null,"参数错误,"+e.getMessage());
        }catch(CheckSignatureException e){
            logger.error("签名验证失败");
            return new Result<RequestFormData>(ResultCode.FAILURE,null,"签名验证失败");
        }catch (ServiceException e) {
            logger.error("充值失败,失败原因:{}",e.getMessage());
            return new Result<RequestFormData>(ResultCode.FAILURE,null,e.getMessage());
        }
        //2.生成请求的form表单
        String orderId=OrderIdHelper.newOrderId();//支付网关订单号
        //增加测试标志位，如果是测试，则金额修改为0.01元
        BigDecimal amount=systemConfig.isTest()?new BigDecimal("0.01"):chargeData.getAmount();
        BigDecimal originAmount=chargeData.getAmount();
        chargeData.setAmount(amount);
        RequestFormData form=payChannelFacade.charge(chargeData.getPayChannel(), orderId,chargeData);
        chargeData.setAmount(originAmount);
        //3.写入请求表
        Result<String> result=orderFacade.sendChargeRequest(chargeData.getPayChannel(), chargeData.getPartnerId(),
                                      chargeData.getPartnerOrderId(), chargeData.getAmount(), 
                                      form, AccessContext.getAccessIp(), RequestSource.from(AccessContext.getAccessSource()),
                                      chargeData.getRedirectUrl(),chargeData.getNotifyUrl(),orderId, chargeData.getPayType());
        if(!result.isSuccess()){
            return new Result<RequestFormData>(ResultCode.FAILURE,null,result.getMessage());
        }
        return new Result<RequestFormData>(ResultCode.SUCCESS, form);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<RequestFormData> chargeCallback(ChannelIdConstant channel,
            Map<String, String> params) {
        //1.支付渠道返回参数解析
        PartnerResult<ChargeCallbackData> partnerResult=null;
        try{
            partnerResult=payChannelFacade.chargeCallback(channel, params);
        }catch(CheckSignatureException e){//像签名认证失败和格式错误等问题可以认为是不合法的回调请求，有可能是数据伪造和攻击。这时候不会把错误信息给到接入方，直接在网关系统提示错误页
            logger.error("支付渠道回调签名验证失败");
            return new Result<RequestFormData>(ResultCode.FAILURE,null,"签名验证失败");
        }catch (ServiceException e) {
            logger.error("充值回调失败,失败原因:{}",e.getMessage());
            return new Result<RequestFormData>(ResultCode.FAILURE,null,e.getMessage());
        }
        String orderId=partnerResult.getObject().getOrderId();
        DepositRequest request=orderFacade.getChargeRequest(orderId);
        if(request==null){
            logger.error("找不到充值请求,订单号:{}",orderId);
            return new Result<RequestFormData>(ResultCode.FAILURE,null,"找不到对应的订单"); 
        }
        //增加测试标志位，如果是测试，则金额按请求时的金额为准,手续费重新计算
        if(systemConfig.isTest()){
            partnerResult.getObject().setAmount(request.getAmount());
        }
        //获取支付类型
        PayType payType = partnerResult.getObject().getPayType();
        //如果渠道没有返回支付类型，以请求时的类型为准
        if(payType == null) {
            payType = request.getPayType();
        }
        if(partnerResult.getObject().getFee()==null){//手续费为空才自己进行计算，易宝的手续费直接获取渠道返回，不需要自己进行计算
            BigDecimal fee=payChannelFacade.feeCalculate(channel, partnerResult.getObject().getAmount(), request.getPayType());
            partnerResult.getObject().setFee(fee);
        }
        
        //2.生成回调的form表单(约定和P2P通讯格式)
        RequestFormData form=partnerFacade.getChargeCallbackForm(channel, request.getPartner(),
                                                                 request.getPartnerOrderId(), partnerResult, request.getCallbackUrl());
        //3.写入订单信息
        Result<String> result=orderFacade.chargeCallback(partnerResult.getCode(), 
                                                         channel, partnerResult.getObject(), params);
        if(!result.isSuccess()){
            return new Result<RequestFormData>(ResultCode.FAILURE, form, partnerResult.getMessage());
        }
        //4.写入通知队列
        String paramUrl=genUrlParamFormat(form.getParams());
        notifyFacade.addQueue(orderId,request.getPartner() ,request.getPartnerOrderId(),
                              paramUrl, request.getNotifyUrl(), new Date(), 1);
        return new Result<RequestFormData>(ResultCode.SUCCESS, form);
    }
    
    /**
     * 计算得到url param格式
     * @param map--参数
     * @return
     */
    private String genUrlParamFormat(Map<String,String> map){
        StringBuffer buffer=new StringBuffer(500);
        for(String key:map.keySet()){
            if(buffer.length()>0){
                buffer.append("&");
            }
            buffer.append(key+"="+map.get(key));
        }
        return buffer.toString();
    }
}
