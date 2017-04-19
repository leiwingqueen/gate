package com.elend.gate.util;

import java.util.Map;

import com.elend.gate.util.vo.RequestFormData;

/**
 * 用于构建前端的form表单(post请求到托管平台)
 * @author liyongquan
 *
 */
public class FormUtil {
    /**
     * 构建form表单
     * @param formData--表单数据
     * @return
     */
    public static String build(RequestFormData formData){
        return build(formData.getRequestUrl(), formData.getParams());
    }
    
    /**
     * 构建form表单
     * @param redirctUrl--重定向Url
     * @param params--请求参数
     * @return
     */
    public static String build(String redirctUrl,Map<String,String> params){
        StringBuffer buffer=new StringBuffer();
        buffer.append("<form id=\"form1\" name=\"form1\" action=\""+redirctUrl+"\" method=\"post\">");
        for(String key:params.keySet()){
            buffer.append(String.format("<input id=\"%s\" name=\"%s\" value='%s' type=\"hidden\" />", key,key,params.get(key)));
        }
        buffer.append("</form>");
        return buffer.toString();
    }
}
