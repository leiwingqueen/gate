package com.elend.gate.notify.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.elend.gate.notify.model.NQueuePO;
import com.elend.p2p.mapper.SqlMapper;

public interface NQueueMapper extends SqlMapper {
    /**
     * 返回列表总数
     * @return
     */
    int countAll(@Param("queueIndex")int queueIndex);

    /**
     * 插入记录
     * 
     * @param vo
     */
    int insert(NQueuePO po);

    /**
     * 根据主键id删除记录
     * 
     * @param id
     */
    void delete(@Param("queueIndex")int queueIndex,@Param("id")int id);
    
    /**
     * 获得 Queue 列表
     *@param queueIndex-队列ID
     * @param limit - 限制最大返回的个数
     * @param now--当前时间(避免DB时间和服务器时间不一致导致延时)
     * @return queue 列表或 size=0 的列表，列表排列顺序 execute_time (从低到高), queue_id (从小到大)
     */
    List<NQueuePO> listQueue(@Param("queueIndex")int queueIndex,@Param("limit")int limit,@Param("now")Date now);
}
