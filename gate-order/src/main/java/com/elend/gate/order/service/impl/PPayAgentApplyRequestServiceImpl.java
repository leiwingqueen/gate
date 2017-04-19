package com.elend.gate.order.service.impl;

import com.elend.gate.order.mapper.PPayAgentApplyRequestMapper;
import com.elend.gate.order.service.PPayAgentApplyRequestService;
import com.elend.gate.order.model.PPayAgentApplyRequestPO;
import com.elend.gate.order.vo.PPayAgentApplyRequestVO;
import com.elend.gate.order.vo.PPayAgentApplyRequestSearchVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;
import com.elend.p2p.PageInfo;

import java.util.*;

@Service
public class PPayAgentApplyRequestServiceImpl implements
        PPayAgentApplyRequestService {

    @Autowired
    private PPayAgentApplyRequestMapper mapper;

    @Override
    public Result<PageInfo<PPayAgentApplyRequestVO>> list(
            PPayAgentApplyRequestSearchVO svo) {
        PageInfo<PPayAgentApplyRequestVO> paginInfo = new PageInfo<PPayAgentApplyRequestVO>();
        List<PPayAgentApplyRequestPO> list = mapper.list(svo);
        List<PPayAgentApplyRequestVO> volist = new ArrayList<PPayAgentApplyRequestVO>();
        for (PPayAgentApplyRequestPO po : list) {
            volist.add(new PPayAgentApplyRequestVO(po));
        }
        paginInfo.setList(volist);

        if (list.size() > 0) {
            int totalNum = mapper.count(svo);
            int totalPage = totalNum % svo.getSize() == 0 ? totalNum
                    / svo.getSize() : totalNum / svo.getSize() + 1;

            paginInfo.setCount(totalNum);
            paginInfo.setPage(svo.getPage());
            paginInfo.setPageCount(totalPage);
        } else {
            paginInfo.setCount(0);
            paginInfo.setPage(svo.getPage());
            paginInfo.setPageCount(0);
        }
        return new Result<PageInfo<PPayAgentApplyRequestVO>>(
                                                             ResultCode.SUCCESS,
                                                             paginInfo);
    }

    @Override
    public Result<PPayAgentApplyRequestVO> get(int id) {
        PPayAgentApplyRequestPO po = mapper.get(id);
        if (po != null) {
            return new Result<PPayAgentApplyRequestVO>(
                                                       ResultCode.SUCCESS,
                                                       new PPayAgentApplyRequestVO(
                                                                                   po));
        }
        return new Result<PPayAgentApplyRequestVO>(ResultCode.FAILURE, null);
    }

    @Override
    public Result<PPayAgentApplyRequestVO> save(PPayAgentApplyRequestVO vo) {
        if (vo.getId() > 0) {
            mapper.update(vo);
        } else {
            mapper.insert(vo);
        }
        return new Result<PPayAgentApplyRequestVO>(ResultCode.SUCCESS, vo);
    }

    @Override
    public Result<Integer> delete(int id) {
        mapper.delete(id);
        return new Result<Integer>(ResultCode.SUCCESS, id);
    }

    @Override
    public PPayAgentApplyRequestVO getByOrderId(String orderId) {
        PPayAgentApplyRequestPO po = mapper.getByOrderId(orderId);
        if(po == null) {
            return null;
        }
        
        return new PPayAgentApplyRequestVO(po);
    }
}
