package com.elend.gate.notify;

public class QueueArbitrator extends Arbitrator{

    @Override
    public void masterEvent() {
        QueueSetting.setJobThreadWorking(true);
    }

    @Override
    public void notMasterEvent() {
        QueueSetting.setJobThreadWorking(false);
    }
}
