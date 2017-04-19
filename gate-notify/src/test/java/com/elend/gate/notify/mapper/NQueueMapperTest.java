package com.elend.gate.notify.mapper;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/spring/*.xml" })
public class NQueueMapperTest {
    @Autowired
    private NQueueMapper queueMapper;
    @Test
    public void testList() {
        fail("Not yet implemented");
    }

    @Test
    public void testCountAll() {
        int count=queueMapper.countAll(2);
        System.out.println("count:"+count);
        Assert.assertTrue(count>0);
    }

    @Test
    public void testGet() {
        fail("Not yet implemented");
    }

    @Test
    public void testInsert() {
        fail("Not yet implemented");
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
