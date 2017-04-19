package com.elend.gate.notify;

import org.junit.Test;

public class QueueArbitratorTest {
    @Test
    public void testInit(){
        QueueArbitrator queueArb=new QueueArbitrator();
        queueArb.setHosts("183.56.145.23:2181");
        queueArb.setRoot("/gate");
        queueArb.setPrefix("node_");
        queueArb.init();
        try {
            Thread.sleep(100*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
