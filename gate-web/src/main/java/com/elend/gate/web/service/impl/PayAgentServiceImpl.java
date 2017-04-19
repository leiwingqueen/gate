package com.elend.gate.web.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elend.gate.channel.constant.BankAccountType;
import com.elend.gate.channel.exception.CheckSignatureException;
import com.elend.gate.channel.exception.ParamException;
import com.elend.gate.channel.facade.PartnerFacade;
import com.elend.gate.channel.facade.vo.PartnerPayAgentChargeData;
import com.elend.gate.channel.facade.vo.PartnerPayAgentChargeRequest;
import com.elend.gate.channel.facade.vo.PartnerPayAgentChargeResponse;
import com.elend.gate.channel.facade.vo.PartnerPayAgentCheckCodeData;
import com.elend.gate.channel.facade.vo.PartnerPayAgentCheckCodeRequest;
import com.elend.gate.channel.facade.vo.PartnerPayAgentCheckCodeResponse;
import com.elend.gate.channel.facade.vo.PartnerPayAgentGetCodeData;
import com.elend.gate.channel.facade.vo.PartnerPayAgentGetCodeRequest;
import com.elend.gate.channel.facade.vo.PartnerPayAgentGetCodeResponse;
import com.elend.gate.channel.mapper.CBankIdConfigMapper;
import com.elend.gate.notify.facade.NotifyFacade;
import com.elend.gate.order.facade.OrderFacade;
import com.elend.gate.order.vo.PPayAgentApplyRequestVO;
import com.elend.gate.risk.RiskHandlerChain;
import com.elend.gate.risk.vo.RiskParam;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.gate.web.service.PayAgentService;
import com.elend.p2p.Result;
import com.elend.p2p.ServiceException;
import com.elend.p2p.constant.ResultCode;
import com.elend.p2p.util.DateUtil;
import com.elend.pay.agent.sdk.constant.AgentStatus;
import com.elend.pay.agent.sdk.constant.PayAgentChannel;
import com.elend.pay.agent.sdk.facade.UmbpayAgentFacade;
import com.elend.pay.agent.sdk.vo.BankInfoVO;
import com.elend.pay.agent.sdk.vo.UmbpayAccreditVO;
import com.elend.pay.agent.sdk.vo.UmbpayAgentVO;
import com.elend.pay.agent.sdk.vo.UmbpaySignWhiteListOneVO;

@Service
public class PayAgentServiceImpl implements PayAgentService {
    
    private final static Logger logger = LoggerFactory.getLogger(PayAgentServiceImpl.class);
    
    @Autowired
    @Qualifier("payAgentRiskHandlerChain")
    private RiskHandlerChain payAgentRiskHandlerChain;
    
    @Autowired
    private PartnerFacade partnerFacade;
    
    @Autowired
    private UmbpayAgentFacade umbpayAgentFacade;
    
    @Autowired
    private OrderFacade orderFacade;
    
    @Autowired
    private CBankIdConfigMapper bankConfigMapper;
    
