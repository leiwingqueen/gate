package com.elend.gate.balance.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elend.gate.balance.constant.BalanceAccountType;
import com.elend.gate.balance.constant.BalanceType;
import com.elend.gate.balance.constant.ChannelIdConstant;
import com.elend.gate.balance.constant.SubSubject;
import com.elend.gate.balance.constant.Subject;
import com.elend.gate.balance.constant.TradeType;
import com.elend.gate.balance.constant.WithdrawChannel;
import com.elend.gate.balance.exception.BalanceException;
import com.elend.gate.balance.exception.BalanceNotEnoughException;
import com.elend.gate.balance.mapper.PBalanceMapper;
import com.elend.gate.balance.mapper.PUserBalanceLogMapper;
import com.elend.gate.balance.model.PBalancePO;
import com.elend.gate.balance.model.PUserBalanceLogPO;
import com.elend.gate.balance.service.BalanceService;
import com.elend.gate.balance.vo.PBalanceStatVO;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;

/**
 * 账本服务类
 * @author mgt
 *
 */
@Service
public class BalanceServiceImpl implements BalanceService {
    
    private final static Logger logger = LoggerFactory.getLogger(BalanceServiceImpl.class);
    
    @Autowired
    private PBalanceMapper pBalanceMapper;

    @Autowired
    private PUserBalanceLogMapper pUserBalanceLogMapper;
    
    /**
     * 保存余额流水
     * @param userId            用户ID
     * @param balanceType       账本类型
     * @param amount            交易金额
     * @param balanceAmount     交易后余额
     * @param orderId           订单
     * @param tradeType         交易类型
     * @param subject           科目
     * @param subSubject        子科目
     * @param createTime        创建时间
     * @param status            状态
     * @param remark            备注
     * @return
     */
    private Result<String> saveBalanceLogPO(
            long userId, 
            BalanceType balanceType, 
            BigDecimal amount, 
            BigDecimal balanceAmount, 
            String orderId, 
            TradeType tradeType, 
            Subject subject,
            SubSubject subSubject,
            Date createTime,
            int status,
            String remark) {
        
        PUserBalanceLogPO po = new PUserBalanceLogPO();
        
        po.setUserId(userId);
        po.setBalanceType(balanceType.getType());
        po.setAmount(amount);
        po.setBalance(balanceAmount);
        po.setOrderId(orderId);
        po.setTradeType(tradeType.getType());
        po.setSubject(subject.getVal());
        po.setSubSubject(subSubject.getVal());
        po.setCreateTime(createTime);
        po.setStatus(status);
        po.setRemark(remark);
        
        int row = pUserBalanceLogMapper.insert(po);
        
        if(row < 1) {
            return new Result<>(ResultCode.FAILURE, null, "保存余额流水失败");
        }
        
        return new Result<>(ResultCode.SUCCESS);
        
    }
    
    /**
     * 余额增加
     * @param orderId   订单号
     * @param userId    用户ID
     * @param amount    金额
     * @param balanceType       账本类型
     * @param subject           科目
     * @param subSubject        子科目
     * @return
     */
    private void balanceIncrease(String orderId, long userId,
            BigDecimal amount, BalanceType balanceType, Subject subject,
            SubSubject subSubject, String remark) {
        
        logger.info("用户余额增加， orderId:{}, userId:{}, amount:{}, balanceType:{}, subject:{}, subSubject:{}", orderId, userId, amount, balanceType, subject, subject, remark);
        
        //余额变动
        int row = pBalanceMapper.balanceIncrease(userId, balanceType.getType(), amount);
        if(row < 1) {
            logger.error("增加用户余额失败， userId:{}, balanceType:{}", userId, balanceType);
            throw new BalanceException("增加用户余额失败");
        }
        
        Result<BigDecimal> result = this.getUserBalance(userId, balanceType);
        if(!result.isSuccess()) {
            logger.error("查询用户余额失败， userId:{}, balanceType:{}", userId, balanceType);
            throw new BalanceException("查询用户余额失败");
        }
        
        //记录余额流水
        Result<String> result2 = saveBalanceLogPO(userId, balanceType, amount, result.getObject(), orderId, TradeType.IN, subject, subSubject, new Date(), 1, remark);
        if(!result2.isSuccess()) {
            logger.error("保存余额流水失败， userId:{}, balanceType:{}", userId, balanceType);
            throw new BalanceException("保存余额流水失败");
        }
    }

