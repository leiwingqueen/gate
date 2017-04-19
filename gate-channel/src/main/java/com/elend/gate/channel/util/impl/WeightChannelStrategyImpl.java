package com.elend.gate.channel.util.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.mapper.CBankIdConfigMapper;
import com.elend.gate.channel.util.IChannelStrategy;
import com.elend.gate.conf.facade.PropertyFacade;
import com.elend.gate.context.AccessContext;
import com.elend.p2p.gson.JSONUtils;
import com.google.gson.reflect.TypeToken;

/**
 * 渠道选择策略(按照渠道权重)
 * 
 * @author mgt
 */
@Component(value = "weightChannelStrategyImpl")
public class WeightChannelStrategyImpl implements IChannelStrategy {

    private final static Logger logger = LoggerFactory.getLogger(WeightChannelStrategyImpl.class);

    @Autowired
    private CBankIdConfigMapper bankConfigMapper;

    @Autowired
    private PropertyFacade facade;

    @Override
    public String getChannel(String bankId) {
        // 针对用户浏览器做特殊处理,QQ浏览器的指定用易宝的充值渠道
        if (StringUtils.isNotBlank(AccessContext.getUserAgent())) {
            if (AccessContext.getUserAgent().contains("QQBrowser"))
                return ChannelIdConstant.YEEPAY.name();
        }

        // 获取渠道的权重
        String weightJson = facade.getProperty("pay_channel_weight", "{\"UMBPAY\":4,\"LIANLIAN_GATE\":3,\"YEEPAY\":2,\"MONEY_MORE_MORE\":1}");
        
        Map<String, Integer> weightMap = JSONUtils.fromJson(weightJson, new TypeToken<Map<String, Integer>>() {});

        List<String> channelList = bankConfigMapper.queryChannel(bankId);
        if(channelList == null || channelList.size() < 1) {
            return "";
        }

        ChannelIdConstant channelEnum = null;
        // 非认证支付的第一个匹配的渠道
        for (String channel : channelList) {
            ChannelIdConstant channelBuf = null;
            try {
                channelBuf = ChannelIdConstant.from(channel);
            } catch (IllegalArgumentException e) {
                logger.info("网银充值渠道选择策略， 非支付渠道，跳过, channel：{}", channel);
            }
            if (channelBuf == null || channelBuf.isAuthPay()) {
                continue;
            }
            
            int weightBuf = 0;
            int weight = 0;
            if(channelEnum == null) {
                weight = -1;
            }
            
            /**
             * 如果都没有配置权重，会选择循环里面的第一个
             * 如果某些配置了， 某些没有配置， 会选择已经配置的并且权重最高的
             */
            try {
                weightBuf = weightMap.get(channelBuf.name());
            } catch (Exception e) {
            }
            try {
                weight = weightMap.get(channelEnum.name());
            } catch (Exception e) {
            }
            
            if(weightBuf > weight) {
                channelEnum = channelBuf;
            }
        }
        return channelEnum == null ? "" : channelEnum.name();
    }
}
