package com.elend.gate.admin.service.impl;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elend.gate.admin.service.WithdrawService;
import com.elend.gate.balance.facade.BalanceFacade;
import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.facade.PartnerFacade;
import com.elend.gate.channel.facade.WithdrawChannelFacade;
import com.elend.gate.channel.facade.vo.WithdrawCallbackData;
import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.conf.facade.SystemConfig;
import com.elend.gate.notify.facade.NotifyFacade;
import com.elend.gate.order.facade.OrderFacade;
import com.elend.gate.order.facade.POrderStatusRecordFacade;
import com.elend.gate.order.facade.PWithdrawRequestFacade;
import com.elend.gate.order.model.PWithdrawRequestPO;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;

@Service
public class WithdrawServiceImpl implements WithdrawService {
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
    private SystemConfig systemConfig;
    @Autowired
    private POrderStatusRecordFacade pOrderStatusRecordFacade;
    @Autowired
    private BalanceFacade balanceFacade;
    @Autowired
    private PWithdrawRequestFacade pWithdrawRequestFacade;
    
    
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
        
        if(!partnerResult.isSuccess()) {
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
