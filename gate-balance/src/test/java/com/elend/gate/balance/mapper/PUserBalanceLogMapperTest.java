package com.elend.gate.balance.mapper;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elend.gate.balance.constant.BalanceType;
import com.elend.gate.balance.constant.PUserBalanceLogStatus;
import com.elend.gate.balance.model.PUserBalanceLogPO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/spring/*.xml" })
public class PUserBalanceLogMapperTest {
    
    @Autowired
    private PUserBalanceLogMapper mapper;

    @Test
    public void testList() {
        fail("Not yet implemented");
    }

    @Test
    public void testCount() {
        fail("Not yet implemented");
    }

    @Test
    public void testGet() {
        fail("Not yet implemented");
    }

    @Test
    public void testInsert() {
        PUserBalanceLogPO po = new PUserBalanceLogPO();
        po.setAmount(new BigDecimal(100));
        po.setBalance(new BigDecimal(100));
        po.setBalanceType(BalanceType.E_COIN.getType());
        po.setCreateTime(new Date());
        po.setOrderId("124545454545");
        po.setRemark("哦哦自己");
        po.setStatus(PUserBalanceLogStatus.NORMAL.getStatus());
        po.setSubject(2);
        po.setSubSubject(2);
        po.setTradeType(0);
        po.setUserId(100529);
        
        int row = mapper.insert(po);
        Assert.assertEquals(1, row);
    }

    @Test
    public void testUpdate() {
        fail("Not yet implemented");
    }

    @Test
    public void testDelete() {
        fail("Not yet implemented");
    }

}
