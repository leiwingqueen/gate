package com.elend.gate.reconcile.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.constant.WithdrawStatus;
import com.elend.gate.channel.facade.WithdrawChannelFacade;
import com.elend.gate.channel.facade.vo.WithdrawCallbackData;
import com.elend.gate.order.facade.PWithdrawRequestFacade;
import com.elend.gate.order.model.PWithdrawRequestPO;
import com.elend.gate.reconcile.constant.SystemConstant;
import com.elend.gate.reconcile.service.OrderCheckService;
import com.elend.p2p.msgclient.facade.EmailSender;
import com.elend.p2p.util.DateUtil;

/**
 * 提现对账
 * @author mgt
 *
 */
@Service
public class WithdrawOrderCheckServiceImpl implements OrderCheckService {
    private static Logger logger = LoggerFactory.getLogger(WithdrawOrderCheckServiceImpl.class);

    @Autowired
    private PWithdrawRequestFacade pWithdrawRequestFacade;
    
    @Autowired
    private WithdrawChannelFacade  withdrawChannelFacade;

    @Autowired
    private EmailSender emailSender;
    
    /**分页的大小*/
    private static final int SIZE = 100;
    
    /**总订单数据*/
    private int orderNum = 0; 
    /**对账成功数量*/
    private int equalNum = 0; 
    /**成功的订单数量*/
    private int successNum = 0; 
    /**失败总数*/
    private int failNum = 0;        
    /**处理中的订单数量*/
    private int handingNum = 0; 
    /**不平的订单数量*/
    private int notEqualNum = 0; 
    /**当前页*/
    private int pageNum = 1;
    /**对账失败明细*/
    private List<Map<String, String>> notEqualList = new ArrayList<>();
    
    private void addNotEqual(String platformOrderId, String platformStatus, Date time, String channelStatus, String channel, String msg, Date createTime) {
        notEqualNum ++;
        logger.info("对账失败订单数量：{}", notEqualNum);
        
        Map<String, String> map = new HashMap<>();
        map.put("platformOrderId", platformOrderId);
        map.put("platformStatus", platformStatus);
        map.put("time", DateUtil.timeToStr(time, DateUtil.DATE_FORMAT_PATTEN));
        map.put("channelStatus", channelStatus);
        map.put("channel", channel);
        map.put("msg", msg);
        map.put("createTime", DateUtil.timeToStr(createTime, DateUtil.DATE_FORMAT_PATTEN));
        
        notEqualList.add(map);
    }
    
