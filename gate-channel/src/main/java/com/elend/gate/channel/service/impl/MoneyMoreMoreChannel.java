package com.elend.gate.channel.service.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.BankIdConstant;
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.ChargeQueryStatus;
import com.elend.gate.channel.constant.PartnerResultCode;
import com.elend.gate.channel.constant.PayType;
import com.elend.gate.channel.exception.CheckSignatureException;
import com.elend.gate.channel.facade.vo.ChargeCallbackData;
import com.elend.gate.channel.facade.vo.PartnerChargeData;
import com.elend.gate.channel.facade.vo.PartnerSingleQueryChargeData;
import com.elend.gate.channel.service.PayChannelService;
import com.elend.gate.channel.util.MoneyMoreMoreUtil;
import com.elend.gate.conf.facade.MoneyMoreMoreConfig;
import com.elend.gate.util.GateHttpUtil;
import com.elend.gate.util.HttpResult;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.ServiceException;
import com.elend.p2p.gson.JSONUtils;
import com.elend.p2p.util.DateUtil;
@Service
public class MoneyMoreMoreChannel extends PayChannelService{
    private final static Logger logger = LoggerFactory.getLogger(MoneyMoreMoreChannel.class);
    
    //双乾成功返回码
    private final static String SUCCESS_CODE="88";
    
    //双乾成功返回码
    private final static int QUERY_SUCCESS_CODE=200;
    
    //双乾成功返回码
    private final static String QUERY_SUCCESS_RESULT_CODE="1";
    
  //双乾成功返回码
    private final static String QUERY_SUCCESS_RESULT_STATE="1";
    
    //双乾成功的返回码
    private final static String MONEYMOREMORE_FAILURE_CODE = "0";
    private final static String MONEYMOREMORE_SUCCESS_CODE = "1";
    private final static String MONEYMOREMORE_UNHAND_CODE = "2";
    private final static String MONEYMOREMORE_CANCELED_CODE = "3";
    private final static String MONEYMOREMORE_UNRETURN_CODE = "4";
    
    @Autowired
    private MoneyMoreMoreConfig config;
    
    @Override
    public RequestFormData charge(String orderId, PartnerChargeData chargeData) {
        logger.info("双乾充值请求,orderId:{},amount:{}", orderId, chargeData.getAmount());
       
        String amount = String.format("%.2f",
                                      chargeData.getAmount().setScale(2,
                                                      BigDecimal.ROUND_DOWN)); // 支付金额   
         String  paymentType="";
        //不指定银行
         logger.info("bankid {}",chargeData.getBankId());
        if(chargeData.getBankId() != null || chargeData.getBankId() != BankIdConstant.NO_DESIGNATED) {
            //银行ID转换成渠道对应的银行
            String channelBandId=this.getChannelBankId(chargeData.getBankId());            
            paymentType=channelBandId;     //在支付平台选择银行
        } 
              
        String billNo=orderId;
        String merNo=config.getMerNo();
        String returnURL=config.getRetrunReqURL();        
        String md5Key=config.getMd5Key();       
        String mD5info = MoneyMoreMoreUtil.getReqMd5InfoForOnlinePayment(amount, billNo, merNo, returnURL, md5Key);
        RequestFormData form = new RequestFormData();
        form.setRequestUrl(config.getRequestUrl());//"https://www.95epay.cn/sslpayment"
        form.addParam("MerNo", merNo);
        form.addParam("BillNo", billNo);
        form.addParam("Amount", amount);
        form.addParam("ReturnURL", returnURL);
        form.addParam("MD5info", mD5info);
        form.addParam("PayType", "CSPAY"); //CSPAY:网银充值  KJPAY:快捷支付;
        form.addParam("NotifyURL", config.getCallbackUrl());
        form.addParam("PaymentType", paymentType);
        form.addParam("MerRemark", "广州易贷");//商户备注信息.
        form.addParam("products", "充值"); //商户自定义备注信息.
      
        logger.info("双乾充值请求表单生成,form:{}", JSONUtils.toJson(form, false));
        return form;
    }

