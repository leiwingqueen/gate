package com.elend.gate.order.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.elend.gate.balance.constant.BalanceAccountConstant;
import com.elend.gate.balance.constant.BalanceType;
import com.elend.gate.balance.constant.SubSubject;
import com.elend.gate.balance.constant.Subject;
import com.elend.gate.balance.facade.BalanceFacade;
import com.elend.gate.channel.constant.BankIdConstant;
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.PartnerResultCode;
import com.elend.gate.channel.constant.PayType;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.constant.WithdrawStatus;
import com.elend.gate.channel.facade.vo.ChargeCallbackData;
import com.elend.gate.channel.facade.vo.PartnerPayAgentChargeData;
import com.elend.gate.channel.facade.vo.PartnerPayAgentCheckCodeData;
import com.elend.gate.channel.facade.vo.PartnerPayAgentGetCodeData;
import com.elend.gate.channel.facade.vo.PartnerWithdrawData;
import com.elend.gate.channel.facade.vo.WithdrawCallbackData;
import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.context.AccessContext;
import com.elend.gate.order.constant.NotifyStatus;
import com.elend.gate.order.constant.OperateType;
import com.elend.gate.order.constant.OrderCompleteStatus;
import com.elend.gate.order.constant.OrderStatus;
import com.elend.gate.order.constant.OrderType;
import com.elend.gate.order.constant.RequestSource;
import com.elend.gate.order.constant.RequestStatus;
import com.elend.gate.order.facade.vo.DepositRequest;
import com.elend.gate.order.mapper.PDepositRequestMapper;
import com.elend.gate.order.mapper.POrderMapper;
import com.elend.gate.order.mapper.POrderStatusRecrodMapper;
import com.elend.gate.order.mapper.PPayAgentApplyRequestMapper;
import com.elend.gate.order.mapper.PPayAgentAuthRequestMapper;
import com.elend.gate.order.mapper.PPayAgentChargeRequestMapper;
import com.elend.gate.order.mapper.PTransactionLogMapper;
import com.elend.gate.order.mapper.PWithdrawRequestMapper;
import com.elend.gate.order.model.PDepositRequestPO;
import com.elend.gate.order.model.POrderPO;
import com.elend.gate.order.model.PPayAgentApplyRequestPO;
import com.elend.gate.order.model.PPayAgentAuthRequestPO;
import com.elend.gate.order.model.PPayAgentChargeRequestPO;
import com.elend.gate.order.model.PTransactionLogPO;
import com.elend.gate.order.model.PWithdrawRequestPO;
import com.elend.gate.order.service.OrderService;
import com.elend.gate.order.vo.PPayAgentApplyRequestVO;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.Result;
import com.elend.p2p.ServiceException;
import com.elend.p2p.constant.ResultCode;
import com.elend.p2p.gson.JSONUtils;
import com.elend.p2p.util.DateUtil;
import com.elend.p2p.util.OrderIdHelper;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;
import com.elend.pay.agent.sdk.constant.AgentStatus;
import com.elend.pay.agent.sdk.constant.PayAgentChannel;
import com.elend.pay.agent.sdk.vo.UmbpayAccreditVO;
import com.elend.pay.agent.sdk.vo.UmbpayAgentVO;
import com.elend.pay.agent.sdk.vo.UmbpaySignWhiteListOneVO;
import com.google.gson.reflect.TypeToken;

@Service
public class OrderServiceImpl implements OrderService{
    private final static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private PDepositRequestMapper requestMapper;
    @Autowired
    private POrderMapper orderMapper;
    @Autowired
    private PTransactionLogMapper tLogMapper;
    
    @Autowired
    private PWithdrawRequestMapper pWithdrawRequestMapper;
    
    @Autowired
    private POrderStatusRecrodMapper pOrderStatusRecrodMapper;
    
    @Autowired
    private BalanceFacade balanceFacade;
    
    @Autowired
    private PPayAgentChargeRequestMapper pPayAgentChargeRequestMapper;
    
    @Autowired
    private PPayAgentApplyRequestMapper pPayAgentApplyRequestMapper;
    
    @Autowired
    private PPayAgentAuthRequestMapper pPayAgentAuthRequestMapper;
    
