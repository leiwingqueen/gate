package com.elend.gate.admin.vo;

import com.elend.gate.conf.vo.SBankPayLimitVO;

/**
 * 支付限额信息封装
 * @author mgt
 *
 */
public class BankPayLimit {
    
    private SBankPayLimitVO vo;
    private String bank;
    
    public SBankPayLimitVO getVo() {
        return vo;
    }
    public void setVo(SBankPayLimitVO vo) {
        this.vo = vo;
    }
    public String getBank() {
        return bank;
    }
    public void setBank(String bank) {
        this.bank = bank;
    }
}
