package com.elend.gate.order.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.order.constant.OrderType;
import com.elend.gate.order.mapper.PTransactionLogMapper;
import com.elend.gate.order.service.PTransactionLogService;
import com.elend.gate.order.vo.PTransactionLogSearchVO;
import com.elend.gate.order.vo.PTransactionLogVO;
import com.elend.gate.order.vo.TotalAssetInfo;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;
import com.elend.pay.agent.sdk.constant.PayAgentChannel;

/**
 * 提现请求
 * 
 * @author mgt
 */
@Service
public class PTransactionLogServiceImpl implements PTransactionLogService {
    
    private Logger logger = LoggerFactory.getLogger(PTransactionLogServiceImpl.class);
    
    @Autowired
    private PTransactionLogMapper mapper;

    @Override
    public Result<PageInfo<PTransactionLogVO>> queryPage(
            PTransactionLogSearchVO svo) {
        
        PageInfo<PTransactionLogVO> pageInfo = new PageInfo<PTransactionLogVO>();
        Result<PageInfo<PTransactionLogVO>> result = new Result<PageInfo<PTransactionLogVO>>();
        result.setObject(pageInfo);
        
        // 查询
        logger.info("svo：{}", svo);
        
        List<PTransactionLogVO> list = mapper.list(svo);
        
        logger.info("query PTransactionLogVO List ,size:{} ", list.size());
        
        for(PTransactionLogVO vo : list) {
            try {
                vo.setChannelName(ChannelIdConstant.from(vo.getPayChannel()).getDesc());
            } catch (Exception e) {
            }
            
            try {
                vo.setChannelName(WithdrawChannel.from(vo.getPayChannel()).getDesc());
            } catch (Exception e) {
            }
            
            try {
                vo.setChannelName(PayAgentChannel.from(vo.getPayChannel()).getDesc());
            } catch (Exception e) {
            }
            
            vo.setOrderTypeName(OrderType.from(vo.getOrderType()).getDesc());
        }

        if (list.size() > 0) {
            int totalNum = mapper.count(svo);
            int totalPage = (totalNum + svo.getSize() - 1) / svo.getSize();

            pageInfo.setCount(totalNum);
            pageInfo.setPage(svo.getPage());
            pageInfo.setPageCount(totalPage);
        } else {
            pageInfo.setCount(0);
            pageInfo.setPage(svo.getPage());
            pageInfo.setPageCount(0);
        }
        pageInfo.setList(list);
        result.setCode(ResultCode.SUCCESS);
        result.setObject(pageInfo);
        return result;
    }

    @Override
    public Result<TotalAssetInfo> getTotal(PTransactionLogSearchVO svo) {
        TotalAssetInfo info = mapper.getTotal(svo);
        return new Result<>(ResultCode.SUCCESS, info);
    }

}
