package com.elend.gate.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elend.gate.channel.constant.BankIdConstant;
import com.elend.gate.channel.constant.ChannelIdConstant;
/**
 * 连连支付
 * 初始化银行ID配置的sql
 * @author liyongquan 2015年7月20日
 *
 */
public class LLInitBankConfig {
    private final static Logger logger = LoggerFactory.getLogger(LLInitBankConfig.class);
    /**
     * 格式 支付渠道银行ID:网关银行ID,...
     */
    private static String[] bankMap = new String[] { "01020000:"+BankIdConstant.ICBC.getBankId(),
            "03080000:"+BankIdConstant.CMBC.getBankId(), "01030000:"+BankIdConstant.ABC.getBankId(), "01050000:"+BankIdConstant.CCB.getBankId(),
            "04031000:"+BankIdConstant.BCCB.getBankId(), "03010000:"+BankIdConstant.BOCO.getBankId(), "03090000:"+BankIdConstant.CIB.getBankId(),
            "03080000:"+BankIdConstant.CMBC.getBankId(), "03030000:"+BankIdConstant.CEB.getBankId(), "01040000:"+BankIdConstant.BOC.getBankId(),
            "03070000:"+BankIdConstant.PINGAN.getBankId(), "03020000:"+BankIdConstant.ECITIC.getBankId(),
            "03060000:"+BankIdConstant.GDB.getBankId(), "04012900:"+BankIdConstant.SHB.getBankId(),
            "01000000:"+BankIdConstant.POST.getBankId(),
            "03040000:"+BankIdConstant.HXB.getBankId() };
    
    private static ChannelIdConstant channelId=ChannelIdConstant.LIANLIAN_MOBILE;

    public static void main(String[] args) {
        LLInitBankConfig config=new LLInitBankConfig();
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