    @Override
    public PartnerResult<ChargeCallbackData> chargeCallback(
            ChannelIdConstant channelId, Map<String, String> params)
            throws CheckSignatureException, ServiceException {
        logger.info(" moneymoremore charge callback,channelId:{},params:{}", channelId,
                    JSONUtils.toJson(params, false));
        String merNo=formatString(params.get("MerNo"));
        String orderno=formatString(params.get("Orderno"));
        
        String amount=formatString(params.get("Amount"));
        String billNo=formatString(params.get("BillNo"));
        String succeed=formatString(params.get("Succeed"));
        String mD5info=formatString(params.get("MD5info"));       
        String result=formatString(params.get("Result"));
        //String merRemark=formatString(params.get("MerRemark"));
        String[] reqMap=new String[4];
        reqMap[0] =merNo;
        reqMap[1] =billNo;
        reqMap[2] = amount;
        reqMap[3] = succeed;     
       String signStr=MoneyMoreMoreUtil.signMap(reqMap, config.getMd5Key(), "RES");
       logger.info("开始签名验证，签名 singStr {}  传过来的  mD5info{}",signStr,mD5info);
       if (mD5info.equals(signStr.toUpperCase())) {
           logger.info("签名校验成功");
           ChargeCallbackData callbackData = new ChargeCallbackData();
           callbackData.setAmount(new BigDecimal(amount));
           callbackData.setCallbackStr("{\"Result\":\""+result+"\",\"succeed\":\""+succeed+"\"}");
           callbackData.setChannelOrderId(orderno);
           callbackData.setOrderId(billNo);
           if (SUCCESS_CODE.equals(succeed)) { 
               callbackData.setNoticeTime(new Date());
               callbackData.setNotify(true);           
               logger.info("双乾充值交易成功,callbackData:{}", callbackData);
               return new PartnerResult<ChargeCallbackData>(PartnerResultCode.SUCCESS,
                                                     callbackData);
           } else {
               logger.error("双乾充值交易失败,失败返回码:{},callbackData:{}", succeed,
                            callbackData);
               return new PartnerResult<ChargeCallbackData>(PartnerResultCode.FAILURE,
                                                     callbackData, "错误码:"
                                                             + succeed);
           }
       
           
           
        } else {
        logger.error("双乾充值交易签名被篡改.channelId:{},params:{}", channelId,
                     JSONUtils.toJson(params, false));
        throw new CheckSignatureException("双乾充值交易签名被篡改");
      }
       
    }

    @Override
    public ChannelIdConstant getChannelId() {     
        return ChannelIdConstant.MONEY_MORE_MORE;
    }

