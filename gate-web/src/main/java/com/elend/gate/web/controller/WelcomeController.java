package com.elend.gate.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.elend.p2p.BaseController;

/**
 * 首页
 * @author liyongquan 2015年5月21日
 *
 */
@Controller
@Scope("prototype")
public class WelcomeController extends BaseController {
    /************************************* .do请求*************************************/
    
    /************************************* 页面跳转 *************************************/
    @RequestMapping(value = "/index.jspx")
    public ModelAndView index(ModelMap model, HttpServletRequest request) {
        return new ModelAndView("forward:/WEB-INF/index.jsp");
    }
}
