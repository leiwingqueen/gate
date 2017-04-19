package com.elend.gate.reconcile.constant;

import com.elend.p2p.util.SystemPropertiesHelp;

/**
 * 系统的配置
 * 
 * @author mgt
 */
public class SystemConstant {
    /**
     * 对账结果发送的邮件地址
     */
    public final static String RECONCILE_SEND_EMAIL = SystemPropertiesHelp.getProperty("reconcile_send_email",
                                                                                       "liuxianyan@gzdai.com,linshumao@gzdai.com,liyongquan@gzdai.com");
}
