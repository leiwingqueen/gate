package com.elend.gate.notify.service.impl;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.elend.gate.channel.facade.PartnerFacade;
import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.notify.NextQueryTimeStategy;
import com.elend.gate.notify.facade.NotifyFacade;
import com.elend.gate.notify.service.PayAgentCallbackService;
import com.elend.gate.order.facade.OrderFacade;
import com.elend.gate.order.facade.PPayAgentApplyRequestFacade;
import com.elend.gate.order.facade.PPayAgentAuthRequestFacade;
import com.elend.gate.order.facade.PPayAgentChargeRequestFacade;
import com.elend.gate.order.vo.PPayAgentApplyRequestVO;
import com.elend.gate.order.vo.PPayAgentAuthRequestSearchVO;
import com.elend.gate.order.vo.PPayAgentAuthRequestVO;
import com.elend.gate.order.vo.PPayAgentChargeRequestSearchVO;
import com.elend.gate.order.vo.PPayAgentChargeRequestVO;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;
import com.elend.p2p.util.DateUtil;
import com.elend.pay.agent.sdk.constant.AgentStatus;
import com.elend.pay.agent.sdk.constant.PayAgentChannel;
import com.elend.pay.agent.sdk.facade.UmbpayAgentFacade;
import com.elend.pay.agent.sdk.vo.UmbpayAgentVO;
import com.elend.pay.agent.sdk.vo.UmbpaySignWhiteListOneVO;

@Service("payAgentCallbackServiceImpl")
public class PayAgentCallbackServiceImpl implements PayAgentCallbackService {
    
    private final static Logger logger = LoggerFactory.getLogger(PayAgentCallbackServiceImpl.class);
    
    @Autowired
    private OrderFacade orderFacade;
    
    @Autowired
    private PartnerFacade partnerFacade;
    
    @Autowired
    private NotifyFacade notifyFacade;
    
    @Autowired
    private UmbpayAgentFacade umbpayAgentFacade;
    
    @Autowired
    @Qualifier("simpleNextQueryTimeStategy")
    private NextQueryTimeStategy nextQueryTimeStategy;
    
    @Autowired
    private PPayAgentChargeRequestFacade pPayAgentChargeRequestFacade;
    
    @Autowired
    private PPayAgentAuthRequestFacade pPayAgentAuthRequestFacade;
    
    @Autowired
    private PPayAgentApplyRequestFacade pPayAgentApplyRequestFacade;

    @Override
    public Result<String> queryAndCallback() {
        
        queryAuthAndCallback();
        queryChargeAndCallback();
        
        return new Result<>(ResultCode.SUCCESS);
    }
    
    /**
     * 查询待收状态并回调P2P
     * @return
     */
    private Result<String> queryChargeAndCallback() {
        Date now = new Date();
        Date startTime = DateUtil.getDate(now, -7, 0, 0, 0);
        logger.info("待收查询第三方状态并回调P2P开始, now:{}, startTime:{}", now, startTime);
        
        int page = 1;
        
        //查询待收鉴权请求中状态的请求记录（查询7天以内的请求，并按照创建时间倒叙排序）
        PPayAgentChargeRequestSearchVO vo = new PPayAgentChargeRequestSearchVO();
        vo.setStatus(AgentStatus.RECEIVE.getStatus());
        vo.setStartTime(startTime);
        vo.setSize(1000); 
        vo.setNextQueryTime(now);
        
        Result<PageInfo<PPayAgentChargeRequestVO>> list = null;
        
        int num = 0;
        
        do {
            vo.setPage(page);
            list = pPayAgentChargeRequestFacade.list(vo);
            
            //循环处理
            for(PPayAgentChargeRequestVO requestVo : list.getObject().getList()) {
                handlePayAgentCharge(requestVo);
            }
            
            page ++;
            if(page > 50) {
                logger.info("代收查询死循环，直接结束，page:{}", page);
                return new Result<>(ResultCode.FAILURE, null, "代收查询死循环");
            }
            
            num += list.getObject().getList().size();
            
        } while (list.getObject().getList().size() >= 1000);
        
        logger.info("查询待收的状态并回调P2P本轮处理结束, 处理订单数量:{}", num);
        
        return new Result<>(ResultCode.SUCCESS);
    }
    
