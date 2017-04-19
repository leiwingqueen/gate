package com.elend.gate.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elend.gate.admin.service.WithdrawService;
import com.elend.gate.order.facade.PDepositRequestFacade;
import com.elend.gate.order.facade.PWithdrawRequestFacade;
import com.elend.gate.order.vo.PDepositRequestSearchVO;
import com.elend.gate.order.vo.PDepositRequestVO;
import com.elend.gate.order.vo.PWithdrawRequestSearchVO;
import com.elend.gate.order.vo.PWithdrawRequestVO;
import com.elend.p2p.BaseController;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;
import com.elend.p2p.resource.ClassResourceDesc;
import com.elend.p2p.resource.MethodResourceDesc;

/**
 * 支付订单查询
 * 
 * @author zengyixiang
 */
@ClassResourceDesc(firstCate = "支付订单管理")
@Controller
@Scope("prototype")
public class PayOrderController extends BaseController {
    
    @Autowired
    private PWithdrawRequestFacade pWithdrawRequestFacade;
    
    @Autowired
    private PDepositRequestFacade pDepositRequestFacade;
    
    @Autowired
    private WithdrawService withdrawService;

    @MethodResourceDesc(name = "支付订单查询")
    @RequestMapping(value = "/payOrder/payOrderList.jspx")
    public ModelAndView createSystemProperties(HttpServletRequest request,
            HttpServletResponse response) {
        return new ModelAndView("forward:/WEB-INF/payOrder/depositRequestList.jsp");
    }
    
    @MethodResourceDesc(name = "充值申请数据")
    @RequestMapping(value = "/order/depositRequestListData.do")
    @ResponseBody
    public Result<PageInfo<PDepositRequestVO>> depositRequestListData(HttpServletRequest request, HttpServletResponse response, PDepositRequestSearchVO svo) {
        return pDepositRequestFacade.queryPage(svo);
    }

    @MethodResourceDesc(name = "提现申请查询")
    @RequestMapping(value = "/order/withdrawRequestList.jspx")
    public ModelAndView withdrawRequestPage(HttpServletRequest request,
            HttpServletResponse response) {
        return new ModelAndView("forward:/WEB-INF/payOrder/withdrawRequestList.jsp");
    }
    
    @MethodResourceDesc(name = "提现申请数据")
    @RequestMapping(value = "/order/withdrawRequestListData.do")
    @ResponseBody
    public Result<PageInfo<PWithdrawRequestVO>> transactionListData(HttpServletRequest request, HttpServletResponse response, PWithdrawRequestSearchVO svo) {
        return pWithdrawRequestFacade.queryPage(svo);
    }
    
    @MethodResourceDesc(name = "第三方同步提现数据")
    @RequestMapping(value = "/order/withdrawSync.do")
    @ResponseBody
    public Result<String> withdrawSync(HttpServletRequest request, HttpServletResponse response, String orderId) {
        if(StringUtils.isBlank(orderId)) {
            return new Result<>(ResultCode.FAILURE, null, "请输入订单号");
        }
        return withdrawService.withdrawSync(orderId);
    }
}