    /**
     * 余额减少
     * @param orderId
     * @param userId
     * @param amount
     * @param balanceType
     * @param subject
     * @param subSubject
     * @param remark
     */
    private void balanceDecrease(String orderId, long userId,
            BigDecimal amount, BalanceType balanceType, Subject subject,
            SubSubject subSubject, String remark) {
        
        logger.info("用户余额减少， orderId:{}, userId:{}, amount:{}, balanceType:{}, subject:{}, subSubject:{}", orderId, userId, amount, balanceType, subject, subject, remark);
        
        //余额减少
        int row = pBalanceMapper.balanceDecrease(userId, balanceType.getType(), amount);
        if(row < 1) {
            logger.error("用户余额不足， userId:{}, balanceType:{}", userId, balanceType);
            throw new BalanceNotEnoughException("转出账户余额不足");
        }
        
        Result<BigDecimal> result = this.getUserBalance(userId, balanceType);
        if(!result.isSuccess()) {
            logger.error("查询用户余额失败， userId:{}, balanceType:{}", userId, balanceType);
            throw new BalanceException("查询用户余额失败");
        }
        
        //记录余额流水
        Result<String> result2 = saveBalanceLogPO(userId, balanceType, amount, result.getObject(), orderId, TradeType.OUT, subject, subSubject, new Date(), 1, remark);
        if(!result2.isSuccess()) {
            logger.error("保存余额流水失败， userId:{}, balanceType:{}", userId, balanceType);
            throw new BalanceException("保存余额流水失败");
        }
        
    }
    
    @Override
    public Result<String> charge(String orderId, long userId,
            BigDecimal amount, BalanceType balanceType) {
        
        logger.info("用户充值  orderId:{}, userId:{}, amount:{}, balanceType:{}", orderId, userId, amount, balanceType);
        
        if(StringUtils.isBlank(orderId) || userId <= 0 || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || balanceType == null) {
            return new Result<>(ResultCode.FAILURE, null, "参数不正确");
        }
        
        //余额变动
        int row = pBalanceMapper.charge(userId, balanceType.getType(), amount, new Date());
        if(row < 1) {
            logger.error("增加用户余额失败， userId:{}, balanceType:{}", userId, balanceType);
            throw new BalanceException("增加用户余额失败");
        }
        
        Result<BigDecimal> result = this.getUserBalance(userId, balanceType);
        if(!result.isSuccess()) {
            logger.error("查询用户余额失败， userId:{}, balanceType:{}", userId, balanceType);
            throw new BalanceException("查询用户余额失败");
        }
        
        //记录余额流水
        Result<String> result2 = saveBalanceLogPO(userId, balanceType, amount, result.getObject(), orderId, TradeType.IN, Subject.CHARGE, SubSubject.CHARGE, new Date(), 1, "用户充值:" + amount);
        if(!result2.isSuccess()) {
            logger.error("保存余额流水失败， userId:{}, balanceType:{}", userId, balanceType);
            throw new BalanceException("保存余额流水失败");
        }
        
        return new Result<>(ResultCode.SUCCESS, orderId);
    }

    @Override
    public Result<String> withdraw(String orderId, long userId,
            BigDecimal amount, BalanceType balanceType){
        
        logger.info("用户提现   orderId:{}, userId:{}, amount:{}, balanceType:{}", orderId, userId, amount, balanceType);
        
        if(StringUtils.isBlank(orderId) || userId <= 0 || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || balanceType == null) {
            return new Result<>(ResultCode.FAILURE, null, "参数不正确");
        }
        
        this.balanceDecrease(orderId, userId, amount, balanceType, Subject.WITHDRAW, SubSubject.WITHDRAW, "用户提现:" + amount);
        
        return new Result<>(ResultCode.SUCCESS, orderId);
    }

    @Override
    public Result<BigDecimal> getUserBalance(long userId,
            BalanceType balanceType) {
        PBalancePO po = pBalanceMapper.getByUserId(userId, balanceType.getType());
        if(po == null) {
            return new Result<>(ResultCode.FAILURE, null, "查询用户余额失败");
        }
        
        return new Result<>(ResultCode.SUCCESS, po.getUsableBalance());
    }

