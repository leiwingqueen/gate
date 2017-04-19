package com.elend.gate.conf.service;

import java.util.List;

import com.elend.gate.conf.model.SBankPayLimitPO;
import com.elend.gate.conf.vo.SBankPayLimitSearchVO;
import com.elend.gate.conf.vo.SBankPayLimitVO;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;

public interface SBankPayLimitService {

    /**
     * 根据搜索条件返回列表
     * 
     * @param svo
     * @return
     */
    Result<PageInfo<SBankPayLimitVO>> list(SBankPayLimitSearchVO vo);

    /**
     * 根据主键id获取单条记录
     * 
     * @param id
     * @return
     */
    Result<SBankPayLimitVO> get(int id);

    /**
     * 插入记录
     * 
     * @param vo
     */
    Result<SBankPayLimitVO> save(SBankPayLimitPO vo);

    /**
     * 根据主键id删除记录
     * 
     * @param id
     */
    Result<Integer> delete(int id);

    /**
     * 获取所有记录
     * @return
     */
    Result<List<SBankPayLimitVO>> listAll();

}
