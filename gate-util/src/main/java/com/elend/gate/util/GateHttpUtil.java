package com.elend.gate.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elend.gate.exception.NetworkException;
import com.elend.p2p.util.HttpUtils;
/**
 * 网关用httpUtil
 * @author liyongquan 2015年6月4日
 *
 */
public class GateHttpUtil extends HttpUtils{
    private static final Logger logger = LoggerFactory.getLogger(GateHttpUtil.class);
    
    /**
     * 发送http post请求
     * @param url--url
     * @param map--请求参数
     * @param connectionTimeout--连接超时ms
     * @param readTimeout--读超时ms
     * @return
     */
    public static HttpResult postRequest(String url, Map<String, String> map,int connectionTimeout,int readTimeout) {
        logger.info("post request url :" + url);
        
        if (logger.isDebugEnabled()) {
            logger.info("get data url :" + url + "?" + map);
        }
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeout);
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, readTimeout);
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, map.get(key)));
        }
        HttpResult result=new HttpResult();
        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httpost);
            StatusLine status=response.getStatusLine();
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity);
            logger.info("{} request result:{}", url, body);
            httpclient.getConnectionManager().shutdown();
            result.setHttpResponseCode(status.getStatusCode());
            result.setHttpBody(body);
            logger.info("{}点对点通知结果:{}", url,result.toString());
            return result;
        }catch(ClientProtocolException e){
            logger.error("生成HTTP POST请求异常." + e.getMessage(), e);
            result.setHttpResponseCode(503);
            result.setHttpBody("生成HTTP POST请求异常." + e.getMessage());
            return result;
        }catch (IOException e) {
            logger.error("生成HTTP POST请求异常." + e.getMessage(), e);
            result.setHttpResponseCode(504);
            result.setHttpBody("生成HTTP POST请求异常." + e.getMessage());
            return result;
        }catch (Exception e) {
            logger.error("生成HTTP POST请求异常." + e.getMessage(), e);
            result.setHttpResponseCode(505);
            result.setHttpBody("生成HTTP POST请求异常." + e.getMessage());
            return result;
        }
    }
    
    /**
     * 发送http post请求
     * @param url--url
     * @param map--请求参数
     * @return
     */
    public static HttpResult postRequest(String url, Map<String, String> map){
        return postRequest(url, map, 3000, 3000);
    }
    
    /**
     * 发送post请求(参数直接写入http body)
     * @param url  请求地址
     * @param param 请求的字符串
     * @param charSet 编码
     * @param contentType  内容格式 application/json
     * @param connectionTimeout 连接超时
     * @param readTimeout 读取超时
     * @return
     * @throws NetworkException
     */
    public static String doPost(String url, String param, String charSet, String contentType, int connectionTimeout, int readTimeout)throws NetworkException {
        URL localURL;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader reader = null;
        InputStreamReader inputStreamReader = null;
        StringBuffer resultBuffer = new StringBuffer();
        try {
            localURL = new URL(url);
            URLConnection connection = localURL.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            httpURLConnection.setDoOutput(true); // 设置连接输出流为true,默认false (post
                                                 // 请求是以流的方式隐式的传递参数)
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Accept-Charset", charSet);
            /*httpURLConnection.setRequestProperty("Content-Type",
                                                 "application/json");*/
            if(StringUtils.isNotBlank(contentType)) {
            	httpURLConnection.setRequestProperty("Content-Type", contentType);
            } else { //默认json格式
            	httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=" + charSet);
            }
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(param.length()));
            if(connectionTimeout > 0) {
            	httpURLConnection.setConnectTimeout(connectionTimeout);
            } else { //默认连接超时时间3s
            	httpURLConnection.setConnectTimeout(3000);
            }
            if(readTimeout > 0) {
            	httpURLConnection.setReadTimeout(readTimeout);
            }
            
            outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), charSet);

            outputStreamWriter.write(param.toString());
            outputStreamWriter.flush();
            if (httpURLConnection.getResponseCode()!=200) {
                logger.error("HTTP Request is not success, Response code is "
                                            + httpURLConnection.getResponseCode());
                throw new NetworkException(
                                    "HTTP Request is not success, Response code is "
                                            + httpURLConnection.getResponseCode());
            }
            inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream(), charSet);
            reader = new BufferedReader(inputStreamReader);
            String tempLine = null;
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
        } catch (MalformedURLException e) {
            throw new NetworkException("url格式错误,"+e.getMessage());
        } catch (IOException e) {
            throw new NetworkException("访问资源失败,"+e.getMessage());
        }finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    logger.error("关闭资源异常...",e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("关闭资源异常...",e);
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    logger.error("关闭资源异常...",e);
                }
            }
        }
        return resultBuffer.toString();
    }
}