    @Autowired
    private NotifyFacade notifyFacade;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<PartnerPayAgentGetCodeResponse> getCode(PartnerPayAgentGetCodeRequest params) {
        
        logger.info("参数校验开始...");
        //风控
        RiskParam riskParam=new RiskParam();
        riskParam.setIp(params.getIp());
        riskParam.setReferer("");
        if(StringUtils.isBlank(params.getTimeStamp())){
            return new Result<>(ResultCode.FAILURE,null,"时间戳不能为空");
        }
        long timeStamp=0L;
        try{
            timeStamp=Long.parseLong(params.getTimeStamp());
        }catch(NumberFormatException e){
            logger.error("时间戳不为长整形,timeStamp:"+params.getTimeStamp());
            return new Result<>(ResultCode.FAILURE,null,"时间戳格式错误"); 
        }
        riskParam.setTimeStamp(timeStamp);
        Result<String> r = payAgentRiskHandlerChain.handle(riskParam);
        if(!r.isSuccess()){
            return new Result<>(ResultCode.FAILURE,  null, r.getMessage());
        }
        //1.签名和参数格式验证(约定和P2P通讯格式)
        PartnerPayAgentGetCodeData data = null;
        try{
            data = partnerFacade.payAgentGetCode(params);
        }catch(ParamException e){
            logger.error("参数错误,{}",e.getMessage());
            return new Result<>(ResultCode.FAILURE, null, "参数错误,"+e.getMessage());
        }catch(CheckSignatureException e){
            logger.error("签名验证失败");
            return new Result<>(ResultCode.FAILURE,null,"签名验证失败");
        }catch (ServiceException e) {
            logger.error("代扣鉴权获取验证码失败,失败原因:{}",e.getMessage());
            return new Result<>(ResultCode.FAILURE,null,e.getMessage());
        }
        
        logger.info("参数校验完成，插入请求订单...");
        
        //转换成渠道的银行名称
        String channelBankId = bankConfigMapper.getChannelBankId(data.getChannel().name(), data.getBankId().getBankId());
        if(StringUtils.isBlank(channelBankId)) {
            logger.info("找不到对应的银行编号, partnerOrderId:{}, channel:{}, bankId:{}", data.getPartnerOrderId(), data.getChannel(), data.getBankId());
            return new Result<>(ResultCode.FAILURE, null, "支付网关找不到对应渠道的银行编号");
        }
        
        //支付网关的订单号和P2P一致，方便查找问题
        String orderId = data.getPartnerOrderId();
        
        //记录请求的订单号
        Result<String> orderRequestResult = orderFacade.sendPayAgentGetCodeRequest(data, orderId);
        logger.info("插入请求订单号result:{}", orderRequestResult);
        if(!orderRequestResult.isSuccess()) {
            return new Result<>(ResultCode.FAILURE, null, orderRequestResult.getMessage());
        }
        
        logger.info("开始发送第三方请求...");
        
        BankInfoVO bankInfoVO = new BankInfoVO();
        bankInfoVO.setBankAccount(data.getCardNo());
        bankInfoVO.setUserName(data.getRealName());
        bankInfoVO.setBankName(channelBankId);
        
        //发送第三方请求
        Result<UmbpayAccreditVO> requestResult = umbpayAgentFacade.userAccredit(data.getIdNo(), data.getCellphone(), data.getAmount(), bankInfoVO);
        logger.info("发送第三方请求result:{}", requestResult);
        
        Result<String> callbackResult = orderFacade.PayAgentGetCodeCallback(requestResult.isSuccess(), orderId, data.getChannel(), requestResult.getObject());
        logger.info("回调订单处理result:{}", callbackResult);
        
        if(!requestResult.isSuccess()){
            
            logger.info("第三方获取代收鉴权验证码失败，直接返回失败结果给合作方, partnerOrderId:{}", data.getPartnerOrderId());
            
            return new Result<>(ResultCode.FAILURE, null, requestResult.getMessage());
        }
        
        logger.info("封装返回合作方参数...");
        
        PartnerPayAgentGetCodeResponse response = new PartnerPayAgentGetCodeResponse();
        response.setPartnerId(data.getPartnerId().name());
        response.setMessage(requestResult.getMessage());
        response.setPartnerOrderId(data.getPartnerOrderId());
        response.setOrderId(orderId);
        response.setAmount(data.getAmount().toString());
        response.setChannelId(data.getChannel().name());
        response.setChannelOrderId(requestResult.getObject().getChannelOrderId());
        
        String hmac = partnerFacade.genSignature(response);
        response.setHmac(hmac);
        
        logger.info("接口处理完毕，返回， response:{}", response);
        
        return new Result<>(ResultCode.SUCCESS, response);
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED)  不加事务，使得 记录请求、回调更新请求在各自的事务当中，后面异常不会导致前面的回滚
    public Result<PartnerPayAgentCheckCodeResponse> checkCode(
            PartnerPayAgentCheckCodeRequest params) {
        
        logger.info("参数校验开始...， params：{}", params);
        
        //风控
        RiskParam riskParam=new RiskParam();
        riskParam.setIp(params.getIp());
        riskParam.setReferer("");
        if(StringUtils.isBlank(params.getTimeStamp())){
            return new Result<>(ResultCode.FAILURE,null,"时间戳不能为空");
        }
        long timeStamp=0L;
        try{
            timeStamp=Long.parseLong(params.getTimeStamp());
        }catch(NumberFormatException e){
            logger.error("时间戳不为长整形,timeStamp:"+params.getTimeStamp());
            return new Result<>(ResultCode.FAILURE,null,"时间戳格式错误"); 
        }
        riskParam.setTimeStamp(timeStamp);
        Result<String> r = payAgentRiskHandlerChain.handle(riskParam);
        if(!r.isSuccess()){
            return new Result<>(ResultCode.FAILURE,  null, r.getMessage());
        }
        //1.签名和参数格式验证(约定和P2P通讯格式)
        PartnerPayAgentCheckCodeData data = null;
        try{
            data = partnerFacade.payAgentCheckCode(params);
        }catch(ParamException e){
            logger.error("参数错误,{}",e.getMessage());
            return new Result<>(ResultCode.FAILURE, null, "参数错误,"+e.getMessage());
        }catch(CheckSignatureException e){
            logger.error("签名验证失败");
            return new Result<>(ResultCode.FAILURE,null,"签名验证失败");
        }catch (ServiceException e) {
            logger.error("代扣鉴权获取验证码失败,失败原因:{}",e.getMessage());
            return new Result<>(ResultCode.FAILURE,null,e.getMessage());
        }
        
        logger.info("参数校验完成，插入请求订单号...");
        
        String orderId = data.getPartnerOrderId();
        
        //记录请求的订单号
        Result<PPayAgentApplyRequestVO> orderRequestResult = orderFacade.sendPayAgentCheckCodeRequest(orderId, data);
        logger.info("更新订单状态, result:{}", orderRequestResult.isSuccess());
        PPayAgentApplyRequestVO applyVo = orderRequestResult.getObject();

        //订单状态更新失败
        if(!orderRequestResult.isSuccess()) {
            logger.error("代扣鉴权获取验证码请求订单处理失败, partnerOrderId:{}, 失败原因:{}", data.getPartnerOrderId(), orderRequestResult.getMessage());
            return new Result<>(ResultCode.FAILURE, null, orderRequestResult.getMessage());
        } 
        
        logger.info("请求订单处理成功, 发送第三方请求...");
        
        //发送第三方请求
        BankInfoVO bankInfoVO = new BankInfoVO();
        bankInfoVO.setBankAccount(applyVo.getCardNo());
        //转换成渠道的银行名称
        String channelBankId = bankConfigMapper.getChannelBankId(applyVo.getChannel(), applyVo.getBankId());
        if(StringUtils.isBlank(channelBankId)) {
            logger.info("找不到对应的银行编号, partnerOrderId:{}, channel:{}, bankId:{}", data.getPartnerOrderId(), applyVo.getChannel(), applyVo.getBankId());
            throw new ServiceException("找不到对应的银行编号");
        }
        bankInfoVO.setBankName(channelBankId);
        bankInfoVO.setUserName(applyVo.getRealName());
        
        UmbpayAccreditVO umbpayAccreditVO = new UmbpayAccreditVO();
        umbpayAccreditVO.setMerOrderId(applyVo.getMerOrderId());
        umbpayAccreditVO.setPhoneToken(applyVo.getPhoneToken());
        umbpayAccreditVO.setCustId(applyVo.getCustId());
        
        Result<UmbpaySignWhiteListOneVO> requestResult = umbpayAgentFacade.userSignWhiteList(applyVo.getIdNo(), applyVo.getCellphone(), applyVo.getAmount(), bankInfoVO, umbpayAccreditVO, data.getAuthCode());
        logger.info("第三方请求结果, requestResult:{}", requestResult);
        
        Result<String> callbackResult = orderFacade.PayAgentCheckCodeCallback(requestResult.isSuccess(), PayAgentChannel.from(applyVo.getChannel()), orderId, requestResult.getObject());
        logger.info("回调订单处理orderId:{}, result:{}", orderId, callbackResult);
        
        logger.info("封装返回合作方参数...");
        
        PartnerPayAgentCheckCodeResponse response = new PartnerPayAgentCheckCodeResponse();
        response.setPartnerId(data.getPartnerId().name());
        response.setMessage(requestResult.isSuccess() ? requestResult.getObject().getErrorMsg() : requestResult.getMessage());
        response.setPartnerOrderId(data.getPartnerOrderId());
        response.setOrderId(orderId);
        response.setAmount(applyVo.getAmount().toString());
        response.setChannelId(applyVo.getChannel());
        response.setChannelNoticeTime(DateUtil.timeToStr(new Date(), DateUtil.DATE_FORMAT_PATTEN));
        response.setFee(requestResult.getObject().getFee() == null ? "" : requestResult.getObject().getFee().toString());
        
        //如果订单处理不成功，不过第三方结果怎么样，都通知接入方为处理中的状态，保证支付网关和接入方的状态一致
        response.setAgentStatus(callbackResult.isSuccess() ? requestResult.getObject().getSignState().name() : AgentStatus.RECEIVE.name());
        
        String hmac = partnerFacade.genSignature(response);
        response.setHmac(hmac);
        
        logger.info("同步回调参数， response:{}", response);
        
        //回调订单处理不成功，不进行异步回调
        if(!callbackResult.isSuccess() || requestResult.getObject().getSignState() == AgentStatus.RECEIVE){
            
            logger.info("不是最终结果，不进行异步回调通知, orderId:{}, partnerOrderID:{}", orderId, data.getPartnerOrderId());
            
            return new Result<>(ResultCode.SUCCESS, response);
        }
        
        //生成回调的form表单(约定和P2P通讯格式)
        RequestFormData form = partnerFacade.getPayAgentAuthCallbackForm(applyVo.getChannel(), orderId, data.getNotifyUrl(), applyVo.getAmount(), data.getPartnerId().name(), data.getPartnerOrderId(), requestResult);
        
        //写入通知队列
        String paramUrl = genUrlParamFormat(form.getParams());
        notifyFacade.addQueue(orderId, data.getPartnerId() , data.getPartnerOrderId(), paramUrl, data.getNotifyUrl(), new Date(), 1);
        
        logger.info("代收鉴权, 写入通知队列成功, orderId:{}, partnerOrderID:{}", orderId, data.getPartnerOrderId());

        return new Result<>(ResultCode.SUCCESS, response);
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

    @Override
    //@Transactional(propagation = Propagation.REQUIRED)  不加事务，使得 记录请求、回调更新请求在各自的事务当中，后面异常不会导致前面的回滚
    public Result<PartnerPayAgentChargeResponse> charge(
            PartnerPayAgentChargeRequest params) {
        
        logger.info("代扣参数校验开始...， params：{}", params);
        
        //风控
        RiskParam riskParam=new RiskParam();
        riskParam.setIp(params.getIp());
        riskParam.setReferer("");
        if(StringUtils.isBlank(params.getTimeStamp())){
            return new Result<>(ResultCode.FAILURE,null,"时间戳不能为空");
        }
        long timeStamp=0L;
        try{
            timeStamp=Long.parseLong(params.getTimeStamp());
        }catch(NumberFormatException e){
            logger.error("时间戳不为长整形,timeStamp:"+params.getTimeStamp());
            return new Result<>(ResultCode.FAILURE,null,"时间戳格式错误"); 
        }
        riskParam.setTimeStamp(timeStamp);
        Result<String> r = payAgentRiskHandlerChain.handle(riskParam);
        if(!r.isSuccess()){
            return new Result<>(ResultCode.FAILURE,  null, r.getMessage());
        }
        //1.签名和参数格式验证(约定和P2P通讯格式)
        PartnerPayAgentChargeData data = null;
        try{
            data = partnerFacade.payAgentCharge(params);
        }catch(ParamException e){
            logger.error("参数错误,{}",e.getMessage());
            return new Result<>(ResultCode.FAILURE, null, "参数错误,"+e.getMessage());
        }catch(CheckSignatureException e){
            logger.error("签名验证失败");
            return new Result<>(ResultCode.FAILURE,null,"签名验证失败");
        }catch (ServiceException e) {
            logger.error("代扣鉴权获取验证码失败,失败原因:{}",e.getMessage());
            return new Result<>(ResultCode.FAILURE,null,e.getMessage());
        }
        
        logger.info("参数校验完成，更新订单状态...");
        
        //转换成渠道的银行名称
        String channelBankId = bankConfigMapper.getChannelBankId(data.getChannel().name(), data.getBankId().getBankId());
        if(StringUtils.isBlank(channelBankId)) {
            logger.info("找不到对应的银行编号, partnerOrderId:{}, channel:{}, bankId:{}", data.getPartnerOrderId(), data.getChannel(), data.getBankId());
            return new Result<>(ResultCode.FAILURE, null, "支付网关找不到对应渠道的银行编号");
        }
        
        //支付网关的订单号和P2P一致，方便查找问题
        String orderId = data.getPartnerOrderId();
        
        //记录请求的订单号
        Result<String> orderRequestResult = orderFacade.sendPayAgentChargeRequest(orderId, data);
        logger.info("代收写入请求订单表, result:{}", orderRequestResult);

        //写入请求表失败
        if(!orderRequestResult.isSuccess()) {
            logger.info("订单请求表写入失败，{}, 直接返回失败结果, partnerOrderId:{}", orderRequestResult.getMessage(), data.getPartnerOrderId());
            return new Result<>(ResultCode.FAILURE, null, orderRequestResult.getMessage());
        } 
        
        logger.info("写入请求表成功, 发送第三方请求...");
        
        BankInfoVO bankInfoVO = new BankInfoVO();
        bankInfoVO.setAccountType(data.getAccountType() == BankAccountType.PUBLIC ? 1 : 2);
        bankInfoVO.setBankAccount(data.getCardNo());
        bankInfoVO.setBankName(channelBankId);
        bankInfoVO.setUserName(data.getRealName());
        bankInfoVO.setIdCard(data.getIdNo());
        bankInfoVO.setMobile(data.getCellphone());

        Result<UmbpayAgentVO> requestResult = umbpayAgentFacade.userAgent(bankInfoVO , data.getAmount());
        logger.info("第三方请求结果, requestResult:{}", requestResult);

        Result<String> callbackResult = orderFacade.PayAgentChargeCallback(requestResult.isSuccess(), data.getChannel(), orderId, requestResult.getObject());
        logger.info("代扣回调订单处理orderId:{}, result:{}", orderId, callbackResult);
        
        logger.info("封装返回合作方参数...");
        
        PartnerPayAgentChargeResponse response = new PartnerPayAgentChargeResponse();
        response.setPartnerId(data.getPartnerId().name());
        response.setMessage(requestResult.isSuccess() ? requestResult.getObject().getErrorMsg() : requestResult.getMessage());
        response.setPartnerOrderId(data.getPartnerOrderId());
        response.setOrderId(orderId);
        response.setAmount(data.getAmount().toString());
        response.setChannelId(data.getChannel().name());
        response.setChannelOrderId(requestResult.getObject().getBussflowno());
        response.setChannelNoticeTime(DateUtil.timeToStr(new Date(), DateUtil.DATE_FORMAT_PATTEN));
        response.setFee(requestResult.getObject().getFee() == null ? "" : requestResult.getObject().getFee().toString());
        
        //如果订单处理不成功，不过第三方结果怎么样，都通知接入方为处理中的状态，保证支付网关和接入方的状态一致
        response.setAgentStatus(callbackResult.isSuccess() ? requestResult.getObject().getSignState().name() : AgentStatus.RECEIVE.name());
        
        String hmac = partnerFacade.genSignature(response);
        response.setHmac(hmac);
        
        logger.info("同步回调参数， response:{}", response);
        
        //回调订单处理不成功，不进行异步回调
        if(!callbackResult.isSuccess() || requestResult.getObject().getSignState() == AgentStatus.RECEIVE){
            
            logger.info("不是最终结果，不进行异步回调通知, orderId:{}, partnerOrderID:{}", orderId, data.getPartnerOrderId());
            
            return new Result<>(ResultCode.SUCCESS, response);
        }
        
        //生成回调的form表单(约定和P2P通讯格式)
        RequestFormData form = partnerFacade.getPayAgentChargeCallbackForm(data.getChannel(), orderId, data.getNotifyUrl(), data.getAmount(), requestResult.getObject().getBussflowno(), data.getPartnerId().name(), data.getPartnerOrderId(), requestResult);
        
        //写入通知队列
        String paramUrl = genUrlParamFormat(form.getParams());
        notifyFacade.addQueue(orderId, data.getPartnerId() , data.getPartnerOrderId(), paramUrl, data.getNotifyUrl(), new Date(), 1);
        
        logger.info("代收, 写入通知队列成功, orderId:{}, partnerOrderID:{}", orderId, data.getPartnerOrderId());

        return new Result<>(ResultCode.SUCCESS, response);
    }
    
}
