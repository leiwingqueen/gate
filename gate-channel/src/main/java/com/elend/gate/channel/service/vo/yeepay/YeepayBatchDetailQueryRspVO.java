package com.elend.gate.channel.service.vo.yeepay;

import java.util.ArrayList;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 打款批次明细查询接口返回报文封装
 * @author mgt
 *
 */
@NodeAnnotation(name="data", type=NodeType.CLASS)
public class YeepayBatchDetailQueryRspVO {
	/**命令 */
    @NodeAnnotation(name="cmd")
	public String cmd = "BatchDetailQuery";
    /**返回码*/
    @NodeAnnotation(name="ret_Code")
	public String retCode;
    /**错误描述信*/
    @NodeAnnotation(name="error_Msg")
	public String errorMsg;
    /**打款批次号*/
    @NodeAnnotation(name="batch_No")
	public String batchNo;
    /**返回记录数*/
    @NodeAnnotation(name="total_Num")
	public String totalNum;
    /**
     * 数据结束标志
     * “Y”---表示查询结果已全部输出完毕；
     * “N”---表示查询结果只输出一部分，后续部分有待请求输出；
     * */
    @NodeAnnotation(name="end_Flag")
    public String endFlag;
    /**签名信息*/
    @NodeAnnotation(name="hmac")
    public String hmac;
    /**记录集合*/
    @NodeAnnotation(name="list/items", type=NodeType.LIST_FIELD)
    public ArrayList<YeepayBillDetailVO> list;
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
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}
	public String getEndFlag() {
		return endFlag;
	}
	public void setEndFlag(String endFlag) {
		this.endFlag = endFlag;
	}
	public String getHmac() {
		return hmac;
	}
	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
	public ArrayList<YeepayBillDetailVO> getList() {
		return list;
	}
	public void setList(ArrayList<YeepayBillDetailVO> list) {
		this.list = list;
	}
	@Override 
    public String toString() { 
        return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
	
}
