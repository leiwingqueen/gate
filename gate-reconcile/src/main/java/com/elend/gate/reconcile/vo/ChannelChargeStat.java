package com.elend.gate.reconcile.vo;

import com.elend.gate.channel.constant.ChannelIdConstant;

/**
 * 通道充值对账统计
 * @author mgt
 */
public class ChannelChargeStat {
    /**渠道*/
    private ChannelIdConstant channel;
    
    private int orderNum = 0; // 总订单数据

    private int successNum = 0; // 成功的订单数量

    private int failNum = 0; // 失败总数

    private int equalNum = 0; // 对账成功数量

    private int notEqualNum = 0; // 不平的订单数量

    private int handingNum = 0; // 处理中的订单数量

    public ChannelChargeStat(ChannelIdConstant channel) {
        super();
        this.channel = channel;
    }

    public ChannelIdConstant getChannel() {
        return channel;
    }

    public void setChannel(ChannelIdConstant channel) {
        this.channel = channel;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(int successNum) {
        this.successNum = successNum;
    }

    public int getFailNum() {
        return failNum;
    }

    public void setFailNum(int failNum) {
        this.failNum = failNum;
    }

    public int getEqualNum() {
        return equalNum;
    }

    public void setEqualNum(int equalNum) {
        this.equalNum = equalNum;
    }

    public int getNotEqualNum() {
        return notEqualNum;
    }

    public void setNotEqualNum(int notEqualNum) {
        this.notEqualNum = notEqualNum;
    }

    public int getHandingNum() {
        return handingNum;
    }

    public void setHandingNum(int handingNum) {
        this.handingNum = handingNum;
    }
    @Override
    public String toString() {
        return "ChannelChargeStat [channel=" + channel + ", orderNum="
                + orderNum + ", successNum=" + successNum + ", failNum="
                + failNum + ", equalNum=" + equalNum + ", notEqualNum="
                + notEqualNum + ", handingNum=" + handingNum + "]";
    }
}
