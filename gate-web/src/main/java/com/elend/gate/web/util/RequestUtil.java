package com.elend.gate.web.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author liyongquan
 *
 */
public class RequestUtil {
    /**
     * request对象转换为map
     * @param request--httpRequest
     * @return
     */
    @SuppressWarnings({"rawtypes" })
    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            String value=request.getParameter(name);
            map.put(name, value);
        }
        return map;
    }
}
