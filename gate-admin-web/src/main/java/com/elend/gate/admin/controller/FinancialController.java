package com.elend.gate.admin.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elend.gate.balance.constant.BalanceAccountType;
import com.elend.gate.balance.constant.BalanceType;
import com.elend.gate.balance.constant.SubSubject;
import com.elend.gate.balance.constant.Subject;
import com.elend.gate.balance.facade.BalanceFacade;
import com.elend.gate.balance.model.PBalancePO;
import com.elend.gate.balance.vo.PBalanceStatVO;
import com.elend.gate.order.facade.OrderFacade;
import com.elend.gate.order.facade.PTransactionLogFacade;
import com.elend.gate.order.vo.PTransactionLogSearchVO;
import com.elend.gate.order.vo.PTransactionLogVO;
import com.elend.gate.order.vo.TotalAssetInfo;
import com.elend.p2p.BaseController;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;
import com.elend.p2p.euc.context.UserInfoContext;
import com.elend.p2p.resource.ClassResourceDesc;
import com.elend.p2p.resource.MethodResourceDesc;

/**
 * 结算
 * @author mgt
 *
 */
@ClassResourceDesc(firstCate = "账本管理")
@Controller
@Scope("prototype")
public class FinancialController extends BaseController {
    
    @Autowired
    private PTransactionLogFacade pTransactionLogFacade;
    
    @Autowired
    private BalanceFacade balanceFacade;
    
    @Autowired
    private OrderFacade orderFacade;
    
    @MethodResourceDesc(name = "资金流水列表页面")
    @RequestMapping(value = "/financial/transactionList.jspx")
    public ModelAndView transactionListPage(HttpServletRequest request,HttpServletResponse response, Long userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        return new ModelAndView("forward:/WEB-INF/financial/transactionList.jsp", map);
    }
    
    @MethodResourceDesc(name = "资金流水列表数据")
    @RequestMapping(value = "/financial/transactionListData.do")
    @ResponseBody
    public Result<PageInfo<PTransactionLogVO>> transactionListData(HttpServletRequest request, HttpServletResponse response, PTransactionLogSearchVO svo) {
        return pTransactionLogFacade.queryPage(svo);
    }
    
    @MethodResourceDesc(name = "账本列表页面")
    @RequestMapping(value = "/financial/balanceList.jspx")
    public ModelAndView balanceListPage(HttpServletRequest request,HttpServletResponse response) {
        return new ModelAndView("forward:/WEB-INF/financial/balanceList.jsp");
    }
    
    @MethodResourceDesc(name = "账本列表数据")
    @RequestMapping(value = "/financial/balanceListData.do")
    @ResponseBody
    public Result<List<PBalanceStatVO>> balanceListData(HttpServletRequest request,HttpServletResponse response, BalanceAccountType type) {
        if(type == null) {
            return new Result<>(ResultCode.FAILURE, null, "请输入账本类型");
        }
        return balanceFacade.queryStatByType(type);
    }
    
    @MethodResourceDesc(name = "转账功能页面")
    @RequestMapping(value = "/financial/transfer.jspx")
    public ModelAndView transferPage(HttpServletRequest request,HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        
        //查询账户列表
        List<PBalancePO> list = balanceFacade.listByType(BalanceAccountType.BANK_ACCOUNT);
        map.put("bankList", list);
        list = balanceFacade.listByType(BalanceAccountType.CHANNEL_ACCOUNT);
        map.put("channelList", list);
        
        return new ModelAndView("forward:/WEB-INF/financial/transfer.jsp", map);
    }
    
    @MethodResourceDesc(name = "查询账本余额")
    @RequestMapping(value = "/financial/queryUserBalance.do")
    @ResponseBody
    public Result<BigDecimal> balanceListData(HttpServletRequest request,HttpServletResponse response, long userId) {
        if(userId < 0) {
            return new Result<>(ResultCode.FAILURE, null, "请输入用户ID");
        }
        return balanceFacade.getUserBalance(userId, BalanceType.E_COIN);
    }
    
    @MethodResourceDesc(name = "渠道提现到银行卡")
    @RequestMapping(value = "/financial/transferOut.do")
    @ResponseBody
    public Result<String> transferOut(HttpServletRequest request,HttpServletResponse response, long outUserId, long inUserId, BigDecimal amount) {
        if(outUserId < 0) {
            return new Result<>(ResultCode.FAILURE, null, "请选择转出账号");
        }
        if(inUserId < 0) {
            return new Result<>(ResultCode.FAILURE, null, "请选择转入账号");
        }
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new Result<>(ResultCode.FAILURE, null, "金额必须大于0");
        }

        log.info("{}操作渠道提现到银行卡， 转出：{}， 转入：{}， 金额:{}", UserInfoContext.getUserId(), outUserId, inUserId, amount);
        
        Result<String> result = orderFacade.transfer(outUserId, inUserId, amount, Subject.TRANSFER_BANK, SubSubject.TRANSFER_BANK, "渠道提现到银行卡：" + amount);
        
        log.info("结果:{}", result);
        
        return result;
    }
    
    @MethodResourceDesc(name = "资金转入渠道")
    @RequestMapping(value = "/financial/transferIn.do")
    @ResponseBody
    public Result<String> transferIn(HttpServletRequest request,HttpServletResponse response, long outUserId, long inUserId, BigDecimal amount) {
        if(outUserId < 0) {
            return new Result<>(ResultCode.FAILURE, null, "请选择转出账号");
        }
        if(inUserId < 0) {
            return new Result<>(ResultCode.FAILURE, null, "请选择转入账号");
        }
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new Result<>(ResultCode.FAILURE, null, "金额必须大于0");
        }

        log.info("{}操作资金转入渠道， 转出：{}， 转入：{}， 金额:{}", UserInfoContext.getUserId(), outUserId, inUserId, amount);
        
        Result<String> result = orderFacade.transfer(outUserId, inUserId, amount, Subject.TRANSFER_BANK, SubSubject.TRANSFER_BANK, "资金转入渠道：" + amount);
        
        log.info("结果:{}", result);
        
        return result;
    }
    
    @MethodResourceDesc(name = "调账页面")
    @RequestMapping(value = "/financial/adjustAccount.jspx")
    public ModelAndView adjustAccountPage(HttpServletRequest request,HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        
        //查询账户列表
        List<PBalancePO> list = balanceFacade.listAll();
        map.put("balanceList", list);
        
        return new ModelAndView("forward:/WEB-INF/financial/adjust.jsp", map);
    }
    
    @MethodResourceDesc(name = "调账动作")
    @RequestMapping(value = "/financial/adjustAccount.do")
    @ResponseBody
    public Result<String> adjustAccount(HttpServletRequest request,HttpServletResponse response, Long userId, BigDecimal amount) {
        if(userId == null || userId < 0) {
            return new Result<>(ResultCode.FAILURE, null, "请选择调账的账号");
        }
        if(amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            return new Result<>(ResultCode.FAILURE, null, "请输入调账金额");
        }

        log.info("{}操作资金调账， 账号：{}， 金额:{}", UserInfoContext.getUserId(), userId, amount);
        
        Result<String> result = orderFacade.adjust(userId, amount);
        
        log.info("调账结果:{}", result);
        
        return result;
    }
    
    
    @MethodResourceDesc(name = "提现总额、充值总额")
    @RequestMapping(value = "/financial/getTransactionTotal.do")
    @ResponseBody
    public Result<TotalAssetInfo> getTransactionTotal(HttpServletRequest request, HttpServletResponse response, PTransactionLogSearchVO svo) {
        return pTransactionLogFacade.getTotal(svo);
    }
}
