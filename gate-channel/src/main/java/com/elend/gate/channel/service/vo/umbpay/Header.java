package com.elend.gate.channel.service.vo.umbpay;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 宝易互通请求报文头
 * @author mgt
 *
 */
@NodeAnnotation(name="head", type=NodeType.CLASS)
public class Header {
    /**版本 */
    @NodeAnnotation(name="version")
    public String version;
    /**
     * 报文类型  
     * 请求报文： 0001
     * 应答报文： 0002
     * */
    @NodeAnnotation(name="msgtype")
    public String msgtype;
    /**
     * 渠道代号
     * 民生体系外商户填 99
     * */
    @NodeAnnotation(name="channelno")
    public String channelno;
    /**
     * 商户号
     *  */
    @NodeAnnotation(name="merchantno")
    public String merchantno;
    /**交易日期*/
    @NodeAnnotation(name="trandate")
    public String trandate;
    /**交易时间*/
    @NodeAnnotation(name="trantime")
    public String trantime;
    /**交易流水号 */
    @NodeAnnotation(name="bussflowno")
    public String bussflowno;
    /**交易代码*/
    @NodeAnnotation(name="trancode")
    public String trancode;
    /**返回码*/
    @NodeAnnotation(name="respcode")
    public String respcode;
    /**返回信息描述*/
    @NodeAnnotation(name="respmsg")
    public String respmsg;
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getMsgtype() {
        return msgtype;
    }
    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }
    public String getChannelno() {
        return channelno;
    }
    public void setChannelno(String channelno) {
        this.channelno = channelno;
    }
    public String getMerchantno() {
        return merchantno;
    }
    public void setMerchantno(String merchantno) {
        this.merchantno = merchantno;
    }
    public String getTrandate() {
        return trandate;
    }
    public void setTrandate(String trandate) {
        this.trandate = trandate;
    }
    public String getTrantime() {
        return trantime;
    }
    public void setTrantime(String trantime) {
        this.trantime = trantime;
    }
    public String getBussflowno() {
        return bussflowno;
    }
    public void setBussflowno(String bussflowno) {
        this.bussflowno = bussflowno;
    }
    public String getTrancode() {
        return trancode;
    }
    public void setTrancode(String trancode) {
        this.trancode = trancode;
    }
    public String getRespcode() {
        return respcode;
    }
    public void setRespcode(String respcode) {
        this.respcode = respcode;
    }
    public String getRespmsg() {
        return respmsg;
    }
    public void setRespmsg(String respmsg) {
        this.respmsg = respmsg;
    }
    @Override 
    public String toString() { 
        return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
