package com.elend.gate.channel.service.vo.yeepay;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 账户余额查询请求报文封装
 * @author mgt
 *
 */
@NodeAnnotation(name="data", type=NodeType.CLASS)
public class YeepayAccountBalanceRequestVO {
	/**命令 */
    @NodeAnnotation(name="cmd")
	public String cmd = "AccountBalanaceQuery";
    /**接口版本*/
    @NodeAnnotation(name="version")
	public String version = "1.0";
    /**商户号*/
    @NodeAnnotation(name="mer_Id")
	public String merId;
    /**日期 */
    @NodeAnnotation(name="date")
	public String date;
    /**签名信息*/
    @NodeAnnotation(name="hmac")
	public String hmac;
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getHmac() {
		return hmac;
	}
	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
    
	@Override 
    public String toString() { 
        return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
	
}
