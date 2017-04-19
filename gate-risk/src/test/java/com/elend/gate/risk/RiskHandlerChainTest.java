package com.elend.gate.risk;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elend.gate.context.AccessContext;
import com.elend.gate.risk.vo.RiskParam;
import com.elend.p2p.Result;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/spring/*.xml" })
public class RiskHandlerChainTest {
    @Autowired
    private RiskHandlerChain chain;
    @Test
    public void testHandle() {
        AccessContext.setAccessIp("127.0.0.1");
        AccessContext.setReferer("http://www.gzdai.com/helloworld");
        RiskParam riskParam=new RiskParam();
        riskParam.setIp("127.0.0.2");
        riskParam.setReferer("http://www.gzdai.com/helloworld");
        riskParam.setTimeStamp(new Date().getTime());
        Result<String> result=chain.handle(riskParam);
        System.out.println(result.getMessage());
        Assert.assertTrue(result.isSuccess());
    }
}
