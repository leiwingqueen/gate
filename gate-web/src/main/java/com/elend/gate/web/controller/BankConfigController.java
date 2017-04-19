package com.elend.gate.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elend.gate.channel.constant.BankIdConstant;
import com.elend.gate.channel.util.IChannelStrategy;
import com.elend.gate.conf.data.BankPayLimitData;
import com.elend.gate.conf.vo.SBankPayLimitVO;
import com.elend.p2p.BaseController;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;
import com.elend.p2p.util.memcached.CounterMemcache;

/**
 * 银行相关配置接口（共其他系统获取数据使用）
 * @author mgt
 *
 */
@Controller
//@Scope("prototype")
public class BankConfigController extends BaseController{
    
    private final static Logger logger = LoggerFactory.getLogger(BankConfigController.class);
    
    @Autowired
    @Qualifier("weightChannelStrategyImpl")
    private IChannelStrategy channelStrategy;
    
    @Autowired
    private CounterMemcache counterMemcache;
    
    /**
     * 单个限额查询缓存key
     */
    private static final String SINGLE_BANK_LIMIT_CACHE_PREFIX = "gate_single_bank_limit_cache_prefix_";
    /**
     * 所有限额查询缓存key
     */
    private static final String LIST_BANK_LIMIT_CACHE_PREFIX = "gate_list_bank_limit_cache_prefix";
    
    /**
     * 获取指定银行的限额信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/bank/cyberBankPayLimit.do")
    @ResponseBody
    public Result<List<SBankPayLimitVO>> cyberBankPayLimit(HttpServletRequest request, String bankId) {
        
        if(StringUtils.isBlank(bankId)) {
            return new Result<>(ResultCode.FAILURE, null, "请输入银行编号");
        }
        
        @SuppressWarnings("unchecked")
        List<SBankPayLimitVO> cacheVo = (List<SBankPayLimitVO>) counterMemcache.getObject(SINGLE_BANK_LIMIT_CACHE_PREFIX + bankId);
        if(cacheVo != null) {
            logger.info("bankId:{}, 获取缓存返回", bankId);
            return new Result<>(ResultCode.SUCCESS, cacheVo);
        }
        
        //根据策略，获取对应的渠道
        String channel = channelStrategy.getChannel(bankId);
        
        if(StringUtils.isBlank(channel)) {
            logger.info("没有对应的渠道，bankId:{}", bankId);
            return new Result<>(ResultCode.FAILURE, null, "暂不支持该银行");
        }
        
        //获取对应的限额
        List<SBankPayLimitVO> list = BankPayLimitData.getLimit(bankId, channel);
        if(list == null) {
            logger.info("找不到限额配置信息，bankId:{}", bankId);
            return new Result<>(ResultCode.FAILURE, null, "暂不支持该银行");
        }
        
        for(SBankPayLimitVO vo : list) {
            vo.setBankName(BankIdConstant.from(vo.getBankId()).getDesc());
        }
        
        /*SBankPayLimitVO maxVo = list.get(0);
        
        //获取最大的限额
        for(SBankPayLimitVO vo : list) {
            if(vo.getSingleLimitTypeEnum() == PayLimitType.NOT_LIMIT || vo.getSingleLimit().compareTo(maxVo.getSingleLimit()) > 0) {
                maxVo = vo;
            }
        }*/
        
        //缓存结果 1分钟
        counterMemcache.putObject(SINGLE_BANK_LIMIT_CACHE_PREFIX + bankId, list, 60);
        
        return new Result<>(ResultCode.SUCCESS, list);
    }
    
    
    /**
     * 查询网银充值的所有已经配置的限额信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/bank/cyberBankPayLimitList.do")
    @ResponseBody
    public Result<List<List<SBankPayLimitVO>>> cyberBankPayLimitList(HttpServletRequest request) {
        
        @SuppressWarnings("unchecked")
        List<List<SBankPayLimitVO>> cacheList = (List<List<SBankPayLimitVO>>) counterMemcache.getObject(LIST_BANK_LIMIT_CACHE_PREFIX);
        if(cacheList != null) {
            logger.info("限额列表获取缓存返回");
            return new Result<>(ResultCode.SUCCESS, cacheList);
        }
        
        List<List<SBankPayLimitVO>> list = new ArrayList<>();
        
        //遍历所有应存在的银行ID
        for(BankIdConstant bank : BankIdConstant.values()) {
            
            //根据策略选择银行的渠道
            String channel = channelStrategy.getChannel(bank.getBankId());
            //如果渠道不存在，说明银行不支持，不返回信息
            if(StringUtils.isBlank(channel)) {
                logger.info("银行不支持, bankId:{}", bank.getBankId());
                continue;
            }
            
            //获取对应的限额
            List<SBankPayLimitVO> subList = BankPayLimitData.getLimit(bank.getBankId(), channel);
            if(subList == null) {
                logger.warn("限额列表找不到限额配置信息，bankId:{}", bank.getBankId());
                continue;
            }
            
            for(SBankPayLimitVO vo : subList) {
                vo.setBankName(BankIdConstant.from(vo.getBankId()).getDesc());
            }
            
            list.add(subList);
        }
        
        //缓存结果 1分钟
        counterMemcache.putObject(LIST_BANK_LIMIT_CACHE_PREFIX, list, 60);
        
        return new Result<>(ResultCode.SUCCESS, list);
    }
    
}
