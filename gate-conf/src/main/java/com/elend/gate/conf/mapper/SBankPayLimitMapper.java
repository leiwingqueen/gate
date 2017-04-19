package com.elend.gate.conf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.elend.p2p.mapper.SqlMapper;
import com.elend.gate.conf.vo.SBankPayLimitSearchVO;
import com.elend.gate.conf.vo.SBankPayLimitVO;
import com.elend.gate.conf.model.SBankPayLimitPO;

public interface SBankPayLimitMapper extends SqlMapper {

    /**
     * 根据搜索条件返回列表
     * 
     * @param svo
     * @return
     */
    List<SBankPayLimitPO> list(SBankPayLimitSearchVO svo);

    /**
     * 根据搜索条件返回列表总数
     * 
     * @param svo
     * @return
     */
    int count(SBankPayLimitSearchVO svo);

    /**
     * 根据主键id获取单条记录
     * 
     * @param id
     * @return
     */
    SBankPayLimitPO get(int id);

    /**
     * 插入记录
     * 
     * @param vo
     */
    void insert(SBankPayLimitPO po);

    /**
     * 更新记录
     * 
     * @param vo
     */
    void update(SBankPayLimitPO po);

    /**
     * 根据主键id删除记录
     * 
     * @param id
     */
    int delete(int id);

    /**
     * 根据银行ID获取
     * @param bankId
     * @param channel
     * @param userType
     * @return
     */
    SBankPayLimitPO getByBankId(@Param("bankId")String bankId, @Param("channel")String channel, @Param("userType")String userType);

}