    @Override
    public Result<String> transfer(String orderId, long sourceUserId, long targetUserId,
            BalanceType balanceType, BigDecimal amount, Subject subject, SubSubject subSubject, String remark) {
        
        logger.info("转账   sourceUserId:{}, targetUserId:{}, balanceType:{}, amount:{}， remark:{}", sourceUserId, targetUserId, balanceType, amount, remark);
        
        if(sourceUserId <=0 || targetUserId <= 0 || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || balanceType == null) {
            return new Result<>(ResultCode.FAILURE, null, "参数不正确");
        }
        
        if(StringUtils.isBlank(remark)) {
            logger.info("remark为空，使用默认值");
            remark = "转账";
        }
        
        //转出账户余额递减
        this.balanceDecrease(orderId, sourceUserId, amount, balanceType, subject, subSubject, remark);
        
        //转入账户余额增加
        this.balanceIncrease(orderId, targetUserId, amount, balanceType, subject, subSubject, remark);
        
        return new Result<>(ResultCode.SUCCESS, orderId);
    }

    @Override
    public Result<String> chargeFee(String orderId, long userId,
            BigDecimal fee, BalanceType balanceType) {
        logger.info("充值手续费   orderId:{}, userId:{}, fee:{}, balanceType:{}", orderId, userId, fee, balanceType);
        
        if(StringUtils.isBlank(orderId) || userId <= 0 || fee == null || fee.compareTo(BigDecimal.ZERO) < 0 || balanceType == null) {
            return new Result<>(ResultCode.FAILURE, null, "参数不正确");
        }
        
        if(fee.compareTo(BigDecimal.ZERO) == 0) {
            logger.info("充值手续费为0， 不修改账本，直接返回，orderId:{}", orderId);
            return new Result<>(ResultCode.SUCCESS, orderId, "手续费为0，直接返回");
        }
        
        this.balanceDecrease(orderId, userId, fee, balanceType, Subject.CHARGE_FEE, SubSubject.CHARGE_FEE, "充值手续费：" + fee);
        
        return new Result<>(ResultCode.SUCCESS, orderId);
    }

    @Override
    public Result<String> withdrawFee(String orderId, long userId,
            BigDecimal fee, BalanceType balanceType) {
        logger.info("提现手续费   orderId:{}, userId:{}, fee:{}, balanceType:{}", orderId, userId, fee, balanceType);
        
        if(StringUtils.isBlank(orderId) || userId <= 0 || fee == null || fee.compareTo(BigDecimal.ZERO) < 0 || balanceType == null) {
            return new Result<>(ResultCode.FAILURE, null, "参数不正确");
        }
        
        if(fee.compareTo(BigDecimal.ZERO) == 0) {
            logger.info("提现手续费为0， 不修改账本，直接返回，orderId:{}", orderId);
            return new Result<>(ResultCode.SUCCESS, orderId, "手续费为0，直接返回");
        }
        
        this.balanceDecrease(orderId, userId, fee, balanceType, Subject.WITHDRAW_FEE, SubSubject.WITHDRAW_FEE, "提现手续费：" + fee);
        
        return new Result<>(ResultCode.SUCCESS, orderId);
    }
    

    @Override
    public Result<List<PBalanceStatVO>> queryStatByType(BalanceAccountType type) {
        List<PBalanceStatVO> list = pBalanceMapper.queryStatByType(type.getType());
        for(PBalanceStatVO vo : list) {
            ChannelIdConstant channelIdConstant = ChannelIdConstant.from(vo.getUserId());
            vo.setPayChannel(channelIdConstant);
            WithdrawChannel withdrawChannel = WithdrawChannel.from(vo.getUserId());
            vo.setWithdrawChannel(withdrawChannel);
        }
        return new Result<>(ResultCode.SUCCESS, list);
    }

    @Override
    public List<PBalancePO> listByType(BalanceAccountType bankAccount) {
        return pBalanceMapper.listByType(bankAccount.getType());
    }

    @Override
    public List<PBalancePO> listAll() {
        return pBalanceMapper.listAll();
    }

    @Override
    public Result<String> adjust(String orderId, long userId,
            BigDecimal amount) {
        
        logger.info("调账   orderId:{}, userId:{}, amount:{}", orderId, userId, amount);
        
        if(userId <=0 || amount.compareTo(BigDecimal.ZERO) == 0) {
            return new Result<>(ResultCode.FAILURE, null, "参数不正确");
        }
        
        if(amount.compareTo(BigDecimal.ZERO) > 0) { //加钱
            //转出账户余额递减
            this.balanceIncrease(orderId, userId, amount, BalanceType.E_COIN, Subject.DATA_FIX, SubSubject.DATA_FIX, "调账：" + amount);
        } else { //减钱
            //取绝对值
            amount = amount.abs();
            //转入账户余额增加
            this.balanceDecrease(orderId, userId, amount, BalanceType.E_COIN, Subject.DATA_FIX, SubSubject.DATA_FIX, "调账：-" + amount);
        }
        
        return new Result<>(ResultCode.SUCCESS, orderId);
    }

