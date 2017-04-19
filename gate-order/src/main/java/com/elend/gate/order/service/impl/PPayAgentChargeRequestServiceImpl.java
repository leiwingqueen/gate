package com.elend.gate.order.service.impl;


import com.elend.gate.order.mapper.PPayAgentChargeRequestMapper;
import com.elend.gate.order.service.PPayAgentChargeRequestService;
import com.elend.gate.order.model.PPayAgentChargeRequestPO;
import com.elend.gate.order.vo.PPayAgentChargeRequestVO;
import com.elend.gate.order.vo.PPayAgentChargeRequestSearchVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;
import com.elend.p2p.util.DateUtil;
import com.elend.p2p.PageInfo;

import java.util.*;


@Service
public class PPayAgentChargeRequestServiceImpl implements PPayAgentChargeRequestService {
	
	@Autowired
	private PPayAgentChargeRequestMapper mapper;

	@Override
	public Result<PageInfo<PPayAgentChargeRequestVO>> list(PPayAgentChargeRequestSearchVO svo) {
		PageInfo<PPayAgentChargeRequestVO> paginInfo = new PageInfo<PPayAgentChargeRequestVO>();
		List<PPayAgentChargeRequestPO> list = mapper.list(svo);
		List<PPayAgentChargeRequestVO> volist=new ArrayList<PPayAgentChargeRequestVO>();
		for(PPayAgentChargeRequestPO po:list){
			volist.add(new PPayAgentChargeRequestVO(po));
		}
		paginInfo.setList(volist);

		if (list.size() > 0) {
			int totalNum = mapper.count(svo);
			int totalPage = totalNum % svo.getSize() == 0 ? totalNum / svo.getSize()
					: totalNum / svo.getSize() + 1;

			paginInfo.setCount(totalNum);
			paginInfo.setPage(svo.getPage());
			paginInfo.setPageCount(totalPage);
		} else {
			paginInfo.setCount(0);
			paginInfo.setPage(svo.getPage());
			paginInfo.setPageCount(0);
		}
		return new Result<PageInfo<PPayAgentChargeRequestVO>>(ResultCode.SUCCESS, paginInfo);
	}

	@Override
	public Result<PPayAgentChargeRequestVO> get(int id) {
		PPayAgentChargeRequestPO po=mapper.get(id);
		if(po!=null){
			return new Result<PPayAgentChargeRequestVO>(ResultCode.SUCCESS, new PPayAgentChargeRequestVO(po));
		}
		return new Result<PPayAgentChargeRequestVO>(ResultCode.FAILURE,null);
	}

	@Override
	public Result<PPayAgentChargeRequestVO> save(PPayAgentChargeRequestVO vo) {
		if(vo.getId() > 0){
			mapper.update(vo);
		}else{
			mapper.insert(vo);
		}
		return new Result<PPayAgentChargeRequestVO>(ResultCode.SUCCESS,vo);
	}


	@Override
	public Result<Integer> delete(int id) {
		mapper.delete(id);
		return new Result<Integer>(ResultCode.SUCCESS,id);
	}

    @Override
    public int updateNextQueryTime(int seconds, String orderId) {
        Date nextQueryTime = DateUtil.getDate(new Date(), 0, 0, 0, seconds);
        return mapper.updateNextQueryTime(nextQueryTime, orderId);
    }
}