    /**
     * 查询并更新代收的请求订单的状态
     * @param requestVo
     */
    private void handlePayAgentCharge(PPayAgentChargeRequestVO requestVo) {
        try {
            logger.info("orderId：{}， 请求开始处理...", requestVo.getOrderId());
            
            Result<UmbpayAgentVO> requestResult = umbpayAgentFacade.userQueryAgent(requestVo.getChannelOrderId(), requestVo.getAmount());
            logger.info("第三方查询结果, requestResult:{}", requestResult);
            
            //更新下次查询时间和查询次数
            int seconds = nextQueryTimeStategy.getSeconds(requestVo.getQueryNum());
            pPayAgentChargeRequestFacade.updateNextQueryTime(seconds, requestVo.getOrderId());

            //第三方查询不成功，订单不更新
            if(!requestResult.isSuccess() || requestResult.getObject().getSignState() == AgentStatus.RECEIVE){
                logger.info("查询失败或者不是最终结果，不更新订单, orderId:{}, partnerOrderID:{}", requestVo.getOrderId(), requestVo.getPartnerOrderId());
                return;
            }
            
            Result<String> callbackResult = orderFacade.PayAgentChargeCallback(requestResult.isSuccess(), PayAgentChannel.from(requestVo.getChannel()), requestVo.getOrderId(), requestResult.getObject());
            logger.info("代扣回调订单处理orderId:{}, result:{}", requestVo.getOrderId(), callbackResult);
            
            //生成回调的form表单(约定和P2P通讯格式)
            RequestFormData form = partnerFacade.getPayAgentChargeCallbackForm(PayAgentChannel.from(requestVo.getChannel()), requestVo.getOrderId(), requestVo.getNotifyUrl(), requestVo.getAmount(), requestResult.getObject().getBussflowno(), requestVo.getPartnerId(), requestVo.getPartnerOrderId(), requestResult);
            
            //写入通知队列
            String paramUrl = genUrlParamFormat(form.getParams());
            notifyFacade.addQueue(requestVo.getOrderId(), PartnerConstant.from(requestVo.getPartnerId()), requestVo.getPartnerOrderId(), paramUrl, requestVo.getNotifyUrl(), new Date(), 1);
            
            logger.info("代收, 写入通知队列成功, 记录处理结束， orderId:{}, partnerOrderID:{}", requestVo.getOrderId(), requestVo.getPartnerOrderId());

        } catch (Exception e) {
            logger.error("代收回调处理异常, orderId:" + requestVo.getOrderId(), e);
        }
    }

    /**
     * 查询待收鉴权的状态并回调P2P
     * @return
     */
    private Result<String> queryAuthAndCallback() {
        
        Date now = new Date();
        Date startTime = DateUtil.getDate(now, -7, 0, 0, 0);
        logger.info("待收鉴权查询第三方状态并回调P2P开始, now:{}, startTime:{}", now, startTime);
        
        int page = 1;
        
        //查询待收鉴权请求中状态的请求记录（查询7天以内的请求，并按照创建时间倒叙排序）
        PPayAgentAuthRequestSearchVO vo = new PPayAgentAuthRequestSearchVO();
        vo.setStatus(AgentStatus.RECEIVE.getStatus());
        vo.setStartTime(startTime);
        vo.setSize(1000); 
        vo.setNextQueryTime(now);
        
        Result<PageInfo<PPayAgentAuthRequestVO>> list = null;
        
        int num = 0;
        
        do {
            vo.setPage(page);
            list = pPayAgentAuthRequestFacade.list(vo);
            
            //循环处理
            for(PPayAgentAuthRequestVO requestVo : list.getObject().getList()) {
                handlePayAgentAuth(requestVo);
            }
            
            page ++;
            if(page > 50) {
                logger.info("代收鉴权查询死循环，直接结束，page:{}", page);
                return new Result<>(ResultCode.FAILURE, null, "代收鉴权查询死循环");
            }
            
            num += list.getObject().getList().size();
            
        } while (list.getObject().getList().size() >= 1000);
        
        logger.info("查询待收鉴权的状态并回调P2P本轮处理结束, 处理订单数量:{}", num);
        
        return new Result<>(ResultCode.SUCCESS);
    }
    
