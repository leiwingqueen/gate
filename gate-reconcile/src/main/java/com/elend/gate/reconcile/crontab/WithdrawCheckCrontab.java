package com.elend.gate.reconcile.crontab;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.elend.gate.reconcile.service.OrderCheckService;
import com.elend.gate.util.LogHelper;
import com.elend.p2p.util.DateUtil;

/**
 * 代付对账
 * 
 * @author mgt
 */
@Component
public class WithdrawCheckCrontab {
     private static Logger logger = LoggerFactory.getLogger(WithdrawCheckCrontab.class);
        
    public static void main(String[] args) {
        logger.info("进入提现对账main");
        LogHelper.initLog4j("config/log/logback_withdraw_check.xml");
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("classpath*:config/spring/*.xml");
        try{
            OrderCheckService service = (OrderCheckService) context.getBean("withdrawOrderCheckServiceImpl");
            Date now = new Date();
            Date begin = DateUtil.getDate(now, -5, -6, -10, 0); //每六小时对账一次，多对账十分钟
            service.check(begin, now);
        }catch(Throwable e){
            logger.error("对账异常退出："+ e.getMessage(),e);
        }finally{
            logger.info("本次对账结束，结束时间：{}", new Date());
            context.close();
            System.exit(0);
        }
    }
}
