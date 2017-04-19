package com.elend.gate.util;

import java.util.HashMap;

import org.junit.Test;

public class GateHttpUtilTest {

    @Test
    public void testPostRequest() {
        HttpResult result=GateHttpUtil.postRequest("http://www.baidu.com", 
                                 new HashMap<String, String>(), 3000, 3000);
        System.out.println(result.toString());
    }

}
