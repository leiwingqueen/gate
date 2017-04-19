package com.elend.gate.web.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.elend.gate.balance.constant.BalanceType;
import com.elend.gate.balance.facade.BalanceFacade;
import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.constant.WithdrawStatus;
import com.elend.gate.channel.exception.CheckSignatureException;
import com.elend.gate.channel.exception.ParamException;
import com.elend.gate.channel.facade.PartnerFacade;
import com.elend.gate.channel.facade.WithdrawChannelFacade;
import com.elend.gate.channel.facade.vo.PartnerWithdrawData;
import com.elend.gate.channel.facade.vo.PartnerWithdrawRequest;
import com.elend.gate.channel.facade.vo.PartnerWithdrawResponse;
import com.elend.gate.channel.facade.vo.WithdrawCallbackData;
import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.conf.facade.SystemConfig;
import com.elend.gate.notify.facade.NotifyFacade;
import com.elend.gate.notify.facade.WithdrawQueueFacade;
import com.elend.gate.notify.model.NWithdrawQueuePO;
import com.elend.gate.order.constant.OrderCompleteStatus;
import com.elend.gate.order.constant.OrderType;
import com.elend.gate.order.facade.OrderFacade;
import com.elend.gate.order.facade.POrderStatusRecordFacade;
import com.elend.gate.order.facade.PWithdrawRequestFacade;
import com.elend.gate.order.model.POrderPO;
import com.elend.gate.order.model.POrderStatusRecrodPO;
import com.elend.gate.order.model.PWithdrawRequestPO;
import com.elend.gate.risk.RiskHandlerChain;
import com.elend.gate.risk.vo.RiskParam;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.gate.web.service.WithdrawService;
import com.elend.p2p.Result;
import com.elend.p2p.ServiceException;
import com.elend.p2p.constant.ResultCode;

@Service
public class WithdrawServiceImpl implements WithdrawService{
    private final static Logger logger = LoggerFactory.getLogger(WithdrawServiceImpl.class);
    @Autowired
    private WithdrawChannelFacade withdrawChannelFacade;
    @Autowired
    private PartnerFacade partnerFacade;
    @Autowired
    private OrderFacade orderFacade;
    @Autowired
    private NotifyFacade notifyFacade;
    @Autowired
    @Qualifier("withdrawRiskHandlerChain")
    private RiskHandlerChain withdrawRiskHandlerChain;
    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private WithdrawQueueFacade withdrawQueueFacade;
    @Autowired
    private POrderStatusRecordFacade pOrderStatusRecordFacade;
    @Autowired
    private BalanceFacade balanceFacade;
    @Autowired
    private PWithdrawRequestFacade pWithdrawRequestFacade;
    
