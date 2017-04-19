package com.elend.gate.conf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elend.gate.conf.mapper.SBankPayLimitMapper;
import com.elend.gate.conf.model.SBankPayLimitPO;
import com.elend.gate.conf.service.SBankPayLimitService;
import com.elend.gate.conf.vo.SBankPayLimitSearchVO;
import com.elend.gate.conf.vo.SBankPayLimitVO;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;

@Service
public class SBankPayLimitServiceImpl implements SBankPayLimitService {
    
    private static Logger logger = LoggerFactory.getLogger(SBankPayLimitServiceImpl.class);

    @Autowired
    private SBankPayLimitMapper mapper;

    @Override
    public Result<PageInfo<SBankPayLimitVO>> list(SBankPayLimitSearchVO svo) {
        PageInfo<SBankPayLimitVO> paginInfo = new PageInfo<SBankPayLimitVO>();
        List<SBankPayLimitPO> list = mapper.list(svo);
        List<SBankPayLimitVO> volist = new ArrayList<SBankPayLimitVO>();
        for (SBankPayLimitPO po : list) {
            volist.add(new SBankPayLimitVO(po));
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
        return new Result<PageInfo<SBankPayLimitVO>>(ResultCode.SUCCESS,
                                                     paginInfo);
    }

    @Override
    public Result<SBankPayLimitVO> get(int id) {
        SBankPayLimitPO po = mapper.get(id);
        if (po != null) {
            return new Result<SBankPayLimitVO>(ResultCode.SUCCESS,
                                               new SBankPayLimitVO(po));
        }
        return new Result<SBankPayLimitVO>(ResultCode.FAILURE, null);
    }

    @Override
    public Result<SBankPayLimitVO> save(SBankPayLimitPO po) {
        po.setUpdateTime(new Date());
        po.setCreateTime(new Date());
        if (po.getId() > 0) {
            mapper.update(po);
        } else {
            SBankPayLimitPO po2 = mapper.getByBankId(po.getBankId(), po.getChannel(), po.getUserType());
            if(po2 != null) {
                logger.info("bankId:{}, channel:{}, userType:{} 限额已经存在", po.getBankId(), po.getChannel(), po.getUserType());
                return new Result<>(ResultCode.FAILURE, null, "限额已经存在，请勿重复添加");
            }
            mapper.insert(po);
        }
        return new Result<SBankPayLimitVO>(ResultCode.SUCCESS, new SBankPayLimitVO(po));
    }

    @Override
    public Result<Integer> delete(int id) {
        mapper.delete(id);
        return new Result<Integer>(ResultCode.SUCCESS, id);
    }

    @Override
    public Result<List<SBankPayLimitVO>> listAll() {
        
        List<SBankPayLimitVO> voList = new ArrayList<>();
        
        SBankPayLimitSearchVO svo = new SBankPayLimitSearchVO();
        svo.setSize(Integer.MAX_VALUE);
        List<SBankPayLimitPO> list = mapper.list(svo);
        for(SBankPayLimitPO po : list) {
            SBankPayLimitVO vo = new SBankPayLimitVO(po);
            voList.add(vo);
        }
        
        return new Result<>(ResultCode.SUCCESS, voList);
    }
}
