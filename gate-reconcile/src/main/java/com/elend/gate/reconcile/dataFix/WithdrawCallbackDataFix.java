package com.elend.gate.reconcile.dataFix;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.constant.WithdrawStatus;
import com.elend.gate.channel.facade.vo.WithdrawCallbackData;
import com.elend.gate.channel.service.impl.WithdrawUmbpayChannel;
import com.elend.gate.order.facade.OrderFacade;

/**
 * 提现补单
 * @author liyongquan 2016年1月25日
 *
 */
public class WithdrawCallbackDataFix {
    private static Logger logger = LoggerFactory.getLogger(WithdrawCallbackDataFix.class);
    public static void main(String[] args) {
        logger.info("进入提现补单开始...");
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("classpath*:config/spring/*.xml");
        try{
            OrderFacade orderFacade = (OrderFacade) context.getBean("orderFacade");
            WithdrawUmbpayChannel withdrawUmbpayChannel = (WithdrawUmbpayChannel) context.getBean("withdrawUmbpayChannel");
            BigDecimal amount=new BigDecimal("155000");
            String orderId="160125104830869";
            
            WithdrawCallbackData backData=new WithdrawCallbackData();
            backData.setAmount(amount);
            backData.setCallbackStr("SUCCESS");
            backData.setCallbackTime(new Date());
            backData.setChannelOrderId("CF2000111423160125104830869");
            backData.setFee(withdrawUmbpayChannel.feeCalculate(amount));
            backData.setNotify(true);
            backData.setOrderId(orderId);
            backData.setWithdrawStatus(WithdrawStatus.SUCCESS);
            orderFacade.withdrawCallback(true, WithdrawChannel.UMBPAY_WITHDRAW, backData);
        }catch(Throwable e){
            logger.error("对账异常退出："+ e.getMessage(),e);
        }finally{
            logger.info("提现补单结束...，结束时间：{}", new Date());
            context.close();
            System.exit(0);
        }
    }
}
