package com.elend.gate.balance.mapper;

import java.util.List;
import com.elend.p2p.mapper.SqlMapper;
import com.elend.gate.balance.vo.PUserBalanceLogSearchVO;
import com.elend.gate.balance.model.PUserBalanceLogPO;

public interface PUserBalanceLogMapper extends SqlMapper {

    /**
     * 根据搜索条件返回列表
     * 
     * @param svo
     * @return
     */
    List<PUserBalanceLogPO> list(PUserBalanceLogSearchVO svo);

    /**
     * 根据搜索条件返回列表总数
     * 
     * @param svo
     * @return
     */
    int count(PUserBalanceLogSearchVO svo);

    /**
     * 根据主键id获取单条记录
     * 
     * @param id
     * @return
     */
    PUserBalanceLogPO get(int id);

    /**
     * 插入记录
     * 
     * @param vo
     */
    int insert(PUserBalanceLogPO po);

    /**
     * 更新记录
     * 
     * @param vo
     */
    int update(PUserBalanceLogPO po);

    /**
     * 根据主键id删除记录
     * 
     * @param id
     */
    int delete(int id);

}
