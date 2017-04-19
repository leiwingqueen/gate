package com.elend.gate.order.service.impl;

import com.elend.gate.order.mapper.PPayAgentAuthRequestMapper;
import com.elend.gate.order.service.PPayAgentAuthRequestService;
import com.elend.gate.order.model.PPayAgentAuthRequestPO;
import com.elend.gate.order.vo.PPayAgentAuthRequestVO;
import com.elend.gate.order.vo.PPayAgentAuthRequestSearchVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;
import com.elend.p2p.util.DateUtil;
import com.elend.p2p.PageInfo;

import java.util.*;

@Service
public class PPayAgentAuthRequestServiceImpl implements
        PPayAgentAuthRequestService {

    @Autowired
    private PPayAgentAuthRequestMapper mapper;

    @Override
    public Result<PageInfo<PPayAgentAuthRequestVO>> list(
            PPayAgentAuthRequestSearchVO svo) {
        PageInfo<PPayAgentAuthRequestVO> paginInfo = new PageInfo<PPayAgentAuthRequestVO>();
        List<PPayAgentAuthRequestPO> list = mapper.list(svo);
        List<PPayAgentAuthRequestVO> volist = new ArrayList<PPayAgentAuthRequestVO>();
        for (PPayAgentAuthRequestPO po : list) {
            volist.add(new PPayAgentAuthRequestVO(po));
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
        return new Result<PageInfo<PPayAgentAuthRequestVO>>(
                                                            ResultCode.SUCCESS,
                                                            paginInfo);
    }

    @Override
    public Result<PPayAgentAuthRequestVO> get(int id) {
        PPayAgentAuthRequestPO po = mapper.get(id);
        if (po != null) {
            return new Result<PPayAgentAuthRequestVO>(
                                                      ResultCode.SUCCESS,
                                                      new PPayAgentAuthRequestVO(
                                                                                 po));
        }
        return new Result<PPayAgentAuthRequestVO>(ResultCode.FAILURE, null);
    }

    @Override
    public Result<PPayAgentAuthRequestVO> save(PPayAgentAuthRequestVO vo) {
        if (vo.getId() > 0) {
            mapper.update(vo);
        } else {
            mapper.insert(vo);
        }
        return new Result<PPayAgentAuthRequestVO>(ResultCode.SUCCESS, vo);
    }

    @Override
    public Result<Integer> delete(int id) {
        mapper.delete(id);
        return new Result<Integer>(ResultCode.SUCCESS, id);
    }

    @Override
    public int updateNextQueryTime(int seconds, String orderId) {
        Date nextQueryTime = DateUtil.getDate(new Date(), 0, 0, 0, seconds);
        return mapper.updateNextQueryTime(nextQueryTime, orderId);
    }
}
