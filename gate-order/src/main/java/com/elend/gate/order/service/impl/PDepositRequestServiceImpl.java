package com.elend.gate.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elend.gate.order.mapper.PDepositRequestMapper;
import com.elend.gate.order.model.PDepositRequestPO;
import com.elend.gate.order.service.PDepositRequestService;
import com.elend.gate.order.vo.PDepositRequestSearchVO;
import com.elend.gate.order.vo.PDepositRequestVO;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;

/**
 * 充值请求
 * @author mgt
 */
@Service
public class PDepositRequestServiceImpl implements PDepositRequestService {
    
    private Logger logger = LoggerFactory.getLogger(PWithdrawRequestServiceImpl.class);
    
	@Autowired
	private PDepositRequestMapper mapper;

	@Override
	public List<PDepositRequestPO> list(PDepositRequestSearchVO vo) {
		return mapper.list(vo);
	}

	@Override
	public PDepositRequestPO getByOrderId(String orderId) {
		return mapper.getByOrderId(orderId);
	}

    @Override
    public Result<PageInfo<PDepositRequestVO>> queryPage(
            PDepositRequestSearchVO svo) {
        PageInfo<PDepositRequestVO> pageInfo = new PageInfo<PDepositRequestVO>();
        Result<PageInfo<PDepositRequestVO>> result = new Result<PageInfo<PDepositRequestVO>>();
        result.setObject(pageInfo);
        
        // 查询
        logger.info("svo：{}", svo);
        
        List<PDepositRequestPO> list = mapper.listPage(svo);
        List<PDepositRequestVO> voList = new ArrayList<>();
        
        logger.info("query PDepositRequestVO List ,size:{} ", list.size());
        
        for(PDepositRequestPO po : list) {
            voList.add(new PDepositRequestVO(po));
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
        pageInfo.setList(voList);
        result.setCode(ResultCode.SUCCESS);
        result.setObject(pageInfo);
        return result;
    }

}
