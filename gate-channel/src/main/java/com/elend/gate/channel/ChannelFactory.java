package com.elend.gate.channel;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.service.PayChannelService;
import com.elend.p2p.spring.SpringContextUtil;

/**
 * 支付渠道工厂
 * @author liyongquan 2015年5月21日
 *
 */
public class ChannelFactory {
    private final static Logger logger = LoggerFactory.getLogger(ChannelFactory.class);
    private static Map<String,PayChannelService> map=null;
    /**
     * 获取beanMap
     * @return
     */
    private static Map<String,PayChannelService> getBeanMap(){
        if(map==null){
            logger.info("初始化beanMap...");
            map=new HashMap<String, PayChannelService>();
            String[] beanNames=SpringContextUtil.getContext().getBeanNamesForType(PayChannelService.class);
            if(beanNames!=null&&beanNames.length>0){
                for(String beanName:beanNames){
                    PayChannelService channel=(PayChannelService)SpringContextUtil.getBean(beanName);
                    map.put(channel.getChannelId().name(),channel);
                }
            }
        }
        return map;
    }
    public static PayChannelService getPayChannel(ChannelIdConstant channelId){
        return getBeanMap().get(channelId.name());
    }
}
