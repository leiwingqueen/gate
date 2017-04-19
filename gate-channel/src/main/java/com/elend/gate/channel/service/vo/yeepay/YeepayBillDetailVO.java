package com.elend.gate.channel.service.vo.yeepay;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 每一条的打款记录信息
 * @author mgt
 *
 */
@NodeAnnotation(name="item", type=NodeType.CLASS)
public class YeepayBillDetailVO {
	/**下级机构编号*/
    @NodeAnnotation(name="mer_Id")
	public String merId;
    /**订单号*/
    @NodeAnnotation(name="order_Id")
	public String orderId;
    /**打款状态码*/
    @NodeAnnotation(name="r1_Code")
	public String r1Code;
    /**银行状态*/
    @NodeAnnotation(name="bank_Status")
	public String bankStatus;
    /**发起时间*/
    @NodeAnnotation(name="request_Date")
	public String request_Date;
    /**收款人名称*/
    @NodeAnnotation(name="payee_Name")
    public String payeeName;
    /**开户行*/
    @NodeAnnotation(name="payee_BankName")
    public String payeeBankName;
    /**收款账号*/
    @NodeAnnotation(name="payee_Bank_Account")
    public String payeeBankAccount;
    /**金额*/
    @NodeAnnotation(name="amount")
    public String amount;
    /**留言*/
    @NodeAnnotation(name="note")
    public String note;
    /**手续费*/
    @NodeAnnotation(name="fee")
    public String fee;
    /**处理时间*/
    @NodeAnnotation(name="complete_Date")
    public String complete_Date;
    /**退款时间*/
    @NodeAnnotation(name="refund_Date")
    public String refund_Date;
    /**失败原因*/
    @NodeAnnotation(name="fail_Desc")
    public String fail_Desc;
    /**摘要*/
    @NodeAnnotation(name="abstractInfo")
    public String abstractInfo;
    /**备注*/
    @NodeAnnotation(name="remarksInfo")
    public String remarksInfo;
    
	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getR1Code() {
		return r1Code;
	}

	public void setR1Code(String r1Code) {
		this.r1Code = r1Code;
	}

	public String getBankStatus() {
		return bankStatus;
	}

	public void setBankStatus(String bankStatus) {
		this.bankStatus = bankStatus;
	}

	public String getRequest_Date() {
		return request_Date;
	}

	public void setRequest_Date(String request_Date) {
		this.request_Date = request_Date;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getPayeeBankName() {
		return payeeBankName;
	}

	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}

	public String getPayeeBankAccount() {
		return payeeBankAccount;
	}

	public void setPayeeBankAccount(String payeeBankAccount) {
		this.payeeBankAccount = payeeBankAccount;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getComplete_Date() {
		return complete_Date;
	}

	public void setComplete_Date(String complete_Date) {
		this.complete_Date = complete_Date;
	}

	public String getRefund_Date() {
		return refund_Date;
	}

	public void setRefund_Date(String refund_Date) {
		this.refund_Date = refund_Date;
	}

	public String getFail_Desc() {
		return fail_Desc;
	}

	public void setFail_Desc(String fail_Desc) {
		this.fail_Desc = fail_Desc;
	}

	public String getAbstractInfo() {
		return abstractInfo;
	}

	public void setAbstractInfo(String abstractInfo) {
		this.abstractInfo = abstractInfo;
	}

	public String getRemarksInfo() {
		return remarksInfo;
	}

	public void setRemarksInfo(String remarksInfo) {
		this.remarksInfo = remarksInfo;
	}

	@Override 
    public String toString() { 
        return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
	
}
