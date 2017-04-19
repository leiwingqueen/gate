package com.elend.gate.admin.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elend.gate.admin.vo.BankPayLimit;
import com.elend.gate.balance.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.BankIdConstant;
import com.elend.gate.channel.service.CBankIdConfigService;
import com.elend.gate.channel.vo.CBankIdConfigSearchVO;
import com.elend.gate.channel.vo.CBankIdConfigVO;
import com.elend.gate.conf.model.SBankPayLimitPO;
import com.elend.gate.conf.service.SBankPayLimitService;
import com.elend.gate.conf.vo.SBankPayLimitSearchVO;
import com.elend.gate.conf.vo.SBankPayLimitVO;
import com.elend.p2p.BaseController;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;
import com.elend.p2p.euc.context.UserInfoContext;
import com.elend.p2p.resource.ClassResourceDesc;
import com.elend.p2p.resource.MethodResourceDesc;

@ClassResourceDesc(firstCate = "银行配置维护")
@Controller
@Scope("prototype")
public class BankConfigController extends BaseController {

    @Autowired
    private SBankPayLimitService limitService;
    
    @Autowired
    private CBankIdConfigService cBankIdConfigService;
    

    @MethodResourceDesc(name = "银行限额列表")
    @RequestMapping(value = "/bank/payLimitList.jspx")
    public ModelAndView payLimitListPage(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("forward:/WEB-INF/bankConfig/payLimitList.jsp");
    }
    
    @MethodResourceDesc(name = "添加银行限额")
    @RequestMapping(value = "/bank/payLimitAdd.jspx")
    public ModelAndView payLimitAdd(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("forward:/WEB-INF/bankConfig/payLimitAdd.jsp");
    }
    
    @MethodResourceDesc(name = "充值申请数据")
    @RequestMapping(value = "/bank/payLimitList.do")
    @ResponseBody
    public Result<PageInfo<BankPayLimit>> payLimitList(HttpServletRequest request, HttpServletResponse response, SBankPayLimitSearchVO svo) {
        
        Result<PageInfo<SBankPayLimitVO>> payResult = limitService.list(svo);
        
        if(!payResult.isSuccess()) {
            return new Result<>(ResultCode.FAILURE, null, payResult.getMessage());
        }

        List<BankPayLimit> list = new ArrayList<>();
        
        for(SBankPayLimitVO vo : payResult.getObject().getList()) {
            BankPayLimit bankPayLimit = new BankPayLimit();
            bankPayLimit.setVo(vo);
            if(StringUtils.isNotBlank(vo.getBankId())) {
                bankPayLimit.setBank(BankIdConstant.from(vo.getBankId()).getDesc());
            }
            list.add(bankPayLimit);
        }
        
        PageInfo<BankPayLimit> page = new PageInfo<>();
        page.setList(list);
        page.setCount(payResult.getObject().getCount());
        page.setPage(payResult.getObject().getPage());
        page.setPageCount(payResult.getObject().getPageCount());

        return new Result<>(ResultCode.SUCCESS, page);
    }
    
    @MethodResourceDesc(name = "添加银行限额")
    @RequestMapping(value = "/bank/payLimitAdd.do")
    @ResponseBody
    public Result<SBankPayLimitVO> payLimitAdd(HttpServletRequest request, HttpServletResponse response, SBankPayLimitPO po) {
        log.info("admin:{}, 添加或更新银行限额, vo:{}", UserInfoContext.getUserId(), po);
        Result<SBankPayLimitVO> result = limitService.save(po);
        log.info("result:{}", result);
        return result;
    }
    
    @MethodResourceDesc(name = "获取银行限额")
    @RequestMapping(value = "/bank/payLimitGet.do")
    @ResponseBody
    public Result<SBankPayLimitVO> payLimitGet(HttpServletRequest request, HttpServletResponse response, Integer id) {
        if(id == null || id < 0) {
            return new Result<>(ResultCode.FAILURE, null, "请输入记录的ID");
        }
        Result<SBankPayLimitVO> result = limitService.get(id);
        return result;
    }
    
    @MethodResourceDesc(name = "删除银行限额")
    @RequestMapping(value = "/bank/payLimitDelete.do")
    @ResponseBody
    public Result<Integer> payLimitDelete(HttpServletRequest request, HttpServletResponse response, Integer id) {
        if(id == null || id < 0) {
            return new Result<>(ResultCode.FAILURE, null, "请输入记录的ID");
        }
        log.info("admin:{}, 删除银行限额, id:{}", UserInfoContext.getUserId(), id);
        Result<Integer> result = limitService.delete(id);
        log.info("result:{}", result);
        return result;
    }
    
    @MethodResourceDesc(name = "充值银行配置")
    @RequestMapping(value = "/bank/payBankList.jspx")
    public ModelAndView payBankListPage(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("forward:/WEB-INF/bankConfig/payBankList.jsp");
    }
    
    @MethodResourceDesc(name = "充值银行列表数据")
    @RequestMapping(value = "/bank/payBankList.do")
    @ResponseBody
    public Result<PageInfo<CBankIdConfigVO>> payBankList(HttpServletRequest request, HttpServletResponse response, CBankIdConfigSearchVO svo) {
        
        //只查询支付渠道
        List<String> channelList = new ArrayList<>();
        for(ChannelIdConstant channel : ChannelIdConstant.values()) {
            channelList.add(channel.name());
        }
        channelList.remove(ChannelIdConstant.NO_DESIGNATED.name());
        svo.setChannelList(channelList);
        
        Result<PageInfo<CBankIdConfigVO>> result = cBankIdConfigService.list(svo);
        
        return result;
    }
}
