package com.elend.gate.channel.util.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.mapper.CBankIdConfigMapper;
import com.elend.gate.channel.util.IChannelStrategy;
import com.elend.gate.context.AccessContext;

/**
 * 渠道选择策略(优先民生)
 * @author mgt
 */
@Component(value="channelStrategy")
public class ChannelStrategyImpl implements IChannelStrategy {
    
    private final static Logger logger = LoggerFactory.getLogger(ChannelStrategyImpl.class);
	
	@Autowired
    private CBankIdConfigMapper bankConfigMapper;
	
	@Override
	public String getChannel(String bankId) {
	    //针对用户浏览器做特殊处理,QQ浏览器的指定用易宝的充值渠道
	    if(StringUtils.isNotBlank(AccessContext.getUserAgent())){
	        if(AccessContext.getUserAgent().contains("QQBrowser"))return ChannelIdConstant.YEEPAY.name();
	    }
            List<String> channelList = bankConfigMapper.queryChannel(bankId);
            if(channelList != null && channelList.size() > 0) {
                    //去除连连认证支付
                    //channelList.remove(ChannelIdConstant.LIANLIAN_MOBILE.name());
                    if(channelList.contains(ChannelIdConstant.UMBPAY.name())) {
                            return ChannelIdConstant.UMBPAY.name();
                    } else {
                        //非认证支付的第一个匹配的渠道
                        for(String channel : channelList) {
                            ChannelIdConstant channelEnum = null;
                            try {
                                channelEnum = ChannelIdConstant.from(channel);
                            } catch (IllegalArgumentException e) {
                                logger.info("网银充值渠道选择策略， 非支付渠道，跳过, channel：{}", channel);
                            }
                            if(channelEnum != null && !channelEnum.isAuthPay()) {
                                return channel;
                            }
                        }
                    }
            }
            return "";
    }
}
