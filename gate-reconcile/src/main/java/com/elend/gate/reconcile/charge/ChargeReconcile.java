package com.elend.gate.reconcile.charge;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.ChargeQueryStatus;
import com.elend.gate.channel.constant.PartnerResultCode;
import com.elend.gate.channel.facade.PayChannelFacade;
import com.elend.gate.channel.facade.vo.PartnerSingleQueryChargeData;
import com.elend.gate.order.constant.RequestStatus;
import com.elend.gate.order.facade.PDepositRequestFacade;
import com.elend.gate.order.model.PDepositRequestPO;
import com.elend.gate.order.vo.PDepositRequestSearchVO;
import com.elend.gate.reconcile.constant.SystemConstant;
import com.elend.gate.reconcile.vo.ChannelChargeStat;
import com.elend.gate.util.DateUtils;
import com.elend.p2p.msgclient.facade.EmailSender;
import com.elend.p2p.util.DateUtil;
import java.text.SimpleDateFormat;

/**
 * 充值对账
 * 
 * @author mgt
 */
@Component
public class ChargeReconcile {

    private static Logger logger = LoggerFactory.getLogger(ChargeReconcile.class);

    @Autowired
    private PayChannelFacade payChannelFacade;

    @Autowired
    private PDepositRequestFacade pDepositRequestFacade;

    @Autowired
    private EmailSender emailSender;

    private int orderNum = 0; // 总订单数据

    private int successNum = 0; // 成功的订单数量

    private int failNum = 0; // 失败总数

    private int equalNum = 0; // 对账成功数量

    private int notEqualNum = 0; // 不平的订单数量

    private int handingNum = 0; // 处理中的订单数量

    private int pageNum = 1;
    
    /**分渠道统计*/
    private Map<String, ChannelChargeStat> statMap = new HashMap<>();
    
    private List<Map<String, String>> notEqualList = new ArrayList<>(); // 不平订单的集合

    private static final int SIZE = 100;