    private DesPropertiesEncoder encoder = new DesPropertiesEncoder();
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<String> sendChargeRequest(ChannelIdConstant channelId,PartnerConstant partnerId,
            String partnerOrderId, BigDecimal amount,RequestFormData form,String ip,RequestSource source,
            String callbackUrl,String notifyUrl,String orderId, PayType payType) {
        PDepositRequestPO request=requestMapper.getByPartnerTradeNo(partnerId.name(), partnerOrderId);
        if(request!=null){
            return new Result<String>(ResultCode.FAILURE,null,"订单重复");
        }
        //String orderId=OrderIdHelper.newOrderId();
        Date now=new Date();
        PDepositRequestPO po=new PDepositRequestPO();
        po.setOrderId(orderId);
        po.setPartnerId(partnerId.name());
        po.setPartnerTradeNo(partnerOrderId);
        po.setPayChannel(channelId.name());
        po.setChannelTradeNo("");
        po.setRequestUrl(form.getRequestUrl());
        po.setPayType(payType.getType());
        
        //敏感信息进行加密
        String paramsStr=JSONUtils.toJson(form.getParams(), false);
        
        if(channelId == ChannelIdConstant.LIANLIAN_MOBILE || channelId == ChannelIdConstant.LIANLIAN_WAP) {
            HashMap<String, String> paramMap = null;
            switch (channelId) {
                case LIANLIAN_MOBILE: {
                    paramMap = new HashMap<>();
                    paramMap.putAll(form.getParams());
                    if(paramMap != null) {
                        HashMap<String, String> riskMap = JSONUtils.fromJson(paramMap.get("risk_item"), new TypeToken<HashMap<String, String>>(){});
                        paramMap.put("id_no", encoder.encode(paramMap.get("id_no")));
                        paramMap.put("acct_name", encoder.encode(paramMap.get("acct_name")));
                        if(StringUtils.isNotBlank(paramMap.get("card_no"))) {
                            paramMap.put("card_no", encoder.encode(paramMap.get("card_no")));
                        }
                        
                        riskMap.put("user_info_id_no", encoder.encode(riskMap.get("user_info_id_no")));
                        riskMap.put("user_info_full_name", encoder.encode(riskMap.get("user_info_full_name")));
                        riskMap.put("user_info_bind_phone", encoder.encode(riskMap.get("user_info_bind_phone")));
                        
                        paramMap.put("risk_item", JSONUtils.toJson(riskMap, false));
                        paramsStr=JSONUtils.toJson(paramMap, false);
                    }
                } break;
                
                case LIANLIAN_WAP: {
                    paramMap = JSONUtils.fromJson(form.getParams().get("req_data"), new TypeToken<HashMap<String, String>>(){});
                    if(paramMap != null) {
                        HashMap<String, String> riskMap = JSONUtils.fromJson(paramMap.get("risk_item"), new TypeToken<HashMap<String, String>>(){});
                        paramMap.put("id_no", encoder.encode(paramMap.get("id_no")));
                        paramMap.put("acct_name", encoder.encode(paramMap.get("acct_name")));
                        if(StringUtils.isNotBlank(paramMap.get("card_no"))) {
                            paramMap.put("card_no", encoder.encode(paramMap.get("card_no")));
                        }
                        
                        riskMap.put("user_info_id_no", encoder.encode(riskMap.get("user_info_id_no")));
                        riskMap.put("user_info_full_name", encoder.encode(riskMap.get("user_info_full_name")));
                        riskMap.put("user_info_bind_phone", encoder.encode(riskMap.get("user_info_bind_phone")));
                        
                        paramMap.put("risk_item", JSONUtils.toJson(riskMap, false));
                        paramsStr=JSONUtils.toJson(paramMap, false);
                    }
                } break;
    
                default:
                    break;
            }
        }
            

        po.setParamValue(paramsStr.length()>1000?paramsStr.substring(0,1000):paramsStr);
        po.setResult("");
        po.setCreateTime(now);
        po.setCallBackResult("");
        po.setAmount(amount);
        po.setStatus(RequestStatus.REQUEST.getValue());
        po.setIp(ip);
        po.setSource(source.getSource());
        po.setCallbackUrl(callbackUrl);
        po.setNotifyUrl(notifyUrl);
        int affectRow=requestMapper.insert(po);
        if(affectRow<=0){
            logger.error("写入充值请求表失败,PDepositRequestPO:{}",po.getOrderId());
            return new Result<String>(ResultCode.FAILURE, orderId,"写入充值请求表失败");
        }
        logger.info("写入充值请求表成功,PDepositRequestPO:{}",po.getOrderId());
        return new Result<String>(ResultCode.SUCCESS, orderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<String> chargeCallback(String resultCode,
            ChannelIdConstant channelId, ChargeCallbackData callbackData,
            Map<String,String> params)throws ServiceException {
        logger.info("开始充值回调,resultCode:{},channelId:{},callbackData:{},params:{}",resultCode,
                    channelId.name(),callbackData.toString(), encoder.encode(JSONUtils.toJson(params,false)));
        
        //非成功或者失败，直接返回失败，不回调P2P
        if(!PartnerResultCode.FAILURE.equals(resultCode) && !PartnerResultCode.SUCCESS.equals(resultCode)){
            return new Result<String>(ResultCode.FAILURE, callbackData.getOrderId());
        }
        
        String orderId=callbackData.getOrderId();
        PDepositRequestPO requestPO = requestMapper.getByOrderId(orderId);
        if (requestPO == null) {
            return new Result<String>(ResultCode.FAILURE, orderId,"找不到对应的订单号");
        }
        // 防止重复刷
        if (requestPO.getStatus() != RequestStatus.REQUEST.getValue()) {
            logger.info("{} 充值回调 : requestPO is not PROCESSING:{}, now return，now status", orderId, requestPO.getStatus(), requestPO);
            if(requestPO.getStatus() == RequestStatus.DONE.getValue()) {
                return new Result<String>(ResultCode.SUCCESS, orderId);
            } else {
                //失败，不回调P2P
                return new Result<String>(ResultCode.FAILURE, orderId);
            }
        }
        Date now=new Date();
        //回调参数
        String jsonStr=JSONUtils.toJson(params,false);
        
        //敏感信息进行加密
        if(channelId == ChannelIdConstant.LIANLIAN_MOBILE || channelId == ChannelIdConstant.LIANLIAN_WAP) {
            HashMap<String, String> paramMap = null;
            switch (channelId) {
                case LIANLIAN_MOBILE: {
                    paramMap = new HashMap<>();
                    paramMap.putAll(params);
                    paramMap.put("id_no", encoder.encode(paramMap.get("id_no")));
                    paramMap.put("acct_name", encoder.encode(paramMap.get("acct_name")));
                    jsonStr=JSONUtils.toJson(paramMap, false);
                } break;
                
                case LIANLIAN_WAP: {
                    paramMap = JSONUtils.fromJson(params.get("res_data"), new TypeToken<HashMap<String, String>>(){});
                    if(paramMap != null) {
                        paramMap.put("id_no", encoder.encode(paramMap.get("id_no")));
                        paramMap.put("acct_name", encoder.encode(paramMap.get("acct_name")));
                        jsonStr=JSONUtils.toJson(paramMap, false);
                    }
                } break;
    
                default:
                    break;
            }
        }
        
        String callbackStr=jsonStr.length()>255?jsonStr.substring(0,255):jsonStr;
        //返回结果失败直接返回
        if(PartnerResultCode.FAILURE.equals(resultCode)){ //失败
            int affectRow =requestMapper.updateStatus(orderId,
                                       RequestStatus.REQUEST.getValue(),
                                       RequestStatus.FAIL.getValue(),
                                       now,
                                       callbackStr,callbackData.getChannelOrderId(), callbackData.getPayType() == null ? 0 : callbackData.getPayType().getType());
            if(affectRow<=0){
                logger.info("{} 充值回调 : requestPO is not PROCESSING,now return",
                            orderId, requestPO);
            }
            return new Result<String>(ResultCode.FAILURE, orderId);
        } else if(!PartnerResultCode.SUCCESS.equals(resultCode)) { //非成功直接返回失败
            return new Result<String>(ResultCode.FAILURE, orderId);
        }
        // 更新请求表
        int affectRow = requestMapper.updateStatus(orderId,
                                                       RequestStatus.REQUEST.getValue(),
                                                       RequestStatus.DONE.getValue(),
                                                       now,callbackStr,callbackData.getChannelOrderId(), callbackData.getPayType() == null ? 0 : callbackData.getPayType().getType());
        if (affectRow <= 0) {
            logger.info("{} 充值回调 : requestPO is not PROCESSING,now return",
                        orderId, requestPO);
            return new Result<String>(ResultCode.SUCCESS, orderId);
        }
        
        //渠道账本充值余额和流水
        Result<String>  r = balanceFacade.charge(orderId, channelId.getBalanceUserId(), requestPO.getAmount(), BalanceType.E_COIN);
        if(!r.isSuccess()) {
            logger.info("更新渠道账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
            throw new ServiceException("更新渠道账本余额失败:{}", r.getMessage());
        }
        this.saveTransactionLog(orderId, 
                                OrderType.CHARGE, 
                                requestPO.getAmount(), 
                                now, 
                                "充值:" + requestPO.getAmount(), 
                                OperateType.IN, 
                                requestPO.getPartnerId(), 
                                requestPO.getPartnerTradeNo(), 
                                channelId.name(), 
                                callbackData.getChannelOrderId(), 
                                Subject.CHARGE, 
                                SubSubject.CHARGE, 
                                callbackData.getFee(), 
                                channelId.getBalanceUserId(), 
                                0);
        
        //总账本充值余额和流水
        r = balanceFacade.charge(orderId, BalanceAccountConstant.TOTAL_ACCOUNT, requestPO.getAmount(), BalanceType.E_COIN);
        if(!r.isSuccess()) {
            logger.info("更新总账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
            throw new ServiceException("更新总账本余额失败:{}", r.getMessage());
        }
        this.saveTransactionLog(orderId, 
                                OrderType.CHARGE, 
                                requestPO.getAmount(), 
                                now, 
                                "充值:" + requestPO.getAmount(), 
                                OperateType.IN, 
                                requestPO.getPartnerId(), 
                                requestPO.getPartnerTradeNo(), 
                                channelId.name(), 
                                callbackData.getChannelOrderId(), 
                                Subject.CHARGE, 
                                SubSubject.CHARGE, 
                                callbackData.getFee(), 
                                BalanceAccountConstant.TOTAL_ACCOUNT, 
                                0);
        
        //渠道账本充值手续费余额和流水
        r = balanceFacade.chargeFee(orderId, channelId.getBalanceUserId(), callbackData.getFee(), BalanceType.E_COIN);
        if(!r.isSuccess()) {
            logger.info("更新渠道账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
            throw new ServiceException("更新渠道账本余额失败:{}", r.getMessage());
        }
        this.saveTransactionLog(orderId, 
                                OrderType.CHARGE_FEE, 
                                callbackData.getFee(), 
                                now, 
                                "充值手续费:" + callbackData.getFee(), 
                                OperateType.OUT, 
                                requestPO.getPartnerId(), 
                                requestPO.getPartnerTradeNo(), 
                                channelId.name(), 
                                callbackData.getChannelOrderId(), 
                                Subject.CHARGE_FEE, 
                                SubSubject.CHARGE_FEE, 
                                BigDecimal.ZERO, 
                                channelId.getBalanceUserId(), 
                                0);
        
        //总账本充值手续费余额和流水
        r = balanceFacade.chargeFee(orderId, BalanceAccountConstant.TOTAL_ACCOUNT, callbackData.getFee(), BalanceType.E_COIN);
        if(!r.isSuccess()) {
            logger.info("更新总账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
            throw new ServiceException("更新总账本余额失败:{}", r.getMessage());
        }
        this.saveTransactionLog(orderId, 
                                OrderType.CHARGE_FEE, 
                                callbackData.getFee(), 
                                now, 
                                "充值手续费:" + callbackData.getFee(), 
                                OperateType.OUT, 
                                requestPO.getPartnerId(), 
                                requestPO.getPartnerTradeNo(), 
                                channelId.name(), 
                                callbackData.getChannelOrderId(), 
                                Subject.CHARGE_FEE, 
                                SubSubject.CHARGE_FEE, 
                                BigDecimal.ZERO, 
                                BalanceAccountConstant.TOTAL_ACCOUNT, 
                                0);
        
        //写入订单表
        POrderPO order=new POrderPO();
        order.setOrderId(orderId);
        order.setOrderType(OrderType.CHARGE.getType());
        order.setAmount(requestPO.getAmount());
        order.setPayChannel(channelId.name());
        order.setChannelTradeNo(callbackData.getChannelOrderId());
        order.setPartnerId(requestPO.getPartnerId());
        order.setPartnerTradeNo(requestPO.getPartnerTradeNo());
        order.setBankId(callbackData.getBankId());
        order.setCreateTime(now);
        order.setChRefundTime(callbackData.getNoticeTime());
        //order.setFinishTime(null); finishTime这时候为空，通知接入方后才设置为成功
        order.setRefundAmount(callbackData.getAmount());
        order.setGmtPayment(callbackData.getPayTime());
        order.setUpdateTime(now);
        order.setStatus(OrderStatus.PAYED.getStatus());
        order.setIp(AccessContext.getAccessIp());
        order.setSubject(Subject.CHARGE.getVal());
        order.setSubSubject(SubSubject.CHARGE.getVal());
        order.setRefrenceId("");
        order.setNotifyStatus(NotifyStatus.REQUEST.getStatus());//通知中
        order.setFee(callbackData.getFee());//手续费
        affectRow=orderMapper.insert(order);
        if (affectRow <= 0) {
            logger.info("写入充值订单表失败,order:{}",
                        order);
            throw new ServiceException("写入充值订单表失败");
        }
        //写入流水表
        /*PTransactionLogPO tLog=new PTransactionLogPO();
        tLog.setOrderId(orderId);
        tLog.setOrderType(OrderType.CHARGE.getType());
        tLog.setAmount(callbackData.getAmount());
        tLog.setLogTime(now);
        tLog.setRemarks("充值:"+callbackData.getAmount());
        tLog.setOperateType(OperateType.IN.getType());
        tLog.setPartnerId(requestPO.getPartnerId());
        tLog.setPartnerTradeNo(requestPO.getPartnerTradeNo());
        tLog.setPayChannel(channelId.name());
        tLog.setChannelTradeNo(callbackData.getChannelOrderId());
        tLog.setSubject(Subject.CHARGE.getVal());
        tLog.setSubSubject(SubSubject.CHARGE.getVal());
        tLog.setFee(callbackData.getFee());
        affectRow=tLogMapper.insert(tLog);
        if (affectRow <= 0) {
            logger.info("写入充值流水表失败,PTransactionLogPO:{}",
                        tLog);
            throw new ServiceException("写入充值流水表失败");
        }*/
        logger.info("充值回调成功,resultCode:{},channelId:{},callbackData:{}",resultCode,channelId.name(),callbackData.toString());
        return new Result<String>(ResultCode.SUCCESS, orderId);
    }

    @Override
    public DepositRequest getChargeRequest(String orderId) {
        PDepositRequestPO po=requestMapper.getByOrderId(orderId);
        if(po==null)return null;
        DepositRequest request=new DepositRequest();
        request.setAmount(po.getAmount());
        request.setCallbackUrl(po.getCallbackUrl());
        request.setNotifyUrl(po.getNotifyUrl());
        request.setOrderId(po.getOrderId());
        request.setPartner(PartnerConstant.from(po.getPartnerId()));
        request.setPartnerOrderId(po.getPartnerTradeNo());
        request.setPayChannel(ChannelIdConstant.from(po.getPayChannel()));
        request.setPayType(PayType.from(po.getPayType()));
        return request;
    }

    @Override
    public Result<String> updateNotifyStatus(String orderId,
            NotifyStatus status) {
        int affectRow=orderMapper.updateNoticeStatus(orderId,status.getStatus() ,new Date());
        if(affectRow<=0){
            logger.error("更新通知状态失败.orderId:{},status:{}",
                        orderId,status);
            return new Result<String>(ResultCode.FAILURE, null,"更新通知状态失败");
        }
        return new Result<String>(ResultCode.SUCCESS, orderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<String> sendWithdrawRequest(PartnerWithdrawData data, String orderId) {
        //写入请求表
        PWithdrawRequestPO po = pWithdrawRequestMapper.getByPartnerOrderId(data.getPartnerId().name(), data.getPartnerOrderId());
        if(po != null){
            logger.error("接入方订单重复, partnerId:{}， partnerOrderId:{}", data.getPartnerId(), data.getPartnerOrderId());
            return new Result<String>(ResultCode.FAILURE,null, "接入方订单重复");
        }
        Date now = new Date();
        po = new PWithdrawRequestPO();
        po.setOrderId(orderId);
        po.setPartnerId(data.getPartnerId().name());
        po.setPartnerOrderId(data.getPartnerOrderId());
        po.setChannel(data.getChannel().name());
        po.setRequestUrl(""); //发送第三方请求后更新
        
        PartnerWithdrawData newData = new PartnerWithdrawData();
        try {
            BeanUtils.copyProperties(newData, data);
        } catch (Exception e) {
        }
        DesPropertiesEncoder encoder = new DesPropertiesEncoder();
        //对敏感信息进行加密
        newData.setUserName(encoder.encode(newData.getUserName()));
        newData.setBankAccount(encoder.encode(newData.getBankAccount()));
        po.setParamValue(JSON.toJSONString(newData, false));
        
        po.setCreateTime(now);
        po.setAmount(data.getAmount());
        po.setStatus(RequestStatus.REQUEST.getValue());
        po.setIp(data.getIp());
        po.setSource(1); //都是web
        po.setNotifyUrl(data.getNotifyUrl());

        int affectRow = pWithdrawRequestMapper.insert(po);
        if(affectRow <= 0){
            logger.error("写入提现请求表失败, PWithdrawRequestPO:{}",po);
            return new Result<String>(ResultCode.FAILURE, orderId, "写入提现请求表失败");
        }
        
        //更新订单状态表状态
        pOrderStatusRecrodMapper.updateStatus(orderId, OrderCompleteStatus.APPLYING.getStatus(), new Date());
        
        logger.info("写入提现请求表成功, PWithdrawRequestPO:{}", po);
        return new Result<String>(ResultCode.SUCCESS, orderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<String> withdrawCallback(boolean success,
                                           WithdrawChannel channel, WithdrawCallbackData backData) {
        logger.info("提现发送请求后处理,success:{},channelId:{}, backData:{}",success, channel.name(), backData);
        
        String orderId = backData.getOrderId();
        Date callbackTime = backData.getCallbackTime();
        String channelOrderId = backData.getChannelOrderId();
        String requestUrl = backData.getRequestUrl();
        
        String result = backData.getResultStr();
        String callbackResult = backData.getCallbackStr();
        
        //截取1024字节
        if(StringUtils.isNotBlank(result)) {
            result = result.length() > 1024 ? result.substring(0,1024) : result;
        }
        if(StringUtils.isNotBlank(callbackResult)) {
            callbackResult = callbackResult.length() > 1024 ? callbackResult.substring(0,1024) : callbackResult;
        }
        if(backData.getWithdrawStatus()==WithdrawStatus.APPLYING){
            
            //同步请求返回时，请求中更新渠道订单号（兼容民生的订单号记录为平台订单号加上民生商户号前缀）
            if(StringUtils.isNotBlank(channelOrderId)) {
                int row = pWithdrawRequestMapper.updateChannelOrderId(orderId, channelOrderId);
                logger.info("请求中状态，更新渠道订单号, orderId:{}, channelOrderID:{}, row:{}", orderId, channelOrderId, row);
            }
            
            logger.info("订单返回状态：APPLYING,orderId:{},now return", orderId);
            return new Result<String>(ResultCode.FAILURE, orderId,"订单返回状态为进行中"); 
        }
        PWithdrawRequestPO requestPO = pWithdrawRequestMapper.getByOrderId(orderId);
        if (requestPO == null) {
            logger.info("找不到对应的请求订单信息：orderId:{}", orderId);
            return new Result<String>(ResultCode.FAILURE, orderId,"找不到对应的请求订单号");
        }
        // 防止重复刷
        if (requestPO.getStatus() != RequestStatus.REQUEST.getValue()) {
            logger.info("{} 提现回调 : requestPO is not PROCESSING,now return", orderId, requestPO);
            return new Result<String>(ResultCode.SUCCESS, orderId);
        }
        
        int affectRow = pWithdrawRequestMapper.update(orderId,
                                                      RequestStatus.REQUEST.getValue(),
                                                      RequestStatus.from(backData.getWithdrawStatus()).getValue(),
                                                      callbackTime, result,
                                                      callbackResult,
                                                      channelOrderId,
                                                      requestUrl);
        if (affectRow <= 0) {
            logger.info("{} 提现回调 : requestPO is not PROCESSING, now return", orderId, requestPO);
            return new Result<String>(ResultCode.SUCCESS, orderId);
        }
        Date now = new Date();
        //处理中定义成失败，不通知接入方
        if(backData.getWithdrawStatus()==WithdrawStatus.FAILURE) {
            //更新订单状态表状态
            pOrderStatusRecrodMapper.updateStatus(orderId, OrderCompleteStatus.FAILURE.getStatus(), now);
            return new Result<String>(ResultCode.SUCCESS, orderId);
        }
        //更新订单状态表状态
        pOrderStatusRecrodMapper.updateStatus(orderId, OrderCompleteStatus.SUCCESS.getStatus(), now);
        
        //渠道账本提现扣款和流水
        Result<String> r = balanceFacade.withdraw(orderId, channel.getBalanceUserId(), requestPO.getAmount(), BalanceType.E_COIN);
        if(!r.isSuccess()) {
            logger.info("更新渠道账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
            throw new ServiceException("更新渠道账本余额失败:{}", r.getMessage());
        }
        this.saveTransactionLog(orderId, 
                                OrderType.WITHDRAW, 
                                requestPO.getAmount(), 
                                now, 
                                "提现:" + requestPO.getAmount(), 
                                OperateType.OUT, 
                                requestPO.getPartnerId(), 
                                requestPO.getPartnerOrderId(), 
                                channel.name(), 
                                channelOrderId == null? "" : channelOrderId, 
                                Subject.WITHDRAW, 
                                SubSubject.WITHDRAW, 
                                backData.getFee(), 
                                channel.getBalanceUserId(), 
                                0);
        
        //总账本提现扣款和流水
        r = balanceFacade.withdraw(orderId, BalanceAccountConstant.TOTAL_ACCOUNT, requestPO.getAmount(), BalanceType.E_COIN);
        if(!r.isSuccess()) {
            logger.info("更新总账账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
            throw new ServiceException("更新总账账本余额失败:{}", r.getMessage());
        }
        this.saveTransactionLog(orderId, 
                                OrderType.WITHDRAW, 
                                requestPO.getAmount(), 
                                now, 
                                "提现:" + requestPO.getAmount(), 
                                OperateType.OUT, 
                                requestPO.getPartnerId(), 
                                requestPO.getPartnerOrderId(), 
                                channel.name(), 
                                channelOrderId == null? "" : channelOrderId, 
                                Subject.WITHDRAW, 
                                SubSubject.WITHDRAW, 
                                backData.getFee(), 
                                BalanceAccountConstant.TOTAL_ACCOUNT, 
                                0);
        
        //渠道账本提现手续费扣款和流水
        r = balanceFacade.withdrawFee(orderId, channel.getBalanceUserId(), backData.getFee(), BalanceType.E_COIN);
        if(!r.isSuccess()) {
            logger.info("更新渠道账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
            throw new ServiceException("更新渠道账本余额失败:{}", r.getMessage());
        }
        this.saveTransactionLog(orderId, 
                                OrderType.WITHDRAW_FEE, 
                                backData.getFee(), 
                                now, 
                                "提现手续费:" + backData.getFee(), 
                                OperateType.OUT, 
                                requestPO.getPartnerId(), 
                                requestPO.getPartnerOrderId(), 
                                channel.name(), 
                                channelOrderId == null? "" : channelOrderId, 
                                Subject.WITHDRAW_FEE, 
                                SubSubject.WITHDRAW_FEE, 
                                BigDecimal.ZERO, 
                                channel.getBalanceUserId(), 
                                0);
        
        //总账本提现手续费扣款和流水
        r = balanceFacade.withdrawFee(orderId, BalanceAccountConstant.TOTAL_ACCOUNT, backData.getFee(), BalanceType.E_COIN);
        if(!r.isSuccess()) {
            logger.info("更新总账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
            throw new ServiceException("更新总账本余额失败:{}", r.getMessage());
        }
        this.saveTransactionLog(orderId, 
                                OrderType.WITHDRAW_FEE, 
                                backData.getFee(), 
                                now, 
                                "提现手续费:" + backData.getFee(), 
                                OperateType.OUT, 
                                requestPO.getPartnerId(), 
                                requestPO.getPartnerOrderId(), 
                                channel.name(), 
                                channelOrderId == null? "" : channelOrderId, 
                                Subject.WITHDRAW_FEE, 
                                SubSubject.WITHDRAW_FEE, 
                                BigDecimal.ZERO, 
                                BalanceAccountConstant.TOTAL_ACCOUNT, 
                                0);
        
        //写入订单表
        POrderPO order=new POrderPO();
        order.setOrderId(orderId);
        order.setOrderType(OrderType.WITHDRAW.getType());
        order.setAmount(requestPO.getAmount());
        order.setPayChannel(channel.name());
        order.setChannelTradeNo(channelOrderId == null? "" : channelOrderId);
        order.setPartnerId(requestPO.getPartnerId());
        order.setPartnerTradeNo(requestPO.getPartnerOrderId());
        order.setBankId("");
        order.setCreateTime(now);
        //order.setChRefundTime(null); //渠道通知完成时间
        //order.setFinishTime(null); finishTime这时候为空，通知接入方后才设置为成功
        order.setRefundAmount(backData.getAmount());
        //order.setGmtPayment(null);   //用户支付时间
        order.setUpdateTime(now);
        order.setStatus(OrderStatus.PAYED.getStatus());
        order.setIp(AccessContext.getAccessIp());
        order.setSubject(Subject.PAY_AGENT_AUTH.getVal());
        order.setSubSubject(SubSubject.PAY_AGENT_AUTH.getVal());
        //order.setRefrenceId(null); //冲正订单
        order.setNotifyStatus(NotifyStatus.REQUEST.getStatus());//通知中
        order.setFee(backData.getFee());//手续费
        affectRow = orderMapper.insert(order);
        if (affectRow <= 0) {
            logger.info("写入代收鉴权订单表失败,order:{}", order);
            throw new ServiceException("写入代收鉴权订单表失败");
        }
        //写入流水表
        /*PTransactionLogPO tLog = new PTransactionLogPO();
        tLog.setOrderId(orderId);
        tLog.setOrderType(OrderType.WITHDRAW.getType());
        tLog.setAmount(requestPO.getAmount());
        tLog.setLogTime(now);
        tLog.setRemarks("提现:" + requestPO.getAmount());
        tLog.setOperateType(OperateType.OUT.getType());
        tLog.setPartnerId(requestPO.getPartnerId());
        tLog.setPartnerTradeNo(requestPO.getPartnerOrderId());
        tLog.setPayChannel(channel.name());
        tLog.setChannelTradeNo(channelOrderId == null? "" : channelOrderId);
        tLog.setSubject(Subject.WITHDRAW.getVal());
        tLog.setSubSubject(SubSubject.WITHDRAW.getVal());
        tLog.setFee(backData.getFee());
        affectRow=tLogMapper.insert(tLog);
        if (affectRow <= 0) {
            logger.info("写入提现流水表失败,PTransactionLogPO:{}", tLog);
            throw new ServiceException("写入提现流水表失败");
        }*/
        
        logger.info("提现回调成功,success:{}, channelId:{}",success,channel.name());
        return new Result<String>(ResultCode.SUCCESS, orderId);
    }
    
    /**
     * 保存交易流水
     * @param orderId
     * @param orderType
     * @param amount
     * @param logTime
     * @param remark
     * @param operateType
     * @param partnerId
     * @param partnerTradeNo
     * @param payChannel
     * @param channelTradeNo
     * @param subject
     * @param subSubject
     * @param fee
     * @param userId
     * @param targetUserId
     */
    private void saveTransactionLog(
            String orderId, 
            OrderType orderType,
            BigDecimal amount,
            Date logTime, 
            String remark,
            OperateType operateType,
            String partnerId,
            String partnerTradeNo,
            String payChannel,
            String channelTradeNo,
            Subject subject,
            SubSubject subSubject,
            BigDecimal fee,
            long userId,
            long targetUserId) {
        
        Result<BigDecimal> result = balanceFacade.getUserBalance(userId, BalanceType.E_COIN);
        if (!result.isSuccess()) {
            logger.error("写入交易流水失败，orderId:{}, 获取账户余额失败:{}", orderId, result.getMessage());
            throw new ServiceException("写入交易流水失败:获取账户余额失败");
        }
        
        PTransactionLogPO tLog = new PTransactionLogPO();
        
        tLog.setOrderId(orderId);
        tLog.setOrderType(orderType.getType());
        tLog.setAmount(amount);
        tLog.setLogTime(logTime);
        tLog.setRemarks(remark);
        tLog.setOperateType(operateType.getType());
        tLog.setPartnerId(partnerId);
        tLog.setPartnerTradeNo(partnerTradeNo);
        tLog.setPayChannel(payChannel);
        tLog.setChannelTradeNo(channelTradeNo);
        tLog.setSubject(subject.getVal());
        tLog.setSubSubject(subSubject.getVal());
        tLog.setFee(fee);
        tLog.setUserId(userId);
        tLog.setTargetUserId(targetUserId);
        tLog.setUsableBalance(result.getObject());
        int affectRow = tLogMapper.insert(tLog);
        if (affectRow <= 0) {
            logger.info("写入交易流水表失败,PTransactionLogPO:{}", tLog);
            throw new ServiceException("写入提现流水表失败");
        }
    }

    @Override
    public PWithdrawRequestPO getWithdrawRequest(String orderId) {
        return pWithdrawRequestMapper.getByOrderId(orderId);
    }

    @Override
    public POrderPO getOrderBypartnerOrderId(PartnerConstant partnerId,
            String partnerOrderId) {
        return orderMapper.getOrderBypartnerOrderId(partnerId, partnerOrderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<String> transfer(long sourceUserId, long targetUserId,
            BigDecimal amount, Subject subject, SubSubject subSubject, String remark) {
        logger.info("转账, sourceUserId:{}, targetUserId:{}, amount:{}", sourceUserId, targetUserId, amount);
        
        //判断余额
        Result<BigDecimal> userBalance = balanceFacade.getUserBalance(sourceUserId, BalanceType.E_COIN);
        if(!userBalance.isSuccess()) {
            logger.error("获取账本余额失败, userId:{}, 原因:{}", sourceUserId, userBalance.getMessage());
            return new Result<>(ResultCode.FAILURE, null, "获取账本余额失败:" + userBalance.getMessage());
        }
        
        if(userBalance.getObject().compareTo(amount) < 0) {
            logger.error("余额不足, amount:{}, balance:{}", amount, userBalance.getObject());
            return new Result<>(ResultCode.FAILURE, null, "账本余额不足");
        }
        
        String orderId = OrderIdHelper.newOrderId();
        
        //资金流转
        Result<String> result = balanceFacade.transfer(orderId, sourceUserId, targetUserId, BalanceType.E_COIN, amount, subject, subSubject, remark);
        if(!result.isSuccess()) {
            logger.error("转账失败, 原因:{}", result.getMessage());
            return new Result<>(ResultCode.FAILURE, null, "转账失败:" + result.getMessage());
        }
        
        Date now = new Date();
        
        //记录资金流水
        //转出
        this.saveTransactionLog(orderId, 
                                OrderType.TRANSFER, 
                                amount, 
                                now, 
                                "转出:" + amount, 
                                OperateType.OUT, 
                                "", 
                                "", 
                                "", 
                                "", 
                                subject, 
                                subSubject, 
                                BigDecimal.ZERO, 
                                sourceUserId, 
                                targetUserId);
        //转入
        this.saveTransactionLog(orderId, 
                                OrderType.TRANSFER, 
                                amount, 
                                now, 
                                "转入:" + amount, 
                                OperateType.IN, 
                                "", 
                                "", 
                                "", 
                                "", 
                                subject, 
                                subSubject, 
                                BigDecimal.ZERO, 
                                targetUserId, 
                                sourceUserId);
        
        //写入订单表
        POrderPO order=new POrderPO();
        order.setOrderId(orderId);
        order.setOrderType(OrderType.TRANSFER.getType());
        order.setAmount(amount);
        order.setPayChannel("");
        order.setChannelTradeNo("");
        order.setPartnerId("");
        order.setPartnerTradeNo("");
        order.setBankId("");
        order.setCreateTime(now);
        order.setRefundAmount(amount);
        order.setUpdateTime(now);
        order.setStatus(OrderStatus.PAYED.getStatus());
        order.setIp(AccessContext.getAccessIp());
        order.setSubject(subject.getVal());
        order.setSubSubject(subSubject.getVal());
        order.setNotifyStatus(NotifyStatus.SUCCESS.getStatus());//通知完成
        order.setFee(BigDecimal.ZERO);//手续费
        int affectRow = orderMapper.insert(order);
        if (affectRow <= 0) {
            logger.info("写入转账订单表失败,order:{}", order);
            throw new ServiceException("写入转账订单表失败");
        }
        
        logger.info("转账成功,orderId:{}", orderId);
        return new Result<String>(ResultCode.SUCCESS, orderId);
    }

    @Override
    public void repairBalanceAndLog(Date startTime) {
        
        logger.info("startTime:{}", startTime);
        
        //查询指定时间内没有余额流水的订单
        int page = 1;
        while(true) {

            List<POrderPO> list =  orderMapper.listNoLog(startTime, 1000);
            
            logger.info("page:{}, size:{}", page ++, list.size());
            
            for(POrderPO o : list) {
                //每条处理做单独的事务，不会因为一条失败导致全部回滚
                addLog(o);
            }
            
            if(list.size() < 1000) {
                break;
            }
        }
        
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public void addLog(POrderPO orderPO) {
        
        logger.info("修复订单余额流水信息:{}", orderPO);
        
        //删除已经存在的交易流水(就得交易流水信息不全，且只有一条)
        tLogMapper.deleteByOrderId(orderPO.getOrderId());
        
        String orderId = orderPO.getOrderId();
        ChannelIdConstant channelId = ChannelIdConstant.from(orderPO.getPayChannel());
        BigDecimal amount = orderPO.getAmount();
        BigDecimal fee = orderPO.getFee();
        Date now = new Date();
        String partnerId = orderPO.getPartnerId();
        String partnerTradeNo = orderPO.getPartnerTradeNo();
        String channelOrderTradeNo = orderPO.getChannelTradeNo();
        
        //判断是充值还是体现
        if(OrderType.CHARGE.getType() == orderPO.getOrderType()) { //充值
          //渠道账本充值余额和流水
            Result<String>  r = balanceFacade.charge(orderId, channelId.getBalanceUserId(), amount, BalanceType.E_COIN);
            if(!r.isSuccess()) {
                logger.info("更新渠道账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
                throw new ServiceException("更新渠道账本余额失败:{}", r.getMessage());
            }
            this.saveTransactionLog(orderId, 
                                    OrderType.CHARGE, 
                                    amount, 
                                    now, 
                                    "充值:" + amount, 
                                    OperateType.IN, 
                                    partnerId, 
                                    partnerTradeNo, 
                                    channelId.name(), 
                                    channelOrderTradeNo, 
                                    Subject.CHARGE, 
                                    SubSubject.CHARGE, 
                                    fee, 
                                    channelId.getBalanceUserId(), 
                                    0);
            
            //总账本充值余额和流水
            r = balanceFacade.charge(orderId, BalanceAccountConstant.TOTAL_ACCOUNT, amount, BalanceType.E_COIN);
            if(!r.isSuccess()) {
                logger.info("更新总账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
                throw new ServiceException("更新总账本余额失败:{}", r.getMessage());
            }
            this.saveTransactionLog(orderId, 
                                    OrderType.CHARGE, 
                                    amount, 
                                    now, 
                                    "充值:" + amount, 
                                    OperateType.IN, 
                                    partnerId, 
                                    partnerTradeNo, 
                                    channelId.name(), 
                                    channelOrderTradeNo, 
                                    Subject.CHARGE, 
                                    SubSubject.CHARGE, 
                                    fee, 
                                    BalanceAccountConstant.TOTAL_ACCOUNT, 
                                    0);
            
            //渠道账本充值手续费余额和流水
            r = balanceFacade.chargeFee(orderId, channelId.getBalanceUserId(), fee, BalanceType.E_COIN);
            if(!r.isSuccess()) {
                logger.info("更新渠道账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
                throw new ServiceException("更新渠道账本余额失败:{}", r.getMessage());
            }
            this.saveTransactionLog(orderId, 
                                    OrderType.CHARGE_FEE, 
                                    fee, 
                                    now, 
                                    "充值手续费:" + fee, 
                                    OperateType.OUT, 
                                    partnerId, 
                                    partnerTradeNo, 
                                    channelId.name(), 
                                    channelOrderTradeNo, 
                                    Subject.CHARGE_FEE, 
                                    SubSubject.CHARGE_FEE, 
                                    BigDecimal.ZERO, 
                                    channelId.getBalanceUserId(), 
                                    0);
            
            //总账本充值手续费余额和流水
            r = balanceFacade.chargeFee(orderId, BalanceAccountConstant.TOTAL_ACCOUNT, fee, BalanceType.E_COIN);
            if(!r.isSuccess()) {
                logger.info("更新总账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
                throw new ServiceException("更新总账本余额失败:{}", r.getMessage());
            }
            this.saveTransactionLog(orderId, 
                                    OrderType.CHARGE_FEE, 
                                    fee, 
                                    now, 
                                    "充值手续费:" + fee, 
                                    OperateType.OUT, 
                                    partnerId, 
                                    partnerTradeNo, 
                                    channelId.name(), 
                                    channelOrderTradeNo, 
                                    Subject.CHARGE_FEE, 
                                    SubSubject.CHARGE_FEE, 
                                    BigDecimal.ZERO, 
                                    BalanceAccountConstant.TOTAL_ACCOUNT, 
                                    0);
        } else if(OrderType.WITHDRAW.getType() == orderPO.getOrderType()) { //提现
            
            //渠道账本提现扣款和流水
            Result<String> r = balanceFacade.withdraw(orderId, channelId.getBalanceUserId(), amount, BalanceType.E_COIN);
            if(!r.isSuccess()) {
                logger.info("更新渠道账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
                throw new ServiceException("更新渠道账本余额失败:{}", r.getMessage());
            }
            this.saveTransactionLog(orderId, 
                                    OrderType.WITHDRAW, 
                                    amount, 
                                    now, 
                                    "提现:" + amount, 
                                    OperateType.OUT, 
                                    partnerId, 
                                    partnerTradeNo, 
                                    channelId.name(), 
                                    channelOrderTradeNo, 
                                    Subject.WITHDRAW, 
                                    SubSubject.WITHDRAW, 
                                    fee, 
                                    channelId.getBalanceUserId(), 
                                    0);
            
            //总账本提现扣款和流水
            r = balanceFacade.withdraw(orderId, BalanceAccountConstant.TOTAL_ACCOUNT, amount, BalanceType.E_COIN);
            if(!r.isSuccess()) {
                logger.info("更新总账账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
                throw new ServiceException("更新总账账本余额失败:{}", r.getMessage());
            }
            this.saveTransactionLog(orderId, 
                                    OrderType.WITHDRAW, 
                                    amount, 
                                    now, 
                                    "提现:" + amount, 
                                    OperateType.OUT, 
                                    partnerId, 
                                    partnerTradeNo, 
                                    channelId.name(), 
                                    channelOrderTradeNo, 
                                    Subject.WITHDRAW, 
                                    SubSubject.WITHDRAW, 
                                    fee, 
                                    BalanceAccountConstant.TOTAL_ACCOUNT, 
                                    0);
            
            //渠道账本提现手续费扣款和流水
            r = balanceFacade.withdrawFee(orderId, channelId.getBalanceUserId(), fee, BalanceType.E_COIN);
            if(!r.isSuccess()) {
                logger.info("更新渠道账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
                throw new ServiceException("更新渠道账本余额失败:{}", r.getMessage());
            }
            this.saveTransactionLog(orderId, 
                                    OrderType.WITHDRAW_FEE, 
                                    fee, 
                                    now, 
                                    "提现手续费:" + fee, 
                                    OperateType.OUT, 
                                    partnerId, 
                                    partnerTradeNo, 
                                    channelId.name(), 
                                    channelOrderTradeNo, 
                                    Subject.WITHDRAW_FEE, 
                                    SubSubject.WITHDRAW_FEE, 
                                    BigDecimal.ZERO, 
                                    channelId.getBalanceUserId(), 
                                    0);
            
            //总账本提现手续费扣款和流水
            r = balanceFacade.withdrawFee(orderId, BalanceAccountConstant.TOTAL_ACCOUNT, fee, BalanceType.E_COIN);
            if(!r.isSuccess()) {
                logger.info("更新总账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
                throw new ServiceException("更新总账本余额失败:{}", r.getMessage());
            }
            this.saveTransactionLog(orderId, 
                                    OrderType.WITHDRAW_FEE, 
                                    fee, 
                                    now, 
                                    "提现手续费:" + fee, 
                                    OperateType.OUT, 
                                    partnerId, 
                                    partnerTradeNo, 
                                    channelId.name(), 
                                    channelOrderTradeNo, 
                                    Subject.WITHDRAW_FEE, 
                                    SubSubject.WITHDRAW_FEE, 
                                    BigDecimal.ZERO, 
                                    BalanceAccountConstant.TOTAL_ACCOUNT, 
                                    0);
        }
    }

    @Override
    public Result<String> adjust(long userId, BigDecimal amount) {
        
        logger.info("调账, userId:{}, amount:{}", userId, amount);
        
        //判断余额
        Result<BigDecimal> userBalance = balanceFacade.getUserBalance(userId, BalanceType.E_COIN);
        if(!userBalance.isSuccess()) {
            logger.error("获取账本余额失败, userId:{}, 原因:{}", userId, userBalance.getMessage());
            return new Result<>(ResultCode.FAILURE, null, "获取账本余额失败:" + userBalance.getMessage());
        }
        
        if(userBalance.getObject().add(amount).compareTo(BigDecimal.ZERO) < 0) {
            logger.error("余额不足, amount:{}, balance:{}", amount, userBalance.getObject());
            return new Result<>(ResultCode.FAILURE, null, "账本余额不足");
        }
        
        String orderId = OrderIdHelper.newOrderId();
        
        //资金流转
        Result<String> result = balanceFacade.adjust(orderId, userId, amount);
        if(!result.isSuccess()) {
            logger.error("调账失败, 原因:{}", result.getMessage());
            return new Result<>(ResultCode.FAILURE, null, "调账失败:" + result.getMessage());
        }
        
        Date now = new Date();
        
        //记录资金流水
        //转出
        this.saveTransactionLog(orderId, 
                                OrderType.DATA_FIX, 
                                amount, 
                                now, 
                                "调账:" + amount, 
                                amount.compareTo(BigDecimal.ZERO) > 0 ? OperateType.IN : OperateType.OUT, 
                                "", 
                                "", 
                                "", 
                                "", 
                                Subject.DATA_FIX, 
                                SubSubject.DATA_FIX, 
                                BigDecimal.ZERO, 
                                userId, 
                                0);
        
        //写入订单表
        POrderPO order=new POrderPO();
        order.setOrderId(orderId);
        order.setOrderType(OrderType.DATA_FIX.getType());
        order.setAmount(amount);
        order.setPayChannel("");
        order.setChannelTradeNo("");
        order.setPartnerId("");
        order.setPartnerTradeNo("");
        order.setBankId("");
        order.setCreateTime(now);
        order.setRefundAmount(amount);
        order.setUpdateTime(now);
        order.setStatus(OrderStatus.PAYED.getStatus());
        order.setIp(AccessContext.getAccessIp());
        order.setSubject(Subject.DATA_FIX.getVal());
        order.setSubSubject(SubSubject.DATA_FIX.getVal());
        order.setNotifyStatus(NotifyStatus.SUCCESS.getStatus());//通知完成
        order.setFee(BigDecimal.ZERO);//手续费
        int affectRow = orderMapper.insert(order);
        if (affectRow <= 0) {
            logger.info("写入调账订单表失败,order:{}", order);
            throw new ServiceException("写入调账订单表失败");
        }
        
        logger.info("调账成功,orderId:{}", orderId);
        return new Result<String>(ResultCode.SUCCESS, orderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<String> sendPayAgentGetCodeRequest(
            PartnerPayAgentGetCodeData data, String orderId) {

        PPayAgentApplyRequestPO po = pPayAgentApplyRequestMapper.getByPartnerOrderId(data.getPartnerId().name(), data.getPartnerOrderId());
        if(po != null){
            logger.info("合作平台的订单号重复, partnerId:{}, partnerOrderId:{}", data.getPartnerId(), data.getPartnerOrderId());
            return new Result<String>(ResultCode.FAILURE, null, "订单重复");
        }
        
        String cardNo = encoder.encode(data.getCardNo());
        String realName = encoder.encode(data.getRealName());
        String idNo = encoder.encode(data.getIdNo());
        String cellphone = encoder.encode(data.getCellphone());
        
        po = new PPayAgentApplyRequestPO();
        po.setOrderId(orderId);
        po.setPartnerId(data.getPartnerId().name());
        po.setPartnerOrderId(data.getPartnerOrderId());
        po.setChannel(data.getChannel().name());
        po.setChannelOrderId("");
        po.setBankId(data.getBankId().getBankId());
        po.setCardNo(cardNo);
        po.setRealName(realName);
        po.setIdNo(idNo);
        po.setCellphone(cellphone);
        po.setAmount(data.getAmount());
        po.setMerOrderId("");
        po.setCustId("");
        po.setPhoneToken("");
        po.setRequestUrl("");
        po.setResult("");
        po.setIp(data.getIp());
        po.setStatus(AgentStatus.RECEIVE.getStatus());
        po.setCreateTime(new Date());
        po.setUpdateTime(new Date());
        
        PartnerPayAgentGetCodeData newData = new PartnerPayAgentGetCodeData();
        try {
            BeanUtils.copyProperties(newData, data);
        } catch (Exception e) {
        }

        //对敏感信息进行加密
        newData.setCardNo(cardNo);
        newData.setRealName(realName);
        newData.setIdNo(idNo);
        newData.setCellphone(cellphone);
        
        po.setParam(JSON.toJSONString(newData, false));

        int affectRow = pPayAgentApplyRequestMapper.insert(po);
        if(affectRow <= 0){
            logger.error("写入鉴权请求表失败, PartnerPayAgentGetCodeData:{}",po);
            return new Result<String>(ResultCode.FAILURE, orderId, "写入鉴权请求表失败");
        }
        
        logger.info("写入鉴权请求表成功, PartnerPayAgentGetCodeData:{}", po);
        return new Result<String>(ResultCode.SUCCESS, orderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<String> PayAgentGetCodeCallback(boolean success, String orderId,
            PayAgentChannel channel, UmbpayAccreditVO backData) {
        
        logger.info("代扣鉴权发送验证码回调订单处理, orderId:{}, success:{}, channelId:{}, backData:{}", orderId, success, channel.name(), backData);
        
        String channelOrderId = StringUtils.trimToEmpty(backData.getChannelOrderId());
        String requestUrl = StringUtils.trimToEmpty(backData.getRequestUrl());
        String result = StringUtils.trimToEmpty(backData.getResultStr());
        String merOrderId = StringUtils.trimToEmpty(backData.getMerOrderId());
        String custId = StringUtils.trimToEmpty(backData.getCustId());
        String phoneToken = StringUtils.trimToEmpty(backData.getPhoneToken());
        
        //截取1024字节
        if(StringUtils.isNotBlank(result)) {
            result = result.length() > 1024 ? result.substring(0,1024) : result;
        }
        
        PPayAgentApplyRequestPO po = pPayAgentApplyRequestMapper.getByOrderId(orderId);
        if(po == null) {
            logger.error("回调找不到对应的请求订单信息：orderId:{}", orderId);
            return new Result<String>(ResultCode.FAILURE, orderId, "回调找不到对应的请求订单号");
        }
        
        int newStatus = success ? AgentStatus.SUCCESS.getStatus() : AgentStatus.FAILURE.getStatus();
        
        int affectRow = pPayAgentApplyRequestMapper.updateCallback(orderId,
                                                      AgentStatus.RECEIVE.getStatus(),
                                                      newStatus,
                                                      result,
                                                      channelOrderId,
                                                      requestUrl, 
                                                      merOrderId, 
                                                      custId, 
                                                      phoneToken, 
                                                      new Date());
        if (affectRow <= 0) {
            logger.info("{} 代收认证申请, 更新回调状态失败, orderId:{}, po:{}", orderId, po);
            return new Result<String>(ResultCode.FAILURE, orderId, "更新请求记录状态失败");
        }
        
        logger.info("代收获取验证码回调处理成功, success:{}, channelId:{}, orderId:{}", success, channel.name(), orderId);
        return new Result<String>(ResultCode.SUCCESS, orderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<PPayAgentApplyRequestVO> sendPayAgentCheckCodeRequest(String orderId,
            PartnerPayAgentCheckCodeData data) {
        
        //判断代收鉴权申请是否存在
        PPayAgentApplyRequestPO pPayAgentApplyRequestPO = pPayAgentApplyRequestMapper.getByPartnerOrderId(data.getPartnerId().name(), data.getPartnerApplyOrderId());
        
        if(pPayAgentApplyRequestPO == null){
            logger.error("找不到对应的代收鉴权申请, partnerId:{}, partnerApplyOrderId:{}", data.getPartnerId(), data.getPartnerApplyOrderId());
            return new Result<>(ResultCode.FAILURE, null, "代收鉴权申请不存在");
        }
        
        if(pPayAgentApplyRequestPO.getStatus() != AgentStatus.SUCCESS.getStatus()) {
            logger.error("代收鉴权申请非成功状态, partnerId:{}, partnerApplyOrderId:{}, status:{}", data.getPartnerId(), data.getPartnerApplyOrderId(), pPayAgentApplyRequestPO.getStatus());
            return new Result<>(ResultCode.FAILURE, null, "代收鉴权申请非成功状态");
        }
        
        PPayAgentAuthRequestPO po = pPayAgentAuthRequestMapper.getByPartnerOrderId(data.getPartnerId().name(), data.getPartnerOrderId());
        if(po != null) {
            logger.error("代收鉴权认证合作方订单已经存在, partnerId:{}, partnerOrderId:{}", data.getPartnerId(), data.getPartnerOrderId());
            return new Result<>(ResultCode.FAILURE, null, "订单已经存在");
        }
        
        String paramJson = JSONUtils.toJson(data, false);
        if(StringUtils.isNotBlank(paramJson)) {
            paramJson = paramJson.length() > 1024 ? paramJson.substring(0,1024) : paramJson;
        }
        
        po = new PPayAgentAuthRequestPO();
        po.setOrderId(orderId);
        po.setPartnerId(data.getPartnerId().name());
        po.setPartnerOrderId(data.getPartnerOrderId());
        po.setAmount(pPayAgentApplyRequestPO.getAmount());
        po.setApplyOrderId(pPayAgentApplyRequestPO.getOrderId());
        po.setChannel(pPayAgentApplyRequestPO.getChannel());
        po.setParam(paramJson);
        po.setRequestUrl("");
        po.setResult("");
        po.setCallbackTime(new Date());
        po.setCallbackResult("");
        po.setIp(data.getIp());
        po.setStatus(AgentStatus.RECEIVE.getStatus());
        po.setNotifyUrl(data.getNotifyUrl());
        po.setCreateTime(new Date());
        po.setUpdateTime(new Date());
        po.setNextQueryTime(DateUtil.getDate(new Date(), 0, 0, 1, 0)); //1分钟以后
        po.setQueryNum(0);
        int affectRow = pPayAgentAuthRequestMapper.insert(po);
        if(affectRow < 1) {
            logger.error("插入代收鉴权认证请求表失败,partnerId:{},  partnerOrderId:{}, affectRow:{}", po.getPartnerId(), data.getPartnerOrderId(), affectRow);
            return new Result<>(ResultCode.FAILURE, null, "保存请求记录失败");
        }
        
        PPayAgentApplyRequestVO vo = new PPayAgentApplyRequestVO(pPayAgentApplyRequestPO);
        
        logger.info("代收鉴权认证请求订单处理成功, PPayAgentAuthRequestPO:{}", po);
        
        return new Result<>(ResultCode.SUCCESS, vo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<String> PayAgentCheckCodeCallback(boolean success, PayAgentChannel channel, 
            String orderId, UmbpaySignWhiteListOneVO backData) {
        
        logger.info("代扣鉴权回调处理, success:{}， orderId:{}, backData:{}", success, orderId, backData);
        
        String resultStr = StringUtils.trimToEmpty(backData.getResultStr());
        String callbackStr = StringUtils.trimToEmpty(backData.getCallbackStr());
        String requestUrl = StringUtils.trimToEmpty(backData.getRequestUrl());
        
        //截取1024字节
        if(StringUtils.isNotBlank(resultStr)) {
            resultStr = resultStr.length() > 1024 ? resultStr.substring(0,1024) : resultStr;
        }
        if(StringUtils.isNotBlank(callbackStr)) {
            callbackStr = callbackStr.length() > 1024 ? callbackStr.substring(0,1024) : callbackStr;
        }
        if(backData.getSignState() == AgentStatus.RECEIVE){
            logger.info("订单返回状态：RECEIVE, orderId:{}, now return", orderId);
            return new Result<String>(ResultCode.FAILURE, orderId, "第三方请求返回状态处理中"); 
        }
        
        PPayAgentAuthRequestPO requestPO = pPayAgentAuthRequestMapper.getByOrderId(orderId);
        if (requestPO == null) {
            logger.info("找不到对应的请求订单信息：orderId:{}", orderId);
            return new Result<String>(ResultCode.FAILURE, orderId, "找不到对应的请求订单号");
        }

        // 防止重复刷
        if (requestPO.getStatus() != AgentStatus.RECEIVE.getStatus()) {
            logger.info("{} 代收鉴权认证请求 : requestPO is not RECEIVE, now return：{}", orderId, requestPO);
            return new Result<String>(ResultCode.SUCCESS, orderId);
        }
        
        int affectRow = pPayAgentAuthRequestMapper.updateCheckCodeCallback(orderId, AgentStatus.RECEIVE.getStatus(), backData.getSignState().getStatus(), requestUrl, resultStr, callbackStr, new Date(), new Date());
        
        if (affectRow <= 0) {
            logger.info("{} 代扣鉴权认证，回调订单已经处理 ， requestPO is not CHECK_CODE, now return", orderId, requestPO);
            return new Result<String>(ResultCode.SUCCESS, orderId);
        }
        
        Date now = new Date();
        
        //如果是失败状态，不写入订单，不更新账本
        if(backData.getSignState() == AgentStatus.FAILURE) {
            logger.info("订单已经处理，不更新账本,直接返回成功, orderId:{}", orderId);
            return new Result<String>(ResultCode.SUCCESS, orderId);
        }
        
        //渠道账本提现扣款和流水
        Result<String> r = balanceFacade.payAgentAuth(orderId, channel.getBalanceUserId(), requestPO.getAmount());
        if(!r.isSuccess()) {
            logger.info("更新渠道账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
            throw new ServiceException("更新渠道账本余额失败:{}", r.getMessage());
        }
        this.saveTransactionLog(orderId, 
                                OrderType.PAY_AGENT_AUTH, 
                                requestPO.getAmount(), 
                                now, 
                                "代收鉴权:" + requestPO.getAmount(), 
                                OperateType.IN, 
                                requestPO.getPartnerId(), 
                                requestPO.getPartnerOrderId(), 
                                channel.name(), 
                                "", 
                                Subject.PAY_AGENT_AUTH, 
                                SubSubject.PAY_AGENT_AUTH, 
                                backData.getFee(), 
                                channel.getBalanceUserId(), 
                                0);
        
        //总账本提现扣款和流水
        r = balanceFacade.payAgentAuth(orderId, BalanceAccountConstant.TOTAL_ACCOUNT, requestPO.getAmount());
        if(!r.isSuccess()) {
            logger.info("更新总账账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
            throw new ServiceException("更新总账账本余额失败:{}", r.getMessage());
        }
        this.saveTransactionLog(orderId, 
                                OrderType.PAY_AGENT_AUTH, 
                                requestPO.getAmount(), 
                                now, 
                                "代收鉴权:" + requestPO.getAmount(), 
                                OperateType.IN, 
                                requestPO.getPartnerId(), 
                                requestPO.getPartnerOrderId(), 
                                channel.name(), 
                                "", 
                                Subject.PAY_AGENT_AUTH, 
                                SubSubject.PAY_AGENT_AUTH, 
                                backData.getFee(), 
                                BalanceAccountConstant.TOTAL_ACCOUNT,
                                0);
        
        //渠道账本提现手续费扣款和流水
        r = balanceFacade.payAgentAuthFee(orderId, channel.getBalanceUserId(), backData.getFee());
        if(!r.isSuccess()) {
            logger.info("更新渠道账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
            throw new ServiceException("更新渠道账本余额失败:{}", r.getMessage());
        }
        this.saveTransactionLog(orderId, 
                                OrderType.PAY_AGENT_AUTH_FEE, 
                                backData.getFee(), 
                                now, 
                                "代收鉴权手续费:" + backData.getFee(), 
                                OperateType.OUT, 
                                requestPO.getPartnerId(), 
                                requestPO.getPartnerOrderId(), 
                                channel.name(), 
                                "", 
                                Subject.PAY_AGENT_AUTH_FEE, 
                                SubSubject.PAY_AGENT_AUTH_FEE, 
                                BigDecimal.ZERO, 
                                channel.getBalanceUserId(), 
                                0);
        
        //总账本提现手续费扣款和流水
        r = balanceFacade.payAgentAuthFee(orderId, BalanceAccountConstant.TOTAL_ACCOUNT, backData.getFee());
        if(!r.isSuccess()) {
            logger.info("更新总账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
            throw new ServiceException("更新总账本余额失败:{}", r.getMessage());
        }
        this.saveTransactionLog(orderId, 
                                OrderType.PAY_AGENT_AUTH_FEE, 
                                backData.getFee(), 
                                now, 
                                "代收鉴权手续费:" + backData.getFee(), 
                                OperateType.OUT, 
                                requestPO.getPartnerId(), 
                                requestPO.getPartnerOrderId(), 
                                channel.name(), 
                                "", 
                                Subject.PAY_AGENT_AUTH_FEE, 
                                SubSubject.PAY_AGENT_AUTH_FEE, 
                                BigDecimal.ZERO, 
                                BalanceAccountConstant.TOTAL_ACCOUNT, 
                                0);
        
        //写入订单表
        POrderPO order=new POrderPO();
        order.setOrderId(orderId);
        order.setOrderType(OrderType.PAY_AGENT_AUTH.getType());
        order.setAmount(requestPO.getAmount());
        order.setPayChannel(channel.name());
        order.setChannelTradeNo("");
        order.setPartnerId(requestPO.getPartnerId());
        order.setPartnerTradeNo(requestPO.getPartnerOrderId());
        order.setBankId("");
        order.setCreateTime(now);
        order.setBankId("");
        //order.setChRefundTime(null); //渠道通知完成时间
        //order.setFinishTime(null); finishTime这时候为空，通知接入方后才设置为成功
        order.setRefundAmount(requestPO.getAmount());
        //order.setGmtPayment(null);   //用户支付时间
        order.setUpdateTime(now);
        order.setStatus(OrderStatus.PAYED.getStatus());
        order.setIp(AccessContext.getAccessIp());
        order.setSubject(Subject.PAY_AGENT_AUTH.getVal());
        order.setSubSubject(SubSubject.PAY_AGENT_AUTH.getVal());
        //order.setRefrenceId(null); //冲正订单
        order.setNotifyStatus(NotifyStatus.REQUEST.getStatus());//通知中
        order.setFee(backData.getFee());//手续费
        affectRow = orderMapper.insert(order);
        if (affectRow <= 0) {
            logger.info("写入代收鉴权订单表失败,order:{}", order);
            throw new ServiceException("写入代收鉴权订单表失败");
        }
        
        logger.info("代收鉴权回调成功,success:{}, channelId:{}",success,channel.name());
        return new Result<String>(ResultCode.SUCCESS, orderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<String> sendPayAgentChargeRequest(String orderId,
            PartnerPayAgentChargeData data) {
        
        PPayAgentChargeRequestPO po = pPayAgentChargeRequestMapper.getByPartnerOrderId(data.getPartnerId().name(), data.getPartnerOrderId());
        if(po != null){
            logger.info("合作平台的订单号重复, partnerId:{}, partnerOrderId:{}", data.getPartnerId(), data.getPartnerOrderId());
            return new Result<String>(ResultCode.FAILURE, null, "订单重复");
        }
        
        String cardNo = encoder.encode(data.getCardNo());
        String realName = encoder.encode(data.getRealName());
        String idNo = encoder.encode(data.getIdNo());
        String cellphone = encoder.encode(data.getCellphone());
        
        po = new PPayAgentChargeRequestPO();
        po.setOrderId(orderId);
        po.setPartnerId(data.getPartnerId().name());
        po.setPartnerOrderId(data.getPartnerOrderId());
        po.setChannel(data.getChannel().name());
        po.setChannelOrderId("");
        po.setRequestUrl("");
        po.setResult("");
        po.setCreateTime(new Date());
        po.setCallbackTime(new Date());
        po.setCallbackResult("");
        po.setAmount(data.getAmount());
        po.setStatus(AgentStatus.RECEIVE.getStatus());
        po.setIp(data.getIp());
        po.setNotifyUrl(data.getNotifyUrl());
        po.setNextQueryTime(DateUtil.getDate(new Date(), 0, 0, 1, 0));  //1分钟以后
        po.setQueryNum(0);
        
        PartnerPayAgentChargeData newData = new PartnerPayAgentChargeData();
        try {
            BeanUtils.copyProperties(newData, data);
        } catch (Exception e) {
        }
        //对敏感信息进行加密
        newData.setCardNo(cardNo);
        newData.setRealName(realName);
        newData.setIdNo(idNo);
        newData.setCellphone(cellphone);
        String param = JSONUtils.toJson(newData, false);
        if(StringUtils.isNotBlank(param)) {
            param = param.length() > 1024 ? param.substring(0,1024) : param;
        }
        po.setParam(param);

        int affectRow = pPayAgentChargeRequestMapper.insert(po);
        if(affectRow <= 0){
            logger.error("写入代收请求表失败, PartnerPayAgentGetCodeData:{}",po);
            return new Result<String>(ResultCode.FAILURE, orderId, "写入代收请求表失败");
        }
        
        logger.info("写入代收请求表成功, PPayAgentChargeRequestPO:{}", po);
        return new Result<String>(ResultCode.SUCCESS, orderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<String> PayAgentChargeCallback(boolean success,
            PayAgentChannel channel, String orderId,
            UmbpayAgentVO backData) {
        
        logger.info("代扣鉴权回调处理, success:{}， orderId:{}, backData:{}", success, orderId, backData);
        
        String resultStr = StringUtils.trimToEmpty(backData.getResultStr());
        String callbackStr = StringUtils.trimToEmpty(backData.getCallbackStr());
        String requestUrl = StringUtils.trimToEmpty(backData.getRequestUrl());
        String channelOrderId = StringUtils.trimToEmpty(backData.getBussflowno());
        
        //截取1024字节
        if(StringUtils.isNotBlank(resultStr)) {
            resultStr = resultStr.length() > 1024 ? resultStr.substring(0,1024) : resultStr;
        }
        if(StringUtils.isNotBlank(callbackStr)) {
            callbackStr = callbackStr.length() > 1024 ? callbackStr.substring(0,1024) : callbackStr;
        }
        if(backData.getSignState() == AgentStatus.RECEIVE){
            logger.info("订单返回状态：RECEIVE, orderId:{}, now return", orderId);
            
            //处理中更新渠道订单号
            pPayAgentChargeRequestMapper.updateCallback(
                                                        orderId, 
                                                        AgentStatus.RECEIVE.getStatus(), 
                                                        AgentStatus.RECEIVE.getStatus(), 
                                                        requestUrl, 
                                                        channelOrderId,
                                                        resultStr, 
                                                        callbackStr, 
                                                        new Date());

            return new Result<String>(ResultCode.FAILURE, orderId, "返回状态处理中"); 
        }
        
        //更新请求状态
        int affectRow = pPayAgentChargeRequestMapper.updateCallback(
                                                                    orderId, 
                                                                    AgentStatus.RECEIVE.getStatus(), 
                                                                    backData.getSignState().getStatus(), 
                                                                    requestUrl, 
                                                                    channelOrderId,
                                                                    resultStr, 
                                                                    callbackStr, 
                                                                    new Date());
        
        if (affectRow <= 0) {
            logger.info("{} 代扣回调订单已经处理 ， requestPO is not CHECK_CODE, now return", orderId);
            return new Result<String>(ResultCode.SUCCESS, orderId);
        }
        
        PPayAgentChargeRequestPO requestPO = pPayAgentChargeRequestMapper.getByOrderId(orderId);
        if (requestPO == null) {
            logger.info("找不到对应的请求订单信息：orderId:{}", orderId);
            throw new ServiceException("找不到对应的请求订单信息");
        }
        
        Date now = new Date();
        
        //如果是失败状态，不写入订单，不更新账本
        if(backData.getSignState() == AgentStatus.FAILURE) {
            return new Result<String>(ResultCode.SUCCESS, orderId);
        }
        
        //渠道账本代收扣款和流水
        Result<String> r = balanceFacade.payAgentCharge(orderId, channel.getBalanceUserId(), requestPO.getAmount());
        if(!r.isSuccess()) {
            logger.info("更新渠道账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
            throw new ServiceException("更新渠道账本余额失败:{}", r.getMessage());
        }
        this.saveTransactionLog(orderId, 
                                OrderType.PAY_AGENT_CHARGE, 
                                requestPO.getAmount(), 
                                now, 
                                "代收:" + requestPO.getAmount(), 
                                OperateType.IN, 
                                requestPO.getPartnerId(), 
                                requestPO.getPartnerOrderId(), 
                                channel.name(), 
                                "", 
                                Subject.PAY_AGENT_CHARGE, 
                                SubSubject.PAY_AGENT_CHARGE, 
                                backData.getFee(), 
                                channel.getBalanceUserId(), 
                                0);
        
        //总账本提现扣款和流水
        r = balanceFacade.payAgentCharge(orderId, BalanceAccountConstant.TOTAL_ACCOUNT, requestPO.getAmount());
        if(!r.isSuccess()) {
            logger.info("更新总账账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
            throw new ServiceException("更新总账账本余额失败:{}", r.getMessage());
        }
        this.saveTransactionLog(orderId, 
                                OrderType.PAY_AGENT_CHARGE, 
                                requestPO.getAmount(), 
                                now, 
                                "代收:" + requestPO.getAmount(), 
                                OperateType.IN, 
                                requestPO.getPartnerId(), 
                                requestPO.getPartnerOrderId(), 
                                channel.name(), 
                                "", 
                                Subject.PAY_AGENT_CHARGE, 
                                SubSubject.PAY_AGENT_CHARGE, 
                                backData.getFee(), 
                                BalanceAccountConstant.TOTAL_ACCOUNT,
                                0);
        
        //渠道账本提现手续费扣款和流水
        r = balanceFacade.payAgentChargeFee(orderId, channel.getBalanceUserId(), backData.getFee());
        if(!r.isSuccess()) {
            logger.info("更新渠道账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
            throw new ServiceException("更新渠道账本余额失败:{}", r.getMessage());
        }
        this.saveTransactionLog(orderId, 
                                OrderType.PAY_AGENT_CHARGE_FEE, 
                                backData.getFee(), 
                                now, 
                                "代收手续费:" + backData.getFee(), 
                                OperateType.OUT, 
                                requestPO.getPartnerId(), 
                                requestPO.getPartnerOrderId(), 
                                channel.name(), 
                                "", 
                                Subject.PAY_AGENT_CHARGE_FEE, 
                                SubSubject.PAY_AGENT_CHARGE_FEE, 
                                BigDecimal.ZERO, 
                                channel.getBalanceUserId(), 
                                0);
        
        //总账本提现手续费扣款和流水
        r = balanceFacade.payAgentChargeFee(orderId, BalanceAccountConstant.TOTAL_ACCOUNT, backData.getFee());
        if(!r.isSuccess()) {
            logger.info("更新总账本余额失败,orderId:{}， 原因:{}", orderId, r.getMessage());
            throw new ServiceException("更新总账本余额失败:{}", r.getMessage());
        }
        this.saveTransactionLog(orderId, 
                                OrderType.PAY_AGENT_CHARGE_FEE, 
                                backData.getFee(), 
                                now, 
                                "代收手续费:" + backData.getFee(), 
                                OperateType.OUT, 
                                requestPO.getPartnerId(), 
                                requestPO.getPartnerOrderId(), 
                                channel.name(), 
                                "", 
                                Subject.PAY_AGENT_CHARGE_FEE, 
                                SubSubject.PAY_AGENT_CHARGE_FEE, 
                                BigDecimal.ZERO, 
                                BalanceAccountConstant.TOTAL_ACCOUNT, 
                                0);
        
        //写入订单表
        POrderPO order=new POrderPO();
        order.setOrderId(orderId);
        order.setOrderType(OrderType.PAY_AGENT_CHARGE.getType());
        order.setAmount(requestPO.getAmount());
        order.setPayChannel(channel.name());
        order.setChannelTradeNo(requestPO.getChannelOrderId() == null ? "" : requestPO.getChannelOrderId());
        order.setPartnerId(requestPO.getPartnerId());
        order.setPartnerTradeNo(requestPO.getPartnerOrderId());
        order.setBankId("");
        order.setCreateTime(now);
        HashMap<String, String> map = JSONUtils.fromJson(requestPO.getParam(), new TypeToken<HashMap<String, String>>() {});
        String bank = map.get("bankId");
        String bankId = "";
        if(StringUtils.isNotBlank(bank)) {
            bankId = BankIdConstant.valueOf(bank).getBankId();
        }
        order.setBankId(bankId);
        //order.setChRefundTime(null); //渠道通知完成时间
        //order.setFinishTime(null); finishTime这时候为空，通知接入方后才设置为成功
        order.setRefundAmount(requestPO.getAmount());
        //order.setGmtPayment(null);   //用户支付时间
        order.setUpdateTime(now);
        order.setStatus(OrderStatus.PAYED.getStatus());
        order.setIp(AccessContext.getAccessIp());
        order.setSubject(Subject.PAY_AGENT_CHARGE.getVal());
        order.setSubSubject(SubSubject.PAY_AGENT_CHARGE.getVal());
        //order.setRefrenceId(null); //冲正订单
        order.setNotifyStatus(NotifyStatus.REQUEST.getStatus());//通知中
        order.setFee(backData.getFee());//手续费
        affectRow = orderMapper.insert(order);
        if (affectRow <= 0) {
            logger.info("写入代收订单表失败,order:{}", order);
            throw new ServiceException("写入代收订单表失败");
        }
        
        logger.info("代收回调成功,success:{}, channelId:{}",success,channel.name());
        return new Result<String>(ResultCode.SUCCESS, orderId);
    }
}
