package com.elend.gate.channel.service.vo.yeepay;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 打款批次明细查询接口请求报文封装
 * @author mgt
 *
 */
@NodeAnnotation(name="data", type=NodeType.CLASS)
public class YeepayBatchDetailQueryRequestVO {
	/**命令 */
    @NodeAnnotation(name="cmd")
	public String cmd = "BatchDetailQuery";
    /**接口版本*/
    @NodeAnnotation(name="version")
	public String version = "1.0";
    /**总公司客户编号*/
    @NodeAnnotation(name="group_Id")
	public String groupId;
    /**分公司客户编号*/
    @NodeAnnotation(name="mer_Id")
	public String merId;
    /**
     * 本级下级标识
     * 1：查询本公司自己发的交易甲方填写 1 即可 
     * 2： 查 询 下 级 机 构 发 的 交 易
     * 3：查询本公司+下级机构所有交易
     * */
    @NodeAnnotation(name="query_Mode")
	public String queryMode;
    /**产品类型*/
    @NodeAnnotation(name="product")
    public String product;
    /**打款批次号*/
    @NodeAnnotation(name="batch_No")
    public String batchNo;
    /**订单号*/
    @NodeAnnotation(name="order_Id")
    public String orderId;
    /**查询页码*/
    @NodeAnnotation(name="page_No")
    public String pageNo;
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
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getQueryMode() {
		return queryMode;
	}
	public void setQueryMode(String queryMode) {
		this.queryMode = queryMode;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
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
