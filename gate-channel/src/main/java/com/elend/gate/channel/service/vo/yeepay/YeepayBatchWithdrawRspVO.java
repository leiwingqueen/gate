package com.elend.gate.channel.service.vo.yeepay;

import java.util.ArrayList;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 易宝批量打款返回报文
 * @author mgt
 *
 */
@NodeAnnotation(name="data", type=NodeType.CLASS)
public class YeepayBatchWithdrawRspVO {
    /**命令 */
    @NodeAnnotation(name="cmd")
    public String cmd;
    
    /**返回代码*/
    @NodeAnnotation(name="ret_Code")
    public String retCode;
    
    /**客户编号*/
    @NodeAnnotation(name="mer_Id")
    public String merId;

    /**打款批次号*/
    @NodeAnnotation(name="batch_No")
    public String batchNo;

    /**打款总金额*/
    @NodeAnnotation(name="total_Amt")
    public String totalAmt;

    /**打款总笔数*/
    @NodeAnnotation(name="total_Num")
    public String totalNum;
    
    /**状态码*/
    @NodeAnnotation(name="r1_Code")
    public String r1Code;
    
    /**
     * 签名信息
     * cmd、ret_Code、mer_Id、
     * batch_No、 total_Amt、 total_Num、 r1_Code
     * 字段值+商户密钥组成字符串
     * */
    @NodeAnnotation(name="hmac")
    public String hmac;
    
    /**
     * 批量打款明细
     */
    @NodeAnnotation(name="list", type=NodeType.LIST_FIELD)
    public ArrayList<YeepayBatchWithdrawRspItemVO> itemList;

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

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getR1Code() {
        return r1Code;
    }

    public void setR1Code(String r1Code) {
        this.r1Code = r1Code;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

    public ArrayList<YeepayBatchWithdrawRspItemVO> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<YeepayBatchWithdrawRspItemVO> itemList) {
        this.itemList = itemList;
    }
}
