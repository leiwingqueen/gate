package com.elend.gate.web.interceptor;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.elend.gate.context.AccessContext;
import com.elend.gate.order.constant.RequestSource;
import com.elend.p2p.BaseController;
import com.elend.p2p.ResponseUtils;
import com.elend.p2p.Result;
import com.elend.p2p.gson.JSONUtils;
import com.elend.p2p.session.CookieUtil;
import com.elend.p2p.util.SystemConstant;
import com.google.gson.reflect.TypeToken;

/**
 * 网关拦截器
 *      不做任何权限校验，只对访问后缀做处理
 * @author liyongquan 2015年5月21日
 *
 */
public class AuthInterceptor implements HandlerInterceptor {
    private final static Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    private String errorUrl;
    
    private CookieUtil cookieUtil;
    
    public CookieUtil getCookieUtil() {
        return cookieUtil;
    }

    public void setCookieUtil(CookieUtil cookieUtil) {
        this.cookieUtil = cookieUtil;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }

    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object arg2, Exception e)
            throws Exception {
    }

    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object arg2, ModelAndView arg3)
            throws Exception {
        // TODO Auto-generated method stub
    }

    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        logger.info("request uri:{}",request.getRequestURI());
        //保存访问IP和来源信息
        String source = cookieUtil.getCookie(SystemConstant.REQUEST_SOURCE, request);
        RequestSource requestSource=RequestSource.WEB;
        if("mobile".equals(source)){
            requestSource=RequestSource.PHONE;
        }
        String ip=BaseController.getRealIp(request);
        String referer = request.getHeader("referer");
        AccessContext.clear();
        AccessContext.setAccessIp(ip);
        AccessContext.setAccessSource(requestSource.getSource());
        AccessContext.setReferer(referer);
        AccessContext.setUserAgent(request.getHeader("User-Agent"));
        return true;
    }
    
    /**
     * 处理返回信息， 如果是JSPX页面的请求，则转到错误信息; 如果是AJAX请求，则返回JSON数据
     * 
     * @param request
     * @param response
     * @param result
     * @throws IOException
     * @throws ServletException
     */
    @SuppressWarnings("rawtypes")
    public void dealHandle(HttpServletRequest request,
            HttpServletResponse response, Result result, boolean login)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        // 页面请求
        if (uri.lastIndexOf(".jspx") != -1) {
            request.setAttribute("result", result);
                request.setAttribute("error", result.getMessage());
                request.getRequestDispatcher(errorUrl).forward(request,
                                                               response);
        }
        // AJAX请求
        else {
            Type targetType = new TypeToken<Result>() {
            }.getType();
            String json = JSONUtils.toJson(result, targetType, false);
            ResponseUtils.renderJson(response, json);
        }
    }
}
