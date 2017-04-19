package com.elend.gate.reconcile.init;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.elend.gate.order.facade.OrderFacade;
import com.elend.p2p.spring.SpringContextUtil;
import com.elend.p2p.util.DateUtil;

/**
 * 账本初始化
 * 
 * @author mgt
 */
public class BalanceInit {

    /**
     * 红包统计初始化
     * 
     * @param args
     */
    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger(BalanceInit.class);
        
        
        if(args == null || args.length < 1) {
            log.info("请输入初始化的时间点");
            return;
        }

        log.info("args:{}", args[0]);
        
        Date startTime = DateUtil.strToTime(args[0], DateUtil.DATE_FORMAT_PATTEN);
        if(startTime == null) {
            log.info("时间格式不正确");
            return;
        }

        String[] xmlCfg = new String[] { "classpath*:config/spring/*.xml" };
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                                                                                    xmlCfg);
        SpringContextUtil.setContext(context);

        try {
            OrderFacade orderFacade = (OrderFacade) SpringContextUtil.getContext().getBean("orderFacade");

            log.info("修复开始");

            orderFacade.repairBalanceAndLog(startTime);

            log.info("修复结束");
        } catch (Exception e) {
            log.error("修复过程异常:{}", e);
        } finally {
            context.close();
            System.exit(0);
        }
    }
}
