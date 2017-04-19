package com.elend.gate.channel.util;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 双乾充值的交互的一些工具方法
 * @author tanzl
 */
public class MoneyMoreMoreUtil {
    
    private final static Logger logger = LoggerFactory.getLogger(MoneyMoreMoreUtil.class);
    
    /** 加密需要md5的参数 支付用
     * @param amount
     * @param billNo
     * @param MerNo
     * @param returnURL
     * @param md5Key
     * @return
     */
    public static String getReqMd5InfoForOnlinePayment(String amount,String billNo,String MerNo,String returnURL,String md5Key) {       
        StringBuffer sValue = new StringBuffer();
        // 该笔订单总金额(元,精确到 小数两位).
        sValue.append("Amount="+amount);
        // 订单号：数字，字母， 下划线，竖划线，中划线
        sValue.append("&BillNo="+billNo);
        //商户ID.
        sValue.append("&MerNo="+MerNo);
        // 商户前台页面跳转通知URL.
        sValue.append("&ReturnURL="+returnURL);
         //md5Key 参数加密串
        logger.info("sValue "+sValue.toString() +"  md5Key "+ md5Key);      
        String bmd5Info=sValue.toString()+"&"+MoneyMoreMoreMD5.getMD5ofStr(md5Key.trim()).toUpperCase();
        logger.info("bmd5Info   "+bmd5Info);        
        String md5Info=MoneyMoreMoreMD5.getMD5ofStr(bmd5Info).toUpperCase();       
        logger.info("md5Info   "+md5Info);
        
        return md5Info;
    }
    
    
    
    /** 加密需要md5的参数
     * @param amount
     * @param billNo
     * @param MerNo
     * @param returnURL
     * @param md5Key
     * @return
     */
    public static String getQueryMd5Info(String billNo,String MerNo,String md5Key) {       
        StringBuffer sValue = new StringBuffer();
        // 订单号：数字，字母， 下划线，竖划线，中划线
        sValue.append("BillNo="+billNo);
        //商户ID.
        sValue.append("&MerNo="+MerNo);
         //md5Key 参数加密串
         
        String bmd5Info=sValue.toString()+"&"+MoneyMoreMoreMD5.getMD5ofStr(md5Key.trim()).toUpperCase();
        logger.info("bmd5Info   "+bmd5Info);        
        String md5Info=MoneyMoreMoreMD5.getMD5ofStr(bmd5Info).toUpperCase();       
        logger.info("md5Info   "+md5Info);
        
        return md5Info;
    }
    
    
    
    /** 加密需要md5的参数
     * @param amount
     * @param billNo
     * @param MerNo
     * @param returnURL
     * @param md5Key
     * @return
     */
    public static String getReultInfoMd5Info(String billNo,String date,String merNo,String state,String md5Key) {     
        StringBuffer sValue = new StringBuffer();
        // 订单号：数字，字母， 下划线，竖划线，中划线
        sValue.append("BillNo="+billNo);
        //商户ID.
        sValue.append("&Date="+date);
         //md5Key 参数加密串
        sValue.append("&MerNo="+merNo);
        //md5Key 参数加密串
        
        sValue.append("&State="+state);
        //md5Key 参数加密串
        
        String bmd5Info=sValue.toString()+"&"+MoneyMoreMoreMD5.getMD5ofStr(md5Key.trim()).toUpperCase();
        logger.info("bmd5Info   "+bmd5Info);        
        String md5Info=MoneyMoreMoreMD5.getMD5ofStr(bmd5Info).toUpperCase();       
        logger.info("md5Info   "+md5Info);
        
        return md5Info;
    }
    
    // public enum SignType
    // {
    // REQ, RES
    // };
    
    public static String signMap(String[] md5Map, String securityKey, String type)
    {
            String[] md5ReqMap = new String[]
            { "MerNo", "BillNo", "Amount", "ReturnURL" };
            String[] md5ResMap = new String[]
            { "MerNo", "BillNo", "Amount", "Succeed" };
            Map<String, String> map = new TreeMap<String, String>();
            if (type.equals("REQ"))
            {
                    for (int i = 0; i < md5ReqMap.length; i++)
                    {
                            map.put(md5ReqMap[i], md5Map[i]);
                    }
            }
            else if (type.equals("RES"))
            {
                    for (int i = 0; i < md5ResMap.length; i++)
                    {
                            map.put(md5ResMap[i], md5Map[i]);
                    }
            }
            
          //  MoneyMoreMoreMD5 md5 = new MoneyMoreMoreMD5();
            
            String strBeforeMd5 = joinMapValue(map, '&') + MoneyMoreMoreMD5.getMD5ofStr(securityKey);
            System.out.println(strBeforeMd5);
            
            return MoneyMoreMoreMD5.getMD5ofStr(strBeforeMd5);
            
    }
    
    public static String joinMapValue(Map<String, String> map, char connector)
    {
            StringBuffer b = new StringBuffer();
            for (Map.Entry<String, String> entry : map.entrySet())
            {
                    b.append(entry.getKey());
                    b.append('=');
                    if (entry.getValue() != null)
                    {
                            b.append(entry.getValue());
                    }
                    b.append(connector);
            }
            return b.toString();
    }
    
}