    @Override
    public PartnerResult<PartnerSingleQueryChargeData> singleQueryCharge(
            ChannelIdConstant channelId, String orderId)
            throws CheckSignatureException, ServiceException {
        logger.info("singleQueryCharge, channelId:{}, orderId:{}", channelId,
                    orderId);
        if (StringUtils.isBlank(orderId)
                || StringUtils.trimToEmpty(orderId).length() > 50) {
            logger.error("无效的订单号:" + orderId);
            throw new ServiceException("无效的订单号:" + orderId);
        }
        
        String merNo=config.getMerNo();
        String md5Key=config.getMd5Key();   

        Map<String, String> reqMap = new HashMap<String, String>();
        reqMap.put("MerNo",merNo); // 商户编号
        reqMap.put("BillNo", orderId); // 签名类型
        reqMap.put("MD5Info", MoneyMoreMoreUtil.getQueryMd5Info(orderId, merNo, md5Key)); // 商户订单号       
        String reqStr = JSONUtils.toJson(reqMap, false);
        logger.info(" 双乾订单查询参数  reqStr{}",reqStr);
        HttpResult httpResult =null;
        try {
            logger.info("双乾订单查询 发送请求...");      
            httpResult = GateHttpUtil.postRequest(config.getRequestQueryUrl(), reqMap, 0, 0);
            logger.info("双乾订单查询 返回数据:{}", httpResult);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        if (httpResult==null) {
            throw new ServiceException("No response.");
        }
        
        if (QUERY_SUCCESS_CODE!=httpResult.getHttpResponseCode()) {
            logger.error("双乾充值 订单号  {} 查询失败  HttpResponseCode {} ",orderId,httpResult.getHttpResponseCode());
            throw new ServiceException("双乾充值 查询失败 ");
        }
        
        SAXBuilder builder = new SAXBuilder();
        Reader in = new StringReader(httpResult.getHttpBody());
        Document read_doc=null;
        try {
            read_doc = builder.build(in);
        } catch (JDOMException e) {      
            logger.error("解析返回报文失败", e);
            throw new ServiceException("解析返回报文失败：" + e.getMessage());
        } catch (IOException e) {
            logger.error("读取返回报文失败", e);
            throw new ServiceException("读取返回报文失败：" + e.getMessage());
        }
        Element root = read_doc.getRootElement();
        Element element=root.getChild("MerInfo");
        if (element==null) {
            logger.error("双乾充值 订单号  {} 查询失败   element is null ",orderId);
            throw new ServiceException("双乾充值 查询失败 ");
        }
      
        String resultCode=element.getAttribute("ResultCode").getValue();
        
        if (!QUERY_SUCCESS_RESULT_CODE.equals(resultCode)) {
            logger.error("双乾充值 订单号  {} 查询失败  HttpResponseCode {} ",orderId,httpResult.getHttpResponseCode());
            throw new ServiceException("双乾充值 查询失败 ");       
        }
        
        String billNo=element.getAttribute("BillNo").getValue();        
        if (!orderId.equals(billNo)) {
            logger.error("双乾充值 订单号  {} 查询失败   订单号异常  查询的订单号orderId  {}  返回的订单号 BillNo {}",orderId,orderId,billNo);
            throw new ServiceException("双乾充值 查询失败 ");       
        
        }
        
        Element selement=element.getChild("OrderInfo");
        
        if (selement==null) {
            logger.error("双乾充值 订单号  {} 查询失败   element is null ",orderId);
            throw new ServiceException("双乾充值 查询失败 ");
        }
      
        String state=selement.getAttribute("State").getValue();        
        String rbillNo=selement.getAttribute("BillNo").getValue();
        String amount=selement.getAttribute("amount").getValue();
        String date=selement.getAttribute("Date").getValue();              
        String mD5ResultInfo=selement.getAttribute("MD5ResultInfo").getValue();
        
        String lmD5ResultInfo=MoneyMoreMoreUtil.getReultInfoMd5Info(rbillNo, date, merNo, state, md5Key);
        
        if (!lmD5ResultInfo.equals(mD5ResultInfo)) {
            logger.error("双乾充值 订单号  {} 查询成功  state {} ",state);
            throw new ServiceException("双乾充值 查询失败 ");              
        
        }      
        if (!QUERY_SUCCESS_RESULT_STATE.equals(state)) {
            logger.error("双乾充值 订单号  {} 查询成功  state {} ",state);
            throw new ServiceException("双乾充值 查询失败 ");       
        
        }
        
        logger.info("state : {}", state);
        PartnerSingleQueryChargeData backDate = new PartnerSingleQueryChargeData();
        
     
        if(MONEYMOREMORE_SUCCESS_CODE.equals(state)) {        //成功
            backDate.setStatus(ChargeQueryStatus.SUCCESS);
        } else if(MONEYMOREMORE_UNHAND_CODE.equals(state)) { //未处理
            backDate.setStatus(ChargeQueryStatus.HANDLING);
        } else if(MONEYMOREMORE_FAILURE_CODE.equals(state)) { //失败
            backDate.setStatus(ChargeQueryStatus.FAILURE);
        } 
         else if(MONEYMOREMORE_CANCELED_CODE.equals(state)) { //取消
           backDate.setStatus(ChargeQueryStatus.CANDELED);
        }
         else if(MONEYMOREMORE_UNRETURN_CODE.equals(state)) { //结果未返回
          backDate.setStatus(ChargeQueryStatus.UNRETURN);
        } else {
                logger.error("未知的支付状态:{}", state);
                throw new ServiceException("未知的支付状态, state:" + state);
        }
        logger.info("查询结束");    
        backDate.setOrderId(orderId);
        backDate.setAmount(new BigDecimal(amount));
        backDate.setCreateTime(DateUtil.strToTime(date, DateUtil.DATE_FORMAT_PATTEN));  
        backDate.setChannelOrderId(orderId);     
        return new PartnerResult<PartnerSingleQueryChargeData>(PartnerResultCode.SUCCESS, backDate);
     
    }

    @Override
    public BigDecimal feeCalculate(BigDecimal amount, PayType payType) {     
            return config.getFeePercentage().multiply(amount).setScale(2, BigDecimal.ROUND_HALF_UP);       
    }

    
    /**
     * 格式化字符串
     * 
     * @param text
     *            --输入文本
     * @return
     */
    private String formatString(String text) {
        if (text == null) {
            return "";
        }
        return text.trim();
    }
}
