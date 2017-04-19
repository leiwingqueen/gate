package com.elend.gate.channel.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.elend.gate.channel.model.CBankIdConfigPO;
import com.elend.gate.channel.vo.CBankIdConfigSearchVO;
import com.elend.p2p.mapper.SqlMapper;

public interface CBankIdConfigMapper extends SqlMapper {
   /**
    * 根据支付渠道银行ID获取信息
    * @param payChannel--支付渠道
    * @param channelBankId--支付渠道银行ID
    * @return
    */
    String getBankId(@Param("payChannel")String payChannel,@Param("channelBankId")String channelBankId);
    /**
     * 根据网关银行ID获取信息
     * @param payChannel--支付渠道
     * @param bankId--网关银行ID
     * @return
     */
    String getChannelBankId(@Param("payChannel")String payChannel,@Param("bankId")String bankId);
    
    /**
     * 获取银行ID对应的渠道
     * @param bankId
     * @return
     */
    List<String> queryChannel(@Param("bankId")String bankId);

    /**
     * 插入记录
     * 
     * @param vo
     */
    void insert(CBankIdConfigPO po);
    
    /**
     * 根据条件查询
     * @param svo
     * @return
     */
    List<CBankIdConfigPO> list(CBankIdConfigSearchVO svo);
    
    /**
     * 根据条件统计
     * @param svo
     * @return
     */
    int count(CBankIdConfigSearchVO svo);
}