    @Override
    public void check(Date begin, Date end) {
        String startDate = DateUtil.timeToStr(new Date(), DateUtil.DATE_FORMAT_PATTEN);
        logger.info("提现对账开始，对账开始时间：{}", startDate);
        
        logger.info("对账的订单开始时间：{}", DateUtil.timeToStr(begin, DateUtil.DATE_FORMAT_PATTEN));
        logger.info("对账的订单结束时间：{}", DateUtil.timeToStr(end, DateUtil.DATE_FORMAT_PATTEN));
        
        //对指定时间内的订单进行对账
        while(true) {
            logger.info("第{}页对账开始", pageNum);
            List<PWithdrawRequestPO> list = pWithdrawRequestFacade.listByCreateTime(begin, end, pageNum++, SIZE);
            logger.info("本页共{}条数据", list.size());
            
            if(list.size() < 1) {
                break;
            }
            orderNum += list.size();
            
            for(PWithdrawRequestPO order : list) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    logger.error("", e.getMessage());
                }
                
                logger.info("订单号：{}， 第三方对账开始，渠道：{}", order.getOrderId(), order.getChannel());
                
                WithdrawChannel channel;

                try {
                    channel = WithdrawChannel.from( order.getChannel());
                } catch (Exception e) {
                    logger.info("订单：{}， 对账失败，无法识别渠道：{}", order.getChannel());
                    addNotEqual(order.getOrderId(), WithdrawStatus.from(order.getStatus()).name(), order.getCreateTime(), "", "", "无法识别渠道：" + order.getChannel() + "，没有发起第三方的请求", order.getCreateTime());
                    continue;
                }
                
                PartnerResult<WithdrawCallbackData> rs = null;
                try {
                    rs = withdrawChannelFacade.withdrawSingleQuery(channel, order.getOrderId());
                } catch (Exception e) {
                    logger.error("第三方查询时异常，订单号：" + order.getOrderId(), e);
                    logger.info("订单：{}， 对账失败，第三方查询失败，原因：{}", order.getOrderId(), "第三方请求时异常");
                    addNotEqual(order.getOrderId(), WithdrawStatus.from(order.getStatus()).name(), order.getCreateTime(), "", order.getChannel(), "第三方查询失败，原因：第三方查询时异常", order.getCreateTime());
                    continue;
                }
                
                WithdrawCallbackData queryData = rs.getObject();
                
                if(!rs.isSuccess()) {
                    logger.info("订单：{}， 对账失败，第三方查询失败，原因：{}", order.getOrderId(), rs.getMessage());
                    addNotEqual(order.getOrderId(), WithdrawStatus.from(order.getStatus()).name(), order.getCreateTime(), "", order.getChannel(), "第三方查询失败，原因：" + rs.getMessage(), order.getCreateTime());
                    continue;
                }
                
                
                if(queryData.getWithdrawStatus() == WithdrawStatus.from(order.getStatus())) { //对账成功
                    logger.info("订单号：{}, 对账成功, 状态：{}， 渠道订单号：{}", order.getOrderId(), WithdrawStatus.from(order.getStatus()).name(), channel.name());
                    equalNum ++;
                    logger.info("对账成功订单数量：{}", equalNum);
                    if(WithdrawStatus.from(order.getStatus()) == WithdrawStatus.SUCCESS) {
                        successNum ++;
                        logger.info("成功订单数量:{}" + successNum);
                    } else if(WithdrawStatus.from(order.getStatus()) == WithdrawStatus.APPLYING) {
                        handingNum ++;
                        logger.info("处理中订单数量:{}" + handingNum);
                    } else {
                        failNum ++;
                        logger.info("失败订单数量:{}" + failNum);
                    }
                } else {  //对账失败
                    logger.info("订单：{}， 对账失败，平台状态：{}， 第三方查询状态：{}", order.getOrderId(), WithdrawStatus.from(order.getStatus()).name(), queryData.getWithdrawStatus().getDesc());
                    addNotEqual(order.getOrderId(), WithdrawStatus.from(order.getStatus()).name(), order.getCreateTime(), queryData.getWithdrawStatus().name(), channel.name(), "", order.getCreateTime());
                }
            }
        }
        logger.info("====================================================对账结果=========================================================");
        logger.info("本次对账的订单总数：{}", orderNum);
        logger.info("对账成功的订单数量：{}, 其中成功：{}， 失败：{}， 处理中：{}", equalNum, successNum, failNum, handingNum);
        logger.info("对账失败的订单数量：{}", notEqualNum);
        logger.info("对账失败的订单明细：{}", notEqualList);
        String endTime = DateUtil.timeToStr(new Date(), DateUtil.DATE_FORMAT_PATTEN);
        logger.info("对账结束时间：{}", endTime);
        
        StringBuilder sb = new StringBuilder();
        sb.append("<br/>");
        sb.append("<br/>");
        sb.append("本次对账的开始时间：" + startDate + "<br/>");
        sb.append("本次对账的结束时间：" + endTime + "<br/>");
        sb.append("本次对账的订单开始时间：" + DateUtil.timeToStr(begin, DateUtil.DATE_FORMAT_PATTEN) + "<br/>");
        sb.append("本次对账的订单订单结束：" + DateUtil.timeToStr(end, DateUtil.DATE_FORMAT_PATTEN) + "<br/>");
        sb.append("本次对账的订单总数：" + orderNum + "<br/>");
        sb.append("对账成功的订单数量：" + equalNum + ", 其中成功：" + successNum + "， 失败：" + failNum + "， 处理中：" + handingNum + "<br/>");
        sb.append("对账失败的订单数量：" + notEqualNum + "<br/>");
        sb.append("对账失败的订单明细：<br/>");
        sb.append("<br/>");
        sb.append("<br/>");
        sb.append("<table style='width: 1500px;' border='1'>");
        sb.append("<thead>");
        sb.append("<tr>");
        sb.append("<td>序号</td>");
        sb.append("<td>平台订单号</td>");
        sb.append("<td>平台状态</td>");
        sb.append("<td>渠道状态</td>");
        sb.append("<td>支付渠道</td>");
        sb.append("<td>订单创建时间</td>");
        sb.append("<td>描述信息</td>");
        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody>");
        int i = 1;
        for(Map<String, String> row : notEqualList) {
                sb.append("<tr>");
                sb.append("<td>" + (i++) + "</td>");
                sb.append("<td>" + StringUtils.trimToEmpty(row.get("platformOrderId")) + "</td>");
                sb.append("<td>" + StringUtils.trimToEmpty(row.get("platformStatus")) + "</td>");
                sb.append("<td>" + StringUtils.trimToEmpty(row.get("channelStatus")) + "</td>");
                sb.append("<td>" + StringUtils.trimToEmpty(row.get("channel")) + "</td>");
                sb.append("<td>" + StringUtils.trimToEmpty(row.get("createTime")) + "</td>");
                sb.append("<td>" + StringUtils.trimToEmpty(row.get("msg")) + "</td>");
                sb.append("</tr>");
        }
        sb.append("<tbody>");
        sb.append("</table>");
        sb.append("<br/>");
        sb.append("<br/>");
        sb.append("<br/>");
        sb.append("<br/>");
        sb.append("<br/>");
        
        logger.info("发送邮件：{}", SystemConstant.RECONCILE_SEND_EMAIL);
        logger.info("邮件内容：{}", sb.toString());
        
        String subject = "提现对账结果";
        if(notEqualNum > 0) {
            subject = "***" + subject;
        }
        
        //发送邮件
        emailSender.send("", SystemConstant.RECONCILE_SEND_EMAIL, "", subject, sb.toString());
        
        //System.out.println(sb.toString());
    }
}



























