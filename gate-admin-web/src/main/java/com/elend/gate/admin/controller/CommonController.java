package com.elend.gate.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.elend.gate.admin.constant.ReconciliationConstant;
import com.elend.p2p.BaseController;
import com.elend.p2p.euc.Result;
import com.elend.p2p.euc.ResultCode;
import com.elend.p2p.resource.ClassResourceDesc;
import com.elend.p2p.resource.MethodResourceDesc;
import com.elend.p2p.sdk.logic.CommonHelper;
import com.elend.p2p.sdk.logic.SucEsbHelper;
import com.elend.p2p.sdk.vo.AppVO;
import com.elend.p2p.sdk.vo.MenuVO;
import com.elend.p2p.sdk.vo.User;
import com.elend.p2p.util.Authencation;

@ClassResourceDesc(firstCate = "公共接口")
@Controller
@Scope("prototype")
public class CommonController extends BaseController {
    @Autowired
    private SucEsbHelper sucEsbHelper;

    @Autowired
    private CommonHelper commonHelper;

    // @Autowired
    // private KeyValueSettingService keyValueSettingService;

    @MethodResourceDesc(name = "获取用户信息")
    @RequestMapping(value = "/common/loadUserInfo.do", method = RequestMethod.GET)
    @ResponseBody
    public Result loadUserInfo(HttpServletRequest request,
            HttpServletResponse response) {
        // 从cookie获取用户信息
        Authencation auth = new Authencation(request, response);
        User user = sucEsbHelper.listOneUser(ReconciliationConstant.app_id,
                                             ReconciliationConstant.app_key,
                                             auth.getUserName());
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        result.setObject(user);
        return result;
    }

    @MethodResourceDesc(name = "获取用户App列表信息")
    @RequestMapping(value = "/common/listUserApp.do", method = RequestMethod.GET)
    @ResponseBody
    public Result listUserApp(HttpServletRequest request,
            HttpServletResponse response) {
        // 从cookie获取用户信息
        Authencation auth = new Authencation(request, response);
        List<AppVO> apps = sucEsbHelper.listUserApp(ReconciliationConstant.app_id,
                                                    ReconciliationConstant.app_key,
                                                    auth.getUserName());
        sucEsbHelper.filter(apps, auth.getUserName(), getRealIp(request));
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        result.setObject(apps);
        return result;
    }

    @MethodResourceDesc(name = "获取服务器IP地址")
    @RequestMapping(value = "/common/loadServerIp.do", method = RequestMethod.GET)
    @ResponseBody
    public Result loadServerIp() {
        List<String> ips = commonHelper.getCurServerIps();
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        result.setObject(ips);
        return result;
    }

    @MethodResourceDesc(name = "获取用户菜单权限")
    @RequestMapping(value = "/common/menu.do", method = RequestMethod.GET)
    @ResponseBody
    public Result menu(HttpServletRequest request,
            HttpServletResponse response) {
        // 设置cookie
        Authencation auth = new Authencation(request, response);
        List<MenuVO> menus = sucEsbHelper.listUserMenu(ReconciliationConstant.app_id,
                                                       ReconciliationConstant.app_key,
                                                       auth.getUserName());

        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        result.setObject(menus);
        return result;
    }

    @MethodResourceDesc(name = "用户退出权限")
    @RequestMapping(value = "/admin/logout.do", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request,
            HttpServletResponse response) {
        // deleteCookie(request,response);
        return new ModelAndView(new RedirectView("/welcome.jspx"));
    }

    @MethodResourceDesc(name = "获取用户App列表信息")
    @RequestMapping(value = "/common/listAppUser.do", method = RequestMethod.GET)
    @ResponseBody
    public Result listAppUser(HttpServletRequest request,
            HttpServletResponse response) {
        // 从cookie获取用户信息
        Result result = new Result();
        result.setCode(ResultCode.FAILURE);
        List<User> user = sucEsbHelper.listAppUser(ReconciliationConstant.app_id,
                                                   ReconciliationConstant.app_key);
        if (user == null) {
            result.setMessage("获取用户信息出错");
            return result;
        }
        result.setCode(ResultCode.SUCCESS);
        result.setObject(user);
        return result;
    }
}
