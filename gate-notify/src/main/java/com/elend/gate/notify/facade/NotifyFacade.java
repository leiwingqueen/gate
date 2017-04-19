package com.elend.gate.notify.facade;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.notify.service.NotifyService;

@Component
public class NotifyFacade {
    @Autowired
    private NotifyService notifyService;
    /**
     * 消息通知入队
     * @param orderId--网关订单号
     * @param partner--接入方
     * @param partnerOrderId--接入方订单号
     * @param params--请求参数 url param格式
     * @param notifyUrl--点对点通知url
     * @param executeTime--发送时间
     * @param retryTime--发送次数
     */
    public void addQueue(String orderId,PartnerConstant partner,String partnerOrderId,
            String params,String notifyUrl,Date executeTime,int retryTime){
        notifyService.addQueue(orderId, partner, 
                               partnerOrderId, params, notifyUrl, executeTime,
                               retryTime);
    }
}
