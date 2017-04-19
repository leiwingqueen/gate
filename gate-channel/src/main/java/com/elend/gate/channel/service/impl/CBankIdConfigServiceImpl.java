package com.elend.gate.channel.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elend.gate.channel.mapper.CBankIdConfigMapper;
import com.elend.gate.channel.model.CBankIdConfigPO;
import com.elend.gate.channel.service.CBankIdConfigService;
import com.elend.gate.channel.vo.CBankIdConfigSearchVO;
import com.elend.gate.channel.vo.CBankIdConfigVO;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;

@Service
public class CBankIdConfigServiceImpl implements CBankIdConfigService {
    
    private final static Logger logger = LoggerFactory.getLogger(CBankIdConfigServiceImpl.class);
    
    @Autowired
    private CBankIdConfigMapper mapper;

    @Override
    public Result<PageInfo<CBankIdConfigVO>> list(CBankIdConfigSearchVO svo) {
        
        PageInfo<CBankIdConfigVO> paginInfo = new PageInfo<CBankIdConfigVO>();
        List<CBankIdConfigPO> list = mapper.list(svo);
        List<CBankIdConfigVO> volist = new ArrayList<CBankIdConfigVO>();
        for (CBankIdConfigPO po : list) {
            volist.add(new CBankIdConfigVO(po));
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
        return new Result<PageInfo<CBankIdConfigVO>>(ResultCode.SUCCESS, paginInfo);
    }
}
