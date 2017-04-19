package com.elend.gate.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.order.constant.RequestStatus;
import com.elend.gate.order.mapper.PWithdrawRequestMapper;
import com.elend.gate.order.model.PWithdrawRequestPO;
import com.elend.gate.order.service.PWithdrawRequestService;
import com.elend.gate.order.vo.PWithdrawRequestSearchVO;
import com.elend.gate.order.vo.PWithdrawRequestVO;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;

/**
 * 提现请求
 * @author mgt
 */
@Service
public class PWithdrawRequestServiceImpl implements PWithdrawRequestService {
    
    private Logger logger = LoggerFactory.getLogger(PWithdrawRequestServiceImpl.class);
    
	@Autowired
	private PWithdrawRequestMapper mapper;

    @Override
    public PWithdrawRequestPO getByPartnerId(PartnerConstant partnerId,
            String partnerOrderId) {
        return mapper.getByPartnerOrderId(partnerId.name(), partnerOrderId);
    }
    
    @Override
    public List<PWithdrawRequestPO> listByCreateTime(Date begin, Date end,
            int currentPage, int size) {
        if(currentPage < 1) {
            return new ArrayList<PWithdrawRequestPO>();
        }
        int start = (currentPage - 1) * size;
        return mapper.listPageByCreateTime(begin, end, start, size);
    }

    @Override
    public Result<PageInfo<PWithdrawRequestVO>> queryPage(
            PWithdrawRequestSearchVO svo) {
        PageInfo<PWithdrawRequestVO> pageInfo = new PageInfo<PWithdrawRequestVO>();
        Result<PageInfo<PWithdrawRequestVO>> result = new Result<PageInfo<PWithdrawRequestVO>>();
        result.setObject(pageInfo);
        
        // 查询
        logger.info("svo：{}", svo);
        
        List<PWithdrawRequestVO> list = mapper.list(svo);
        
        logger.info("query PWithdrawRequestVO List ,size:{} ", list.size());
        
        for(PWithdrawRequestVO vo : list) {
            RequestStatus status = RequestStatus.from((short)vo.getStatus());
            vo.setStatusStr(status.getDesc());
            vo.setChannelName(WithdrawChannel.from(vo.getChannel()).getDesc());
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
    public PWithdrawRequestPO getByOrderId(String orderId) {
        return mapper.getByOrderId(orderId);
    }
}
