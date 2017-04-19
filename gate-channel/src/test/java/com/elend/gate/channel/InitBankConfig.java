package com.elend.gate.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elend.gate.channel.constant.BankIdConstant;
import com.elend.gate.channel.constant.ChannelIdConstant;
/**
 * 初始化银行ID配置的sql
 * @author liyongquan 2015年6月11日
 *
 */
public class InitBankConfig {
    private final static Logger logger = LoggerFactory.getLogger(InitBankConfig.class);
    /**
     * 格式 支付渠道银行ID:网关银行ID,...
     */
    private static String[] bankMap = new String[] { "ICBC-NET:"+BankIdConstant.ICBC.getBankId(),
            "CMBCHINA-NET:"+BankIdConstant.CMBC.getBankId(), "ABC-NET:"+BankIdConstant.ABC.getBankId(), "CCB-NET:"+BankIdConstant.CCB.getBankId(),
            "BCCB-NET:"+BankIdConstant.BCCB.getBankId(), "BOCO-NET:"+BankIdConstant.BOCO.getBankId(), "CIB-NET:"+BankIdConstant.CIB.getBankId(),
            "CMBC-NET:"+BankIdConstant.CMBC.getBankId(), "CEB-NET:"+BankIdConstant.CEB.getBankId(), "BOC-NET:"+BankIdConstant.BOC.getBankId(),
            "PINGANBANK-NET:"+BankIdConstant.PINGAN.getBankId(), "ECITIC-NET:"+BankIdConstant.ECITIC.getBankId(),
            "SDB-NET:"+BankIdConstant.SDB.getBankId(), "GDB-NET:"+BankIdConstant.GDB.getBankId(), "SHB-NET:"+BankIdConstant.SHB.getBankId(),
            "SPDB-NET:"+BankIdConstant.SPDB.getBankId(), "POST-NET:"+BankIdConstant.POST.getBankId(), "BJRCB-NET:"+BankIdConstant.BJRCB.getBankId(),
            "HXB-NET:"+BankIdConstant.HXB.getBankId() };
    
    private static ChannelIdConstant channelId=ChannelIdConstant.YEEPAY;

    public static void main(String[] args) {
        InitBankConfig config=new InitBankConfig();
        config.initSql();
    }

    public void initSql() {
        StringBuffer buffer=new StringBuffer();
        for(String bank:bankMap){
            String channelBank=bank.split(":")[0];
            String bankId=bank.split(":")[1];
            String sql="insert into c_bank_id_config values(0,'%s','%s','%s',1);";
            buffer.append(String.format(sql,channelId.name(),channelBank,bankId));
            buffer.append("\n");
        }
        logger.info(buffer.toString());
    }
}