    @Override
    public Result<String> payAgentAuth(String orderId, long userId,
            BigDecimal amount) {
        
        BalanceType balanceType = BalanceType.E_COIN;
        
        logger.info("用户代扣鉴权  orderId:{}, userId:{}, amount:{}, balanceType:{}", orderId, userId, amount, balanceType);
        
        if(StringUtils.isBlank(orderId) || userId <= 0 || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || balanceType == null) {
            return new Result<>(ResultCode.FAILURE, null, "参数不正确");
        }
        
        this.balanceIncrease(orderId, userId, amount, balanceType, Subject.PAY_AGENT_AUTH, SubSubject.PAY_AGENT_AUTH, "用户代收鉴权:" + amount);
        
        return new Result<>(ResultCode.SUCCESS, orderId);
    }

    @Override
    public Result<String> payAgentAuthFee(String orderId, long userId,
            BigDecimal fee) {
        
        BalanceType balanceType = BalanceType.E_COIN;
        
        logger.info("代收鉴权手续费   orderId:{}, userId:{}, fee:{}, balanceType:{}", orderId, userId, fee, balanceType);
        
        if(StringUtils.isBlank(orderId) || userId <= 0 || fee == null || fee.compareTo(BigDecimal.ZERO) < 0 || balanceType == null) {
            return new Result<>(ResultCode.FAILURE, null, "参数不正确");
        }
        
        if(fee.compareTo(BigDecimal.ZERO) == 0) {
            logger.info("代收鉴权手续费为0， 不修改账本，直接返回，orderId:{}", orderId);
            return new Result<>(ResultCode.SUCCESS, orderId, "手续费为0，直接返回");
        }
        
        this.balanceDecrease(orderId, userId, fee, balanceType, Subject.PAY_AGENT_AUTH_FEE, SubSubject.PAY_AGENT_AUTH_FEE, "代收鉴权手续费：" + fee);
        
        return new Result<>(ResultCode.SUCCESS, orderId);
    }

    @Override
    public Result<String> payAgentCharge(String orderId, long userId,
            BigDecimal amount) {
        
        BalanceType balanceType = BalanceType.E_COIN;
        
        logger.info("用户代扣 orderId:{}, userId:{}, amount:{}, balanceType:{}", orderId, userId, amount, balanceType);
        
        if(StringUtils.isBlank(orderId) || userId <= 0 || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || balanceType == null) {
            return new Result<>(ResultCode.FAILURE, null, "参数不正确");
        }
        
        this.balanceIncrease(orderId, userId, amount, balanceType, Subject.PAY_AGENT_CHARGE, SubSubject.PAY_AGENT_CHARGE, "用户代收:" + amount);
        
        return new Result<>(ResultCode.SUCCESS, orderId);
    }

    @Override
    public Result<String> payAgentChargeFee(String orderId, long userId,
            BigDecimal fee) {
        
        BalanceType balanceType = BalanceType.E_COIN;
        
        logger.info("代收手续费   orderId:{}, userId:{}, fee:{}, balanceType:{}", orderId, userId, fee, balanceType);
        
        if(StringUtils.isBlank(orderId) || userId <= 0 || fee == null || fee.compareTo(BigDecimal.ZERO) < 0 || balanceType == null) {
            return new Result<>(ResultCode.FAILURE, null, "参数不正确");
        }
        
        if(fee.compareTo(BigDecimal.ZERO) == 0) {
            logger.info("代收手续费为0， 不修改账本，直接返回，orderId:{}", orderId);
            return new Result<>(ResultCode.SUCCESS, orderId, "手续费为0，直接返回");
        }
        
        this.balanceDecrease(orderId, userId, fee, balanceType, Subject.PAY_AGENT_CHARGE_FEE, SubSubject.PAY_AGENT_CHARGE_FEE, "代收手续费：" + fee);
        
        return new Result<>(ResultCode.SUCCESS, orderId);
    }

}
