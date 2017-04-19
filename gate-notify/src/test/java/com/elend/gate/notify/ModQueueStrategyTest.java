package com.elend.gate.notify;

import org.junit.Test;

import com.elend.p2p.util.OrderIdHelper;

public class ModQueueStrategyTest {

    @Test
    public void testGetQueueIndex() {
        ModQueueStrategy strategy=new ModQueueStrategy();
        for(int i=0;i<100;i++){
            String orderId=OrderIdHelper.newOrderId();
            int index=strategy.getQueueIndex(orderId);
            System.out.println(index);
        }
    }

}