    public void checkOrder(Date begin, Date end) {
        String startDate = DateUtil.timeToStr(new Date(),
                                              DateUtil.DATE_FORMAT_PATTEN);
        logger.info("充值对账开始，对账开始时间：{}", startDate);

        PDepositRequestSearchVO vo = new PDepositRequestSearchVO();
        vo.setStartDate(begin);
        vo.setEndDate(end);

        logger.info("对账的订单开始时间：{}",
                    DateUtil.timeToStr(begin, DateUtil.DATE_FORMAT_PATTEN));
        logger.info("对账的订单结束时间：{}",
                    DateUtil.timeToStr(end, DateUtil.DATE_FORMAT_PATTEN));

        while (true) {
            vo.setPage(pageNum);
            vo.setSize(SIZE);
            logger.info("第{}页对账开始", pageNum);
            List<PDepositRequestPO> list = pDepositRequestFacade.list(vo);
            logger.info("本页共{}条数据", list.size());
            if (list.size() < 1) {
                break;
            }
            pageNum++; // 页码++
            orderNum += list.size();

            for (PDepositRequestPO po : list) {

                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    logger.error("", e);
                }

                logger.info("订单号：{}， 对账开始，渠道：{}", po.getOrderId(),
                            po.getPayChannel());
                PartnerResult<PartnerSingleQueryChargeData> rs = null;
                ChannelIdConstant channel = null;
                try {
                    channel = ChannelIdConstant.from(po.getPayChannel());
                } catch (Exception e) {
                    logger.error("订单号：{}, 无法识别渠道类型", po.getOrderId());

                    notEqualNum++;
                    logger.info("对账失败订单数量：{}", notEqualNum);

                    Map<String, String> map = new HashMap<>();
                    map.put("platformOrderId", po.getOrderId());
                    map.put("channelOrderId",
                            StringUtils.trimToEmpty(po.getChannelTradeNo()));
                    map.put("platformStatus",
                            RequestStatus.from((short) po.getStatus()).getDesc());
                    map.put("channelStatus", "无法识别渠道类型:" + po.getPayChannel());
                    map.put("channel", po.getPayChannel());
                    notEqualList.add(map);

                    continue;
                }

                try {
                    rs = payChannelFacade.singleQueryCharge(channel,
                                                            po.getOrderId());
                } catch (Exception e) {
                    logger.error("订单号：{}, 第三方订单查询失败， 失败原因：{}",
                                 po.getOrderId(), e.getMessage());

                    notEqualNum++;
                    logger.info("对账失败订单数量：{}", notEqualNum);

                    Map<String, String> map = new HashMap<>();
                    map.put("platformOrderId", po.getOrderId());
                    map.put("channelOrderId",
                            StringUtils.trimToEmpty(po.getChannelTradeNo()));
                    map.put("platformStatus",
                            RequestStatus.from((short) po.getStatus()).getDesc());
                    map.put("channelStatus", "订单查询失败， 失败原因：" + e.getMessage());
                    map.put("channel", po.getPayChannel());
                    notEqualList.add(map);

                    continue;
                }

                if (PartnerResultCode.SUCCESS.equals(rs.getCode())) { // 查询成功
                    PartnerSingleQueryChargeData queryData = rs.getObject();
                    // 获取最新的请求数据
                    PDepositRequestPO newPo = pDepositRequestFacade.getByOrderId(po.getOrderId());
                    if (newPo == null) {
                        logger.error("查询的记录不存在,订单号:{}", po.getOrderId());
                        continue;
                    }
                    if ((queryData.getStatus() == ChargeQueryStatus.SUCCESS && newPo.getStatus() == RequestStatus.DONE.getValue()) // 成功
                            || (((queryData.getStatus() == ChargeQueryStatus.FAILURE) || (queryData.getStatus() == ChargeQueryStatus.NOT_EXIST)) && newPo.getStatus() == RequestStatus.FAIL.getValue()) // 失败(查询订单不存在也有可能是平台的失败， 平台处理中渠道失败也是对账成功的失败)
                            || (((queryData.getStatus() == ChargeQueryStatus.HANDLING) || (queryData.getStatus() == ChargeQueryStatus.NOT_EXIST) || (queryData.getStatus() == ChargeQueryStatus.FAILURE)) && newPo.getStatus() == RequestStatus.REQUEST.getValue()) // 处理中（查询订单不存在也有可能是品台的处理中）
                    ) { // 对账成功
                        
                        ChannelChargeStat stat = statMap.get(channel.name());
                        if(stat == null) {
                            stat = new ChannelChargeStat(channel);
                            statMap.put(channel.name(), stat);
                        }

                        stat.setEqualNum(stat.getEqualNum() + 1);
                        equalNum++;
                        logger.info("订单号：{}对账成功, 状态：{}， 渠道订单号：{}, channel:{}",
                                    newPo.getOrderId(),
                                    queryData.getStatus(),
                                    newPo.getChannelTradeNo(),
                                    channel.name());
                        logger.info("总对账成功订单数量：{}", equalNum);
                        logger.info("{}对账成功订单数量：{}", channel.name(), stat.getEqualNum());

                        if (queryData.getStatus() == ChargeQueryStatus.SUCCESS) {
                            stat.setSuccessNum(stat.getSuccessNum() + 1);
                            successNum++;
                            logger.info("总成功订单数量:{}", successNum);
                            logger.info("{}成功订单数量:{}", channel.name(), stat.getSuccessNum());
                        } else if (queryData.getStatus() == ChargeQueryStatus.HANDLING) {
                            stat.setHandingNum(stat.getHandingNum() + 1);
                            handingNum++;
                            logger.info("总处理中订单数量:{}", handingNum);
                            logger.info("{}处理中订单数量:{}", channel.name(), stat.getHandingNum());
                        } else {
                            stat.setFailNum(stat.getFailNum() + 1);
                            failNum++;
                            logger.info("总失败订单数量:{}", failNum);
                            logger.info("{}失败订单数量:{}", channel.name(), stat.getFailNum());
                        }
                    } else { // 对账失败
                        notEqualNum++;
                        logger.info("对账失败订单数量：{}", notEqualNum);

                        Map<String, String> map = new HashMap<>();
                        map.put("platformOrderId", newPo.getOrderId());
                        map.put("channelOrderId",
                                StringUtils.trimToEmpty(StringUtils.isNotBlank(newPo.getChannelTradeNo()) ? newPo.getChannelTradeNo()
                                    : queryData.getChannelOrderId()));
                        map.put("platformStatus",
                                RequestStatus.from((short) newPo.getStatus()).getDesc());
                        map.put("channelStatus",
                                queryData.getStatus().getDesc());
                        map.put("channel", newPo.getPayChannel());
                        notEqualList.add(map);
                    }

                } else {
                    logger.error("订单号：{}, 第三方订单查询失败", po.getOrderId());

                    notEqualNum++;
                    logger.info("对账失败订单数量：{}", notEqualNum);

                    Map<String, String> map = new HashMap<>();
                    map.put("platformOrderId", po.getOrderId());
                    map.put("channelOrderId",
                            StringUtils.trimToEmpty(po.getChannelTradeNo()));
                    map.put("platformStatus",
                            RequestStatus.from((short) po.getStatus()).getDesc());
                    map.put("channelStatus",
                            "第三方订单查询失败， 失败原因:" + rs.getMessage());
                    map.put("channel", po.getPayChannel());
                    notEqualList.add(map);
                }
            }
        }

