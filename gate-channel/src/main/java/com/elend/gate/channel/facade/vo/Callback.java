package com.elend.gate.channel.facade.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 渠道回调数据
 * @author liyongquan 2015年5月29日
 *
 */
public class Callback {
    /**
     * 是否点对点通知
     *     如果为是，接收成功标志不能为空
     */
    private boolean isNotify;
    /**
     * 接收成功标志
     */
    private String callbackStr;

    public String getCallbackStr() {
        return callbackStr;
    }

    public void setCallbackStr(String callbackStr) {
        this.callbackStr = callbackStr;
    }

    public boolean isNotify() {
        return isNotify;
    }

    public void setNotify(boolean isNotify) {
        this.isNotify = isNotify;
    }
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
