package com.elend.gate.notify.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.facade.PartnerFacade;
import com.elend.gate.channel.facade.WithdrawChannelFacade;
import com.elend.gate.channel.facade.vo.PartnerWithdrawData;
import com.elend.gate.channel.facade.vo.WithdrawCallbackData;
import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.notify.facade.NotifyFacade;
import com.elend.gate.notify.mapper.NWithdrawQueueMapper;
import com.elend.gate.notify.model.NWithdrawQueuePO;
import com.elend.gate.notify.service.WithdrawQueueService;
import com.elend.gate.order.facade.OrderFacade;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;

@Service("withdrawQueueServiceImpl")
public class WithdrawQueueServiceImpl implements WithdrawQueueService{
    
    private final static Logger logger = LoggerFactory.getLogger(WithdrawQueueServiceImpl.class);

    @Autowired
    private NWithdrawQueueMapper withdrawQueueMapper;
    
    @Autowired
    private OrderFacade orderFacade;
    
    @Autowired
    private PartnerFacade partnerFacade;
    
    @Autowired
    private WithdrawChannelFacade withdrawChannelFacade;
    
    @Autowired
    private NotifyFacade notifyFacade;
    
    @Override
    public int addWithdrawQueue(NWithdrawQueuePO po) {
        int row = withdrawQueueMapper.insert(po);
        return row;
    }
    
    @Override
    public List<NWithdrawQueuePO> listWithdrawQueue(int limit) {
        List<NWithdrawQueuePO> list= withdrawQueueMapper.list(limit, new Date());
        if(list==null)
            return new ArrayList<NWithdrawQueuePO>();
        return list;
    }
    
    @Override
    //@Transactional(propagation = Propagation.REQUIRED) 不加事务，请求表和订单表的处理在各自的事务中，订单表操作异常，不会导致请求表回滚
    public Result<String> withdrawSingle(NWithdrawQueuePO queue) {
        //1.获取提现的参数
        PartnerWithdrawData data = JSON.parseObject(queue.getParams(), PartnerWithdrawData.class);
        
        //2.写入请求表，更新订单记录表状态
        String orderId = queue.getOrderId();//支付网关订单号
        Result<String> result = orderFacade.sendWithdrawRequest(data, orderId);
        logger.info("插入提现请求订单号result:{}", result);
        //插入请求表失败，直接返回失败
        if(!result.isSuccess()) {
            return new Result<String>(ResultCode.FAILURE, "", result.getMessage());
        }
        //3.发送第三方的提现请求
        PartnerResult<WithdrawCallbackData> partnerResult = withdrawChannelFacade.withdrawSingle(data.getChannel(), orderId, data);
        logger.info("发送第三方提现请求result:{}", partnerResult);
        
        //4.写入订单表和更新请求表
        Result<String> result2 = orderFacade.withdrawCallback(partnerResult.isSuccess(), data.getChannel(), partnerResult.getObject());
        logger.info("提现返回更新订单result:{}", result2);
        
        //回调订单处理不成功，不通知接入方
        if(!result2.isSuccess()){
            return new Result<String>(ResultCode.SUCCESS, null, result.getMessage());
        }
        
        //5.生成回调的form表单(约定和P2P通讯格式)
        RequestFormData form = partnerFacade.getWithdrawCallbackForm(data.getChannel(), data.getPartnerId().name(), data.getPartnerOrderId(), partnerResult, data.getNotifyUrl());
        
        //6.写入通知队列
        String paramUrl = genUrlParamFormat(form.getParams());
        notifyFacade.addQueue(orderId, data.getPartnerId() , data.getPartnerOrderId(), paramUrl, data.getNotifyUrl(), new Date(), 1);

        //没有发生异常，一律认为成功
        return new Result<String>(ResultCode.SUCCESS);
    }
    
    @Override
    public int deleteWithdrawQueue(int id) {
        return withdrawQueueMapper.delete(id);
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
    public NWithdrawQueuePO getWithdrawQueue(PartnerConstant partnerId,
            String partnerOrderId) {
        return withdrawQueueMapper.getByPertnerId(partnerId.name(), partnerOrderId);
    }
}
