package com.elend.gate.notify.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elend.gate.channel.facade.PartnerFacade;
import com.elend.gate.channel.facade.PayChannelFacade;
import com.elend.gate.channel.facade.WithdrawChannelFacade;
import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.notify.QueueStrategy;
import com.elend.gate.notify.constant.NotifyStatus;
import com.elend.gate.notify.mapper.NNotifyLogMapper;
import com.elend.gate.notify.mapper.NQueueMapper;
import com.elend.gate.notify.mapper.NWithdrawQueueMapper;
import com.elend.gate.notify.model.NNotifyLogPO;
import com.elend.gate.notify.model.NQueuePO;
import com.elend.gate.notify.service.NotifyService;
import com.elend.gate.order.facade.OrderFacade;
import com.elend.gate.util.GateHttpUtil;
import com.elend.gate.util.HttpResult;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;
import com.elend.p2p.gson.JSONUtils;

@Service("notifyServiceImpl")
public class NotifyServiceImpl implements NotifyService{
    private final static Logger logger = LoggerFactory.getLogger(NotifyServiceImpl.class);
    
    /**
     * 接入方接收成功返回
     */
    private static final String SUCCESS_CALLBACK="SUCCESS";
    @Autowired
    private NQueueMapper queueMapper;
    @Autowired
    @Qualifier("modQueueStrategy")
    private QueueStrategy strategy;
    @Autowired
    private NNotifyLogMapper notifyLogMapper;
    @Autowired
    private NWithdrawQueueMapper withdrawQueueMapper;
    
    @Autowired
    private OrderFacade orderFacade;
    
    @Autowired
    private PayChannelFacade payChannelFacade;
    
    @Autowired
    private PartnerFacade partnerFacade;
    
    @Autowired
    private WithdrawChannelFacade withdrawChannelFacade;
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addQueue(String orderId, PartnerConstant partner,
            String partnerOrderId, String params, String notifyUrl,
            Date executeTime,int retryTime) {
        //获取写入的队列id
        int queueIndex=strategy.getQueueIndex(orderId);
        Date now=new Date();
        NQueuePO queue=new NQueuePO();
        queue.setExecuteTime(executeTime);
        queue.setCreateTime(now);
        queue.setLastModify(now);
        queue.setParams(params);
        queue.setNotifyUrl(notifyUrl);
        queue.setOrderId(orderId);
        queue.setPartnerId(partner.name());
        queue.setPartnerOrderId(partnerOrderId);
        queue.setQueueIndex(queueIndex);
        queue.setRetryTimes(retryTime);
        int affectRow=queueMapper.insert(queue);
        if(affectRow<=0){
            logger.error("写入通知队列失败...queue:{}",queue.toString());
            return;
        }
        logger.info("写入通知队列成功...queue:{}",queue.toString());
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<String> notify(NQueuePO queue) {
        logger.info("开始进行点对点通知...queue:{}",queue.toString());
        Map<String,String> paramMap=getParamMap(queue.getParams());
        HttpResult httpResult=GateHttpUtil.postRequest(queue.getNotifyUrl(), paramMap);
        NotifyStatus status=httpResult.getHttpResponseCode()==200&&SUCCESS_CALLBACK.equals(httpResult.getHttpBody())?NotifyStatus.SUCCESS:NotifyStatus.FAIL;
        Date now=new Date();
        NNotifyLogPO po=new NNotifyLogPO();
        po.setExecuteTime(now);
        po.setCreateTime(queue.getCreateTime());
        po.setLastModify(now);
        po.setParams(queue.getParams());
        po.setStatus(status.getValue());
        po.setHttpResultCode(httpResult.getHttpResponseCode()+"");
        String result=httpResult.getHttpBody().length()>255?httpResult.getHttpBody().substring(0, 255):httpResult.getHttpBody();
        po.setResult(result);
        po.setQueueIndex(queue.getQueueIndex());
        po.setOrderId(queue.getOrderId());
        po.setPartnerId(queue.getPartnerId());
        po.setPartnerOrderId(queue.getPartnerOrderId());
        po.setRetryTimes(queue.getRetryTimes());
        int affectRow= notifyLogMapper.insert(po);
        if(affectRow<=0){
            logger.error("写入通知日志失败...NNotifyLogPO:{}",po.toString());
            return new Result<String>(ResultCode.FAILURE, null,"写入通知日志失败");
        }
        if(status==NotifyStatus.FAIL){
            logger.error("点对点通知失败...queue:{}",queue.toString());
            return new Result<String>(ResultCode.FAILURE,null,"通知失败");
        }
        logger.info("点对点通知成功...queue:{}",queue.toString());
        return new Result<String>(ResultCode.SUCCESS,queue.getOrderId());
    }    
    /**
     * 获取参数转换map
     * @param params
     * @return
     */
    private Map<String,String> getParamMap(String params){
        logger.info("参数转换,params:{}",params);
        Map<String,String> map=new HashMap<String, String>();
        for(String item:params.split("&")){
            if(!StringUtils.isBlank(item)&&item.split("=").length>=2){
                map.put(item.split("=")[0], item.split("=")[1]);
            }
        }
        logger.info("参数转换成功,前,params:{},后,paramMap:{}",params,JSONUtils.toJson(map, false));
        return map;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void retry(NQueuePO queue, int timeInterval) {
        //写入同一个队列，方便查询
        Calendar cal=Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.SECOND, timeInterval);
        queue.setExecuteTime(cal.getTime());
        Date now=new Date();
        queue.setLastModify(now);
        queue.setRetryTimes(queue.getRetryTimes()+1);
        int affectRow=queueMapper.insert(queue);
        if(affectRow<=0){
            logger.error("写入通知队列失败...queue:{}",queue.toString());
            return;
        }
        logger.info("写入通知队列成功...queue:{}",queue.toString());
    }
    @Override
    public List<NQueuePO> listQueue(int queueIndex, int limit) {
        List<NQueuePO> list= queueMapper.listQueue(queueIndex, limit, new Date());
        if(list==null)return new ArrayList<NQueuePO>();
        return list;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteQueue(int queueIndex, int id) {
        queueMapper.delete(queueIndex, id);
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