    @Override
    public Result<PartnerWithdrawResponse> withdrawRequest(PartnerWithdrawRequest params) {
        //风控
        RiskParam riskParam=new RiskParam();
        riskParam.setIp(params.getIp());
        riskParam.setReferer("");
        if(StringUtils.isBlank(params.getTimeStamp())){
            return new Result<PartnerWithdrawResponse>(ResultCode.FAILURE,null,"时间戳不能为空");
        }
        long timeStamp=0L;
        try{
            timeStamp=Long.parseLong(params.getTimeStamp());
        }catch(NumberFormatException e){
            logger.error("时间戳不为长整形,timeStamp:"+params.getTimeStamp());
            return new Result<PartnerWithdrawResponse>(ResultCode.FAILURE,null,"时间戳格式错误"); 
        }
        riskParam.setTimeStamp(timeStamp);
        Result<String> r = withdrawRiskHandlerChain.handle(riskParam);
        if(!r.isSuccess()){
            return new Result<PartnerWithdrawResponse>(ResultCode.FAILURE,  null, r.getMessage());
        }
        //1.签名和参数格式验证(约定和P2P通讯格式)
        PartnerWithdrawData data = null;
        try{
            data = partnerFacade.withdrawRequest(params);
        }catch(ParamException e){
            logger.error("参数错误,{}",e.getMessage());
            return new Result<PartnerWithdrawResponse>(ResultCode.FAILURE, null, "参数错误,"+e.getMessage());
        }catch(CheckSignatureException e){
            logger.error("签名验证失败");
            return new Result<PartnerWithdrawResponse>(ResultCode.FAILURE,null,"签名验证失败");
        }catch (ServiceException e) {
            logger.error("提现失败,失败原因:{}",e.getMessage());
            return new Result<PartnerWithdrawResponse>(ResultCode.FAILURE,null,e.getMessage());
        }
        
        //封装返回信息
        PartnerWithdrawResponse response = new PartnerWithdrawResponse();
        response.setPartnerId(data.getPartnerId().name());
        response.setPartnerOrderId(data.getPartnerOrderId());
        response.setAmount(data.getAmount().toString());
        response.setChannelId(data.getChannel().name());
       
        
        //2.判断订单是否已经存在（订单状态记录表）
        POrderStatusRecrodPO statusPo = pOrderStatusRecordFacade.getByPartnerOrderId(data.getPartnerId(), data.getPartnerOrderId());
        if(statusPo != null) {
            logger.error("提现申请重复，直接返回提现结果，  statusPo:{}", statusPo);
            OrderCompleteStatus orderStatus = OrderCompleteStatus.from(statusPo.getStatus());
            if(OrderCompleteStatus.FAILURE == orderStatus) {
                response.setWithdrawStatus(WithdrawStatus.FAILURE.name());
            } else if(OrderCompleteStatus.SUCCESS == orderStatus) {
                response.setWithdrawStatus(WithdrawStatus.SUCCESS.name());
                
                //如果成功，返回手续费的信息
                POrderPO pOrderPO = orderFacade.getOrderBypartnerOrderId(data.getPartnerId(), data.getPartnerOrderId());
                if(pOrderPO == null) {
                    logger.info("订单状态记录为成功，找不到对应的订单号， PartnerId:{}, PartnerOrderId:{}", data.getPartnerId(), data.getPartnerOrderId());
                    return new Result<PartnerWithdrawResponse>(ResultCode.FAILURE, null, "订单状态记录为成功，找不到对应的订单号");
                }
                response.setFee(pOrderPO.getFee().toString());
                response.setOrderId(pOrderPO.getOrderId());
            } else {
                response.setWithdrawStatus(WithdrawStatus.APPLYING.name());
            }
            
            
            //生成签名
            String hmac = partnerFacade.genSignature(response);
            response.setHmac(hmac);
            
            return new Result<PartnerWithdrawResponse>(ResultCode.SUCCESS, response, "订单处理完成");
        }
        
        //判断账本是否够钱，如果不够钱，直接返回失败
        Result<BigDecimal> bResult = balanceFacade.getUserBalance(Enum.valueOf(com.elend.gate.balance.constant.WithdrawChannel.class, params.getChannel()).getBalanceUserId(), BalanceType.E_COIN);
        //账本查询失败
        if(!bResult.isSuccess()) {
            String msg = "Exception : 提现账本不存在，导致订单失败, userId:" + Enum.valueOf(com.elend.gate.balance.constant.WithdrawChannel.class, params.getChannel()).getBalanceUserId() + ", pOrderId: " + params.getPartnerOrderId();
            logger.error(msg, new ServiceException(msg));
            response.setWithdrawStatus(WithdrawStatus.FAILURE.name());
            String hmac = partnerFacade.genSignature(response);
            response.setHmac(hmac);
            
            return new Result<PartnerWithdrawResponse>(ResultCode.FAILURE, response, "提现失败，请联系客服！");
        }
        
        //账本余额不足
        if(bResult.getObject().compareTo(new BigDecimal(params.getAmount())) < 0) {
            String msg = "Exception : 提现账本余额不足，导致订单失败, userId:" + Enum.valueOf(com.elend.gate.balance.constant.WithdrawChannel.class, params.getChannel()).getBalanceUserId() + ", pOrderId: " + params.getPartnerOrderId();
            logger.error(msg, new ServiceException(msg));
            response.setWithdrawStatus(WithdrawStatus.FAILURE.name());
            String hmac = partnerFacade.genSignature(response);
            response.setHmac(hmac);
            
            return new Result<PartnerWithdrawResponse>(ResultCode.FAILURE, response, "提现失败，请联系客服！");
        }
        
        /*WithdrawQueuePO po = withdrawQueueFacade.getWithdrawQueue(data.getPartnerId(), data.getPartnerOrderId());
        if(po != null) {
            logger.error("提现失败, 提现申请重复， queuePo:{}", po);
            return new Result<String>(ResultCode.FAILURE,params.getPartnerOrderId(), "提现申请失败，订单号重复");
        }*/

        //String orderId = OrderIdHelper.newOrderId15();//支付网关订单号
        String orderId = params.getPartnerOrderId();//支付网关订单号暂时使用支付网关的订单号，方便过渡，通过原来的代付渠道的回调也能正常回调，当民生的回调地址切换过来以后也能正常运行
        
        //3.参数校验成功，插入提现队列
        NWithdrawQueuePO po = new NWithdrawQueuePO();
        Date now = new Date();
        po.setOrderId(orderId);
        po.setExecuteTime(now);
        po.setCreateTime(now);
        po.setLastModify(now);
        po.setParams(JSON.toJSONString(data));
        po.setChannel(data.getChannel().name());
        po.setPartnerId(data.getPartnerId().name());
        po.setPartnerOrderId(data.getPartnerOrderId());
        
        int row = 0;
        //订单号重复异常
        row = withdrawQueueFacade.addWithdrawQueue(po);
        if(row < 1) {
            logger.error("提现失败, 失败原因:{}", "插入提现队列失败");
            return new Result<PartnerWithdrawResponse>(ResultCode.FAILURE, null, "保存提现申请失败");
        }
        
        //4.插入订单状态表，跟踪订单状态
        statusPo = new POrderStatusRecrodPO();
        statusPo.setOrderId(orderId);
        statusPo.setOrderType(OrderType.WITHDRAW.getType());
        statusPo.setCreateTime(now);
        statusPo.setLastModify(now);
        statusPo.setChannel(data.getChannel().name());
        statusPo.setPartnerId(data.getPartnerId().name());
        statusPo.setPartnerOrderId(data.getPartnerOrderId());
        statusPo.setStatus(OrderCompleteStatus.GATE_RECEIVE.getStatus()); //网关已接收
        
        row =  pOrderStatusRecordFacade.save(statusPo);
        if(row < 1) {
            logger.error("提现失败, 失败原因:插入订单状态记录失败");
            return new Result<PartnerWithdrawResponse>(ResultCode.FAILURE, null, "插入订单状态记录失败");
        }
        
        response.setWithdrawStatus(WithdrawStatus.APPLYING.name());
        response.setOrderId(orderId);
        //生成签名
        String hmac = partnerFacade.genSignature(response);
        response.setHmac(hmac);
        return new Result<PartnerWithdrawResponse>(ResultCode.SUCCESS, response, "订单已经受理");
    }