    /**
     * 查询并更新鉴权请求订单的状态
     * @param requestVo
     */
    private void handlePayAgentAuth(PPayAgentAuthRequestVO requestVo) {
        try {
            
            //查询代收鉴权申请，获取第三方查询的参数
            PPayAgentApplyRequestVO pPayAgentApplyRequestVO = pPayAgentApplyRequestFacade.getByOrderId(requestVo.getApplyOrderId());
            
            if(pPayAgentApplyRequestVO == null) {
                logger.info("找不到代收鉴权申请的记录, orderId:{}, applyOrderId:{}", requestVo.getOrderId(), requestVo.getApplyOrderId());
                return;
            }
            
            logger.info("orderId：{}， 请求开始处理...", requestVo.getOrderId());
            
            Result<UmbpaySignWhiteListOneVO> requestResult = umbpayAgentFacade.userQuerySign(pPayAgentApplyRequestVO.getCardNo(), pPayAgentApplyRequestVO.getRealName(), pPayAgentApplyRequestVO.getIdNo(), pPayAgentApplyRequestVO.getCellphone(), pPayAgentApplyRequestVO.getAmount());
            logger.info("第三方查询结果, requestResult:{}", requestResult);
            
            //更新下次查询时间和查询次数
            int seconds = nextQueryTimeStategy.getSeconds(requestVo.getQueryNum());
            pPayAgentAuthRequestFacade.updateNextQueryTime(seconds, requestVo.getOrderId());
            
            //查询失败，或者结果是请求中，不更新订单状态
            if(!requestResult.isSuccess() || requestResult.getObject().getSignState() == AgentStatus.RECEIVE) {
                logger.info("orderId:{}, 查询失败或者不是最终结果， 不更新订单状态，记录处理结束", requestVo.getOrderId());
                return;
            }
            
            Result<String> callbackResult = orderFacade.PayAgentCheckCodeCallback(requestResult.isSuccess(), PayAgentChannel.from(requestVo.getChannel()), requestVo.getOrderId(), requestResult.getObject());
            logger.info("回调订单处理orderId:{}, result:{}", requestVo.getOrderId(), callbackResult);
            
            //生成回调的form表单(约定和P2P通讯格式)
            RequestFormData form = partnerFacade.getPayAgentAuthCallbackForm(requestVo.getChannel(), requestVo.getOrderId(), requestVo.getNotifyUrl(), requestVo.getAmount(), requestVo.getPartnerId(), requestVo.getPartnerOrderId(), requestResult);
            
            //写入通知队列
            String paramUrl = genUrlParamFormat(form.getParams());
            notifyFacade.addQueue(requestVo.getOrderId(), PartnerConstant.from(requestVo.getPartnerId()), requestVo.getPartnerOrderId(), paramUrl, requestVo.getNotifyUrl(), new Date(), 1);
            
            logger.info("待收鉴权, 写入通知队列成功, 记录处理结束， orderId:{}, partnerOrderID:{}", requestVo.getOrderId(), requestVo.getPartnerOrderId());
        } catch (Exception e) {
            logger.error("鉴权回调处理异常, orderId:" + requestVo.getOrderId(), e);
        }
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
