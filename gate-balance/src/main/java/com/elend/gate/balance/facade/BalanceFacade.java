package com.elend.gate.balance.facade;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.balance.constant.BalanceAccountType;
import com.elend.gate.balance.constant.BalanceType;
import com.elend.gate.balance.constant.SubSubject;
import com.elend.gate.balance.constant.Subject;
import com.elend.gate.balance.model.PBalancePO;
import com.elend.gate.balance.service.BalanceService;
import com.elend.gate.balance.vo.PBalanceStatVO;
import com.elend.p2p.Result;

/**
 * 账本门面类
 * @author mgt
 *
 */
@Component
public class BalanceFacade {
    @Autowired
    private BalanceService balanceService;
    
    /**
     * 充值
     * @param orderId   订单号
     * @param userId    用户ID
     * @param amount    金额
     * @param balanceType       账本类型
     * @return
     */
    public Result<String> charge(String orderId, long userId, BigDecimal amount, BalanceType balanceType) {
        return balanceService.charge(orderId, userId, amount, balanceType);
    }
    
    /**
     * 充值手续费
     * @param orderId   订单号
     * @param userId    用户ID
     * @param fee       手续费
     * @param balanceType       账本类型
     * @return
     */
    public Result<String> chargeFee(String orderId, long userId, BigDecimal fee, BalanceType balanceType) {
        return balanceService.chargeFee(orderId, userId, fee, balanceType);
    }
    
    /**
     * 提现
     * @param orderId   订单号
     * @param userId    用户ID
     * @param amount    金额
     * @param balanceType       账本类型
     * @return
     */
    public Result<String> withdraw(String orderId, long userId, BigDecimal amount, BalanceType balanceType) {
        return balanceService.withdraw(orderId, userId, amount, balanceType);
    }
    
    /**
     * 提现手续费
     * @param orderId   订单号
     * @param userId    用户ID
     * @param fee       手续费
     * @param balanceType       账本类型
     * @return
     */
    public Result<String> withdrawFee(String orderId, long userId, BigDecimal fee, BalanceType balanceType) {
        return balanceService.withdrawFee(orderId, userId, fee, balanceType);
    }
    
    /**
     * 查询用户余额
     * @param userId
     * @param balanceType
     * @return
     */
    public Result<BigDecimal> getUserBalance(long userId, BalanceType balanceType) {
        return balanceService.getUserBalance(userId, balanceType);
    }
    
    /**
     * 转账
     * @param sourceUserId      转出账户
     * @param orderId           订单号
     * @param targetUserId      转入账户
     * @param balanceType       账本类型
     * @param amount            金额
     * @param subject           科目
     * @param subSubject        子科目
     * @param remark            备注
     * @return
     */
    public Result<String> transfer(String orderId, long sourceUserId, long targetUserId, BalanceType balanceType, BigDecimal amount, Subject subject, SubSubject subSubject, String remark) {
        return balanceService.transfer(orderId, sourceUserId, targetUserId, balanceType, amount, subject, subSubject, remark);
    }
    
    /**
     * 按类型查询账本的统计信息
     * @param type
     * @return
     */
    public Result<List<PBalanceStatVO>> queryStatByType(
            BalanceAccountType type) {
        return balanceService.queryStatByType(type);
    }

    /**
     * 按类型查询
     * @param bankAccount
     * @return
     */
    public List<PBalancePO> listByType(BalanceAccountType bankAccount) {
        return balanceService.listByType(bankAccount);
    }

    /**
     * 查询所有账本
     * 
     * @return
     */
    public List<PBalancePO> listAll() {
        return balanceService.listAll();
    }

    /**
     * 调账
     * @param orderId
     * @param userId
     * @param amount
     * @return
     */
    public Result<String> adjust(String orderId, long userId,
            BigDecimal amount) {
        return balanceService.adjust(orderId, userId, amount);
    }

    /**
     * 代扣鉴权余额变动
     * @param orderId
     * @param balanceUserId
     * @param authAmt
     * @param eCoin
     * @return
     */
    public Result<String> payAgentAuth(String orderId, long userId,
            BigDecimal amount) {
        return balanceService.payAgentAuth(orderId, userId, amount);
    }

    /**
     * 代收鉴权手续费
     * @param orderId
     * @param balanceUserId
     * @param fee
     * @return
     */
    public Result<String> payAgentAuthFee(String orderId, long userId,
            BigDecimal fee) {
        return balanceService.payAgentAuthFee(orderId, userId, fee);
    }

    /**
     * 代收余额变动
     * @param orderId
     * @param balanceUserId
     * @param amount
     * @return
     */
    public Result<String> payAgentCharge(String orderId, long userId,
            BigDecimal amount) {
        return balanceService.payAgentCharge(orderId, userId, amount);
    }

    /**
     * 代收手续费
     * @param orderId
     * @param balanceUserId
     * @param fee
     * @return
     */
    public Result<String> payAgentChargeFee(String orderId,
            long userId, BigDecimal fee) {
        return balanceService.payAgentChargeFee(orderId, userId, fee);
    }
}
