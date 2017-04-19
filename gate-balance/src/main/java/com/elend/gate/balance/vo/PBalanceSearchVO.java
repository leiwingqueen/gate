package com.elend.gate.balance.vo;

import com.elend.p2p.util.vo.BaseSearchVO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PBalanceSearchVO extends BaseSearchVO{

	/**  */
	private long userId;
	
	/** 账本类型：rmb，或者其他货币 */
	private int balanceType;
	
	/** 状态:1 正常，2 可用余额锁定，不能扣款 */
	private int status;
	
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public int getBalanceType() {
		return balanceType;
	}
	
	public void setBalanceType(int balanceType) {
		this.balanceType = balanceType;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