        logger.info("====================================================对账结果=========================================================");
        logger.info("本次对账的订单总数：{}", orderNum);
        logger.info("对账成功的订单数量：{}, 其中成功：{}， 失败：{}， 处理中：{}", equalNum,
                    successNum, failNum, handingNum);
        logger.info("对账失败的订单数量：{}", notEqualNum);
        logger.info("对账失败的订单明细：{}", notEqualList);
        String endTime = DateUtil.timeToStr(new Date(),
                                            DateUtil.DATE_FORMAT_PATTEN);
        logger.info("对账结束时间：{}", endTime);
        logger.info("各渠道stat：{}", statMap);

        StringBuilder sb = new StringBuilder();
        sb.append("<br/>");
        sb.append("<br/>");
        sb.append("对账规则说明<br/>");
        sb.append("<span style='padding-left:50px;'>1.对账成功：</span><br/>");
        sb.append("<span style='padding-left:100px;'>（1）充值成功：渠道状态成功，平台状态成功；</span><br/>");
        sb.append("<span style='padding-left:100px;'>（2）充值失败：渠道状态失败或订单不存在，平台状态失败；渠道状态失败或订单不存在，平台状态处理中；</span><br/>");
        sb.append("<span style='padding-left:100px;'>（3）充值处理中：渠道状态处理中，平台状态处理中；</span><br/>");
        sb.append("<span style='padding-left:50px;'>2.对账失败：</span><br/>");
        sb.append("<span style='padding-left:100px;'>非对账成功的情况，具体参看对账失败明细</span><br/>");
        sb.append("<br/>");
        sb.append("<br/>");
        sb.append("本次对账的开始时间：" + startDate + "<br/>");
        sb.append("本次对账的结束时间：" + endTime + "<br/>");
        sb.append("本次对账的订单开始时间："
                + DateUtil.timeToStr(begin, DateUtil.DATE_FORMAT_PATTEN)
                + "<br/>");
        sb.append("本次对账的订单订单结束："
                + DateUtil.timeToStr(end, DateUtil.DATE_FORMAT_PATTEN)
                + "<br/>");
        sb.append("<br/>");
        sb.append("本次对账的订单总数：" + orderNum + "<br/>");
        sb.append("总对账成功的订单数量：" + equalNum + ", 其中成功：" + successNum + "， 失败："
                + failNum + "， 处理中：" + handingNum + "<br/>");
        //遍历各渠道状态
        for(String key : statMap.keySet()) {
            ChannelChargeStat channelChargeStat = statMap.get(key);
            sb.append(channelChargeStat.getChannel().name() + "渠道对账成功的订单数量：" + channelChargeStat.getEqualNum() + ", 其中成功：" + channelChargeStat.getSuccessNum() + "， 失败："
                    + channelChargeStat.getFailNum() + "， 处理中：" + channelChargeStat.getHandingNum() + "<br/>");
        }
        sb.append("<br/>");
        sb.append("对账失败的订单数量：" + notEqualNum + "<br/>");
        sb.append("对账失败的订单明细：<br/>");
        sb.append("<table style='width: 1200px;' border='1'>");
        sb.append("<thead>");
        sb.append("<tr>");
        sb.append("<td>序号</td>");
        sb.append("<td>网关订单号</td>");
        sb.append("<td>网关状态</td>");
        sb.append("<td>渠道订单号</td>");
        sb.append("<td>渠道状态</td>");
        sb.append("<td>支付渠道</td>");
        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody>");
        int i = 1;
        for (Map<String, String> row : notEqualList) {
            sb.append("<tr>");
            sb.append("<td>" + (i++) + "</td>");
            sb.append("<td>" + row.get("platformOrderId") + "</td>");
            sb.append("<td>" + row.get("platformStatus") + "</td>");
            sb.append("<td>" + row.get("channelOrderId") + "</td>");
            sb.append("<td>" + row.get("channelStatus") + "</td>");
            sb.append("<td>" + row.get("channel") + "</td>");
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

        // 发送邮件
        String subject = "充值对账结果";
        if(notEqualNum > 0) {
            subject = "***" + subject;
        }
        emailSender.send("", SystemConstant.RECONCILE_SEND_EMAIL, "",
                         subject, sb.toString());
    }

    public static void main(String[] args) {
        // 初始化spring容器, 并注入对应的属性
        logger.info("开始初始化spring容器");
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(
                                                                                      "classpath*:config/spring/*.xml");
        try {
            ChargeReconcile chargeReconcile = context.getBean(ChargeReconcile.class);
            logger.info("初始化Spring容器完成");
            // 设置开始对账的时间和结束对账的时间
            Date begin = DateUtils.getDate(new Date(), 0, -6, -10, 0);
			//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//Date begin = simpleDateFormat.parse("2015-09-28 00:00:00");
            Date end = new Date();
			//Date end = simpleDateFormat.parse("2015-09-29 00:00:00");
            chargeReconcile.checkOrder(begin, end);
        } catch (Throwable e) {
            logger.error("对账线程异常退出", e);
        } finally {
            logger.info("本次对账结束，结束时间：{}", new Date());
            context.close();
            System.exit(0);
        }
    }
}
