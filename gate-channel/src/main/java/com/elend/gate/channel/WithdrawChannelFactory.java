package com.elend.gate.channel;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.service.WithdrawChannelService;
import com.elend.p2p.spring.SpringContextUtil;

/**
 * 提现渠道工厂
 * @author mgt
 *
 */
public class WithdrawChannelFactory {
    private final static Logger logger = LoggerFactory.getLogger(WithdrawChannelFactory.class);
    private static Map<String,WithdrawChannelService> map=null;
    /**
     * 获取beanMap
     * @return
     */
    private static Map<String,WithdrawChannelService> getBeanMap(){
        if(map==null){
            logger.info("初始化beanMap...");
            map=new HashMap<String, WithdrawChannelService>();
            String[] beanNames=SpringContextUtil.getContext().getBeanNamesForType(WithdrawChannelService.class);
            if(beanNames!=null&&beanNames.length>0){
                for(String beanName:beanNames){
                    WithdrawChannelService channel=(WithdrawChannelService)SpringContextUtil.getBean(beanName);
                    map.put(channel.getChannelId().name(),channel);
                }
            }
        }
        return map;
    }
    public static WithdrawChannelService getPayChannel(WithdrawChannel channelId){
        return getBeanMap().get(channelId.name());
    }
}
