package com.elend.gate.channel.service.vo.yeepay;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 易宝批量打款请求报文
 * @author mgt
 *
 */
@NodeAnnotation(name="data", type=NodeType.CLASS)
public class YeepayBatchWithdrawRequestVO {
    /**命令 */
    @NodeAnnotation(name="cmd")
    public String cmd = "TransferSingle";
    
    /**版本*/
    @NodeAnnotation(name="version")
    public String version = "1.0";
    
    /**总公司商户编号*/
    @NodeAnnotation(name="group_Id")
    public String groupId;

    /**实际发起付款的交易商户编号*/
    @NodeAnnotation(name="mer_Id")
    public String merId;

    /**产品类型  为空走代付、代发出款*/
    @NodeAnnotation(name="product")
    public String product;

    /**打款批次号*/
    @NodeAnnotation(name="batch_No")
    public String batchNo;
    
    /**打款总笔数*/
    @NodeAnnotation(name="total_Num")
    public String totalNum;
    
    /**打款总金额*/
    @NodeAnnotation(name="total_Amt")
    public String totalAmt;

    /**是否需要判断重复打款*/
    @NodeAnnotation(name="is_Repay")
    public String isRepay;

    /**
     * 签名信息
     * cmd 、 mer_Id 、 batch_No 、
     * total_Num、total_Amt、is_Repay 参数值
     * +商户密钥组成字符串
     * */
    @NodeAnnotation(name="hmac")
    public String hmac;
    
    /**
     * 批量打款明细
     */
    @NodeAnnotation(name="list", type=NodeType.LIST_FIELD)
    public List<YeepayBatchWithdrawItemVO> itemList;

    @Override 
    public String toString() { 
        return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }

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

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getIsRepay() {
        return isRepay;
    }

    public void setIsRepay(String isRepay) {
        this.isRepay = isRepay;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

    public List<YeepayBatchWithdrawItemVO> getItemList() {
        return itemList;
    }

    public void setItemList(List<YeepayBatchWithdrawItemVO> itemList) {
        this.itemList = itemList;
    }
}