    @Override
    public Result<String> withdrawCallback(WithdrawChannel channel,
            String backStr) {
        //1.支付渠道返回参数解析
        PartnerResult<WithdrawCallbackData> partnerResult=null;
        try{
            partnerResult = withdrawChannelFacade.withdrawCallback(channel, backStr);
        }catch(CheckSignatureException e){//像签名认证失败和格式错误等问题可以认为是不合法的回调请求，有可能是数据伪造和攻击。这时候不会把错误信息给到接入方，直接在网关系统提示错误页
            logger.error("提现渠道回调签名验证失败");
            return new Result<String>(ResultCode.FAILURE, "", "签名验证失败");
        }catch (ServiceException e) {
            logger.error("提现回调失败,失败原因:{}",e.getMessage());
            return new Result<String>(ResultCode.FAILURE,"",e.getMessage());
        }
        
        String orderId = partnerResult.getObject().getOrderId();
        //orderId = "15110609460102788001";
        //partnerResult.getObject().setOrderId(orderId);
        PWithdrawRequestPO request = orderFacade.getWithdrawRequest(orderId);
        if(request == null){
            logger.error("找不到提现请求,订单号:{}",orderId);
            return new Result<String>(ResultCode.FAILURE, "", "找不到对应的订单"); 
        }
        
        //订单处理
        return withdrawCallbackHandle(channel, partnerResult, request);
    }
    
    @Override
    public Result<String> withdrawSync(String orderId) {
        
        //查询订单信息
        //orderId = "15110609460102788001";
        //partnerResult.getObject().setOrderId(orderId);
        PWithdrawRequestPO request = orderFacade.getWithdrawRequest(orderId);
        if(request == null){
            logger.error("找不到提现请求,订单号:{}",orderId);
            return new Result<String>(ResultCode.FAILURE, "", "找不到对应的订单"); 
        }
        
        WithdrawChannel channel = WithdrawChannel.from(request.getChannel());
        
        //调用查询方法
        PartnerResult<WithdrawCallbackData> partnerResult = null;
        try{
            partnerResult = withdrawChannelFacade.withdrawSingleQuery(channel, orderId);
        }catch (Exception e) {
            logger.error("提现回调失败,失败原因:{}",e.getMessage());
            return new Result<String>(ResultCode.FAILURE,"",e.getMessage());
        }
        
        if(partnerResult.isSuccess()) {
            return new Result<String>(ResultCode.FAILURE,"",partnerResult.getMessage());
        }
        
        //有些渠道查询的时候没有返回金额， 所以使用请求表的金额
        if(partnerResult.getObject().getAmount() == null) {
            partnerResult.getObject().setAmount(request.getAmount());
        }
        
        //订单处理
        return withdrawCallbackHandle(channel, partnerResult, request);
    }

    /**
     * 提现回调订单处理
     * @param channel
     * @param partnerResult
     * @return
     */
    private Result<String> withdrawCallbackHandle(WithdrawChannel channel,
            PartnerResult<WithdrawCallbackData> partnerResult, PWithdrawRequestPO request) {
        
        //2.写入订单信息
        Result<String> result = orderFacade.withdrawCallback(partnerResult.isSuccess(), channel, partnerResult.getObject());
        
        //回调订单处理不成功，不通知接入方
        if(!result.isSuccess()){
            logger.error("订单处理失败...orderId:{},message:{}",request.getOrderId(),result.getMessage());
            return new Result<String>(ResultCode.FAILURE, "", result.getMessage());
        }
        
        //2.生成回调的form表单(约定和P2P通讯格式)
        RequestFormData form = partnerFacade.getWithdrawCallbackForm(channel, request.getPartnerId(), request.getPartnerOrderId(), partnerResult, request.getNotifyUrl());
        //4.写入通知队列
        String paramUrl = genUrlParamFormat(form.getParams());
        notifyFacade.addQueue(request.getOrderId(), PartnerConstant.from(request.getPartnerId()) ,request.getPartnerOrderId(), paramUrl, request.getNotifyUrl(), new Date(), 1);

        return new Result<String>(ResultCode.SUCCESS, partnerResult.getObject().getResponseStr());
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
