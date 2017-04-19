package com.elend.gate.channel.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.elend.gate.channel.exception.CheckSignatureException;
import com.elend.gate.channel.facade.vo.AuthPartnerChargeData;
import com.elend.gate.channel.facade.vo.AuthPartnerChargeRequest;
import com.elend.gate.channel.facade.vo.PartnerChargeData;
import com.elend.gate.channel.facade.vo.PartnerChargeRequest;
import com.elend.p2p.ServiceException;
import com.elend.p2p.util.DateUtil;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;

/**
 * 认证支付对外接口格式定义
 * @author liyongquan 2015年7月13日
 *
 */
@Service("authPartnerService")
public class AuthPartnerServiceImpl extends PartnerServiceImpl{
    private final static Logger logger = LoggerFactory.getLogger(AuthPartnerServiceImpl.class);
    
    private DesPropertiesEncoder encoder = new DesPropertiesEncoder();
    
    /**
     * 充值传入参数
     * @param params--接入方传入的参数
     * @throws CheckSignatureException--签名验证失败
     * @return 解析后的参数
     */
    @Override
    public PartnerChargeData chargeRequest(PartnerChargeRequest params)throws CheckSignatureException,ServiceException{
        if(!(params instanceof AuthPartnerChargeRequest)){
            logger.error("类型转换失败");
        }
        //参数解析
        PartnerChargeData chargeData=super.chargeRequest(params);
        AuthPartnerChargeRequest authParams=(AuthPartnerChargeRequest)params;
        AuthPartnerChargeData authChargeData=new AuthPartnerChargeData(chargeData);
        long userId=0L;
        try{
            userId=StringUtils.isBlank(authParams.getUserId())?0L:Long.parseLong(authParams.getUserId());
        }catch(NumberFormatException e){
            logger.error("用户ID格式错误,userId:{}",authParams.getUserId());
        }
        //加密信息解密
        String realName = "";
        String idCard = "";
        String bankAccount = "";
        String mobilePhone = "";
        if(StringUtils.isNotBlank(authParams.getRealName())) {
            realName = encoder.decode(formatStr(authParams.getRealName()));
        }
        if(StringUtils.isNotBlank(authParams.getIdCard())) {
            idCard = encoder.decode(formatStr(authParams.getIdCard()));
        }
        if(StringUtils.isNotBlank(authParams.getBankAccount())) {
            bankAccount = encoder.decode(formatStr(authParams.getBankAccount()));
        }
        if(StringUtils.isNotBlank(authParams.getMobilePhone())) {
            mobilePhone = encoder.decode(formatStr(authParams.getMobilePhone()));
        }
        
        authChargeData.setUserId(userId);
        authChargeData.setBankAccount(bankAccount);
        authChargeData.setIdCard(idCard);
        authChargeData.setRealName(realName);
        authChargeData.setRegisterTime(DateUtil.strToTime(authParams.getRegisterTime(), DateUtil.DATE_FORMAT_PATTEN4));
        authChargeData.setContractNo(authParams.getContractNo());
        authChargeData.setMobilePhone(mobilePhone);
        return authChargeData;
    }
}
