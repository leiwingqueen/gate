package com.elend.gate.balance.service;

import java.math.BigDecimal;
import java.util.List;

import com.elend.gate.balance.constant.BalanceAccountType;
import com.elend.gate.balance.constant.BalanceType;
import com.elend.gate.balance.constant.SubSubject;
import com.elend.gate.balance.constant.Subject;
import com.elend.gate.balance.model.PBalancePO;
import com.elend.gate.balance.vo.PBalanceStatVO;
import com.elend.p2p.Result;

/**
 * 账本服务接口
 * @author mgt
 *
 */
public interface BalanceService {
    
    /**
     * 充值
     * @param orderId   订单号
     * @param userId    用户ID
     * @param amount    金额
     * @param balanceType       账本类型
     * @return
     */
    public Result<String> charge(String orderId, long userId, BigDecimal amount, BalanceType balanceType);
    
    /**
     * 提现
     * @param orderId   订单号
     * @param userId    用户ID
     * @param amount    金额
     * @param balanceType       账本类型
     * @return
     */
    public Result<String> withdraw(String orderId, long userId, BigDecimal amount, BalanceType balanceType);
    
    /**
     * 查询用户余额
     * @param userId
     * @param balanceType
     * @return
     */
    public Result<BigDecimal> getUserBalance(long userId, BalanceType balanceType);
    
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
    public Result<String> transfer(String orderId, long sourceUserId, long targetUserId, BalanceType balanceType, BigDecimal amount, Subject subject, SubSubject subSubject, String remark);

    /**
     * 充值手续费
     * @param orderId
     * @param userId
     * @param fee
     * @param balanceType
     * @return
     */
    public Result<String> chargeFee(String orderId, long userId,
            BigDecimal fee, BalanceType balanceType);

    /**
     * 提现手续费
     * @param orderId
     * @param userId
     * @param fee
     * @param balanceType
     * @return
     */
    public Result<String> withdrawFee(String orderId, long userId,
            BigDecimal fee, BalanceType balanceType);
    
    /**
     * 按类型查询账本的统计信息
     * @param type
     * @return
     */
    Result<List<PBalanceStatVO>> queryStatByType(BalanceAccountType type);

    /**
     * 按类型查询
     * @param bankAccount
     * @return
     */
    public List<PBalancePO> listByType(BalanceAccountType bankAccount);

    /**
     * 查询所有的账本
     * @return
     */
    public List<PBalancePO> listAll();

    /**
     * 调账
     * @param orderId
     * @param userId
     * @param amount
     * @return
     */
    public Result<String> adjust(String orderId, long userId,
            BigDecimal amount);

    /**
     * 代扣鉴权余额变动
     * @param orderId
     * @param userId
     * @param amount
     * @return
     */
    public Result<String> payAgentAuth(String orderId, long userId,
            BigDecimal amount);

    /**
     * 代收鉴权手续费
     * @param orderId
     * @param userId
     * @param fee
     * @return
     */
    public Result<String> payAgentAuthFee(String orderId, long userId,
            BigDecimal fee);

    /**
     * 代收余额变动
     * @param orderId
     * @param userId
     * @param amount
     * @return
     */
    public Result<String> payAgentCharge(String orderId, long userId,
            BigDecimal amount);

    /**
     * 代收手续费
     * @param orderId
     * @param userId
     * @param fee
     * @return
     */
    public Result<String> payAgentChargeFee(String orderId, long userId,
            BigDecimal fee);

}
