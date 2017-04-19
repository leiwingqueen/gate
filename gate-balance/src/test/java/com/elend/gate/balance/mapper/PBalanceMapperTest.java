package com.elend.gate.balance.mapper;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elend.gate.balance.constant.BalanceType;
import com.elend.gate.balance.constant.PBalanceStatus;
import com.elend.gate.balance.model.PBalancePO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/spring/*.xml" })
public class PBalanceMapperTest {
    
    @Autowired
    private PBalanceMapper mapper;

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
        PBalancePO po = new PBalancePO();
        
        po.setBalanceType(BalanceType.E_COIN.getType());
        po.setCreateTime(new Date());
        po.setDepositTime(new Date());
        po.setStatus(PBalanceStatus.NORMAL.getStatus());
        po.setUsableBalance(new BigDecimal(100000));
        po.setUserId(100529);
        po.setRemark("总账");
        
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
