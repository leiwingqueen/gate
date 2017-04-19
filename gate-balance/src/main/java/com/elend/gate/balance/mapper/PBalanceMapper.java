package com.elend.gate.balance.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.elend.gate.balance.model.PBalancePO;
import com.elend.gate.balance.vo.PBalanceSearchVO;
import com.elend.gate.balance.vo.PBalanceStatVO;
import com.elend.p2p.mapper.SqlMapper;

public interface PBalanceMapper extends SqlMapper {

    /**
     * 根据搜索条件返回列表
     * 
     * @param svo
     * @return
     */
    List<PBalancePO> list(PBalanceSearchVO svo);

    /**
     * 根据搜索条件返回列表总数
     * 
     * @param svo
     * @return
     */
    int count(PBalanceSearchVO svo);

    /**
     * 根据主键id获取单条记录
     * 
     * @param id
     * @return
     */
    PBalancePO get(int id);

    /**
     * 插入记录
     * 
     * @param vo
     */
    int insert(PBalancePO po);

    /**
     * 更新记录
     * 
     * @param vo
     */
    int update(PBalancePO po);

    /**
     * 根据主键id删除记录
     * 
     * @param id
     */
    int delete(int id);

    /**
     * 余额增加
     * @param userId
     * @param balanceType
     * @param amount
     */
    int balanceIncrease(@Param("userId")long userId, @Param("balanceType")int balanceType,
            @Param("amount")BigDecimal amount);
    
    /**
     * 充值
     * @param userId
     * @param balanceType
     * @param depositTime       最后充值时间
     * @param amount
     */
    int charge(@Param("userId")long userId, @Param("balanceType")int balanceType,
            @Param("amount")BigDecimal amount, @Param("depositTime")Date depositTime);

    /**
     * 获取记录
     * @param userId
     * @param type
     * @return
     */
    PBalancePO getByUserId(@Param("userId")long userId, @Param("balanceType")int balanceType);

    /**
     * 余额减少
     * @param userId
     * @param balanceType
     * @param amount
     * @return
     */
    int balanceDecrease(@Param("userId")long userId, @Param("balanceType")int balanceType,
            @Param("amount")BigDecimal amount);
    
    /**
     * 按类型查询账本的统计信息
     * @param type
     * @return
     */
    List<PBalanceStatVO> queryStatByType(int type);

    /**
     * 按类型查询
     * @param bankAccount
     * @return
     */
    List<PBalancePO> listByType(int type);

    /**
     * 查询所有的账本
     * @return
     */
    List<PBalancePO> listAll();

}
