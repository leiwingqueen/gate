package com.elend.gate.channel.service.vo.yeepay;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 账户余额查询返回报文封装
 * @author mgt
 *
 */
@NodeAnnotation(name="data", type=NodeType.CLASS)
public class YeepayAccountBalanceRspVO {
	/**命令 */
    @NodeAnnotation(name="cmd")
	public String cmd;
    /**返回码 */
    @NodeAnnotation(name="ret_Code")
	public String retCode;
    /**错误信息描述 */
    @NodeAnnotation(name="error_msg")
	public String errorMsg;
    /**账户余额*/
    @NodeAnnotation(name="balance_Amount")
	public String balanceAmount;
    /**可用打款余额*/
    @NodeAnnotation(name="valid_Amount")
	public String validAmount;
    /**签名信息*/
    @NodeAnnotation(name="hmac")
	public String hmac;
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(String balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	public String getValidAmount() {
		return validAmount;
	}
	public void setValidAmount(String validAmount) {
		this.validAmount = validAmount;
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
