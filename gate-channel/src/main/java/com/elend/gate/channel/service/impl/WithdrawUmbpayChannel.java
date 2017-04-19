package com.elend.gate.channel.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.PartnerResultCode;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.constant.WithdrawStatus;
import com.elend.gate.channel.exception.CheckSignatureException;
import com.elend.gate.channel.facade.vo.PartnerWithdrawData;
import com.elend.gate.channel.facade.vo.WithdrawCallbackData;
import com.elend.gate.channel.facade.vo.WithdrawSingleQueryData;
import com.elend.gate.channel.service.WithdrawChannelService;
import com.elend.gate.channel.service.vo.umbpay.Header;
import com.elend.gate.channel.service.vo.umbpay.RequestMessage;
import com.elend.gate.channel.service.vo.umbpay.SingleWithdrawCallbackRspMessage;
import com.elend.gate.channel.service.vo.umbpay.SingleWithdrawQueryRequestBody;
import com.elend.gate.channel.service.vo.umbpay.SingleWithdrawQueryRspMessage;
import com.elend.gate.channel.service.vo.umbpay.SingleWithdrawRequestBody;
import com.elend.gate.channel.service.vo.umbpay.SingleWithdrawRspMessage;
import com.elend.gate.conf.data.CityData;
import com.elend.gate.conf.facade.WithdrawUmbpayConfig;
import com.elend.gate.conf.facade.vo.SCity;
import com.elend.gate.conf.facade.vo.SProvince;
import com.elend.gate.util.XMLUtils;
import com.elend.p2p.ServiceException;
import com.elend.p2p.util.DateUtil;
import com.elend.p2p.util.HttpUtils;
import com.elend.p2p.util.OrderIdHelper;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;

/**
 * 宝易互通提现渠道
 * @author mgt
 */
@Service
public class WithdrawUmbpayChannel extends WithdrawChannelService {
    
    private static Logger logger = LoggerFactory.getLogger(WithdrawUmbpayChannel.class);
    
    /**
     * 版本号
     */
    public final static String VERSION = "1.0.0";
    /**
     * 消息类型：请求
     */
    public final static String MSG_TYPE_REQUEST = "0001";
    /**
     * 消息类型：返回
     */
    public final static String MSG_TYPE_RESPONSE = "0002";
    /**
     * 渠道代号：民生体系外商户填 99
     */
    public final static String CHANNEL_NO = "99";
    /**
     * 币种 CNY人民币
     */
    public final static String CURTYPE = "CNY";
    
    /**编码*/
    public static final String CHAR_SET = "UTF-8";
    
    /**
     * 成功接收通知返回 S
     */
    public static final String RET_CODE_SUCCESS = "C000000000";
    /**返回码，处理中*/
    public static final String RET_CODE_HANDLING = "W000000000";
    /**订单号不存在*/
    public static final String RECORD_NOT_EXIST_CODE = "W000000001";
    /**交易状态：受理成功（非实时付收时返回）*/
    public static final String TRANSTATE_ACCEPT = "00";
    /**交易状态：代付成功（实时付收时返回）*/
    public static final String TRANSTATE_SUCCESS = "01";
    /**交易状态：代付成功（实时付收时返回）*/
    public static final String TRANSTATE_HANDLING = "02";
    /**交易状态：代付失败*/
    public static final String TRANSTATE_FAILURE = "03";
    
    @Autowired
    private WithdrawUmbpayConfig config;
    
    private DesPropertiesEncoder desEncoder = new DesPropertiesEncoder();
    
    /**
     * 交易代码
     * @author mgt
     *
     */
    public static final class TradeCode{
        /**
         * 单笔打款
         */
        public static final String SINGLE_WITHDRAW = "CP0003";
        /**
         * 单笔打款结果查询
         */
        public static final String SINGLE_WITHDRAW_QUERY = "CP0004";
        /**
         * 单笔单款
         */
        public static final String SINGLE_WITHDRAW_CALLBACK = "CP0006";
    }
    
    /***
     * 格式化省市县
     * @param name
     * @return
     */
    private String formatArea(String name) {
        logger.info("区域原来名称:{}", name);
        if(StringUtils.isBlank(name)) {
            return "";
        }
        name = StringUtils.trimToEmpty(name);
        int i = 0;
        if(((i = name.indexOf("省")) > 0)
                || ((i = name.indexOf("自治区")) > 0)
                || ((i = name.indexOf("市")) > 0)
                || ((i = name.indexOf("区")) > 0)
                || ((i = name.indexOf("县")) > 0)
                ) {
            logger.info("区域处理后名称:{}", name.substring(0, i));
            return name.substring(0, i);
        } else {
            logger.info("区域处理后名称:{}", name);
            return name;
        }
    }
    
    /**
     * 签名
     * @return
     */
    private String sign(String xmlStr, String pwd) {
        String macStr = xmlStr + pwd;
        macStr = DigestUtils.sha256Hex(macStr);
        logger.info("签名之后的数据：{}", macStr);
        return macStr;
    }
    
    private boolean checkSign(String xmlStr, String mac, String pwd) {
        logger.info("返回的mac：{}", mac);
        String signMac = sign(xmlStr, pwd);
        if(signMac.equals(mac)) {
            logger.info("签名验证成功");
            return true;
        } else {
            logger.info("签名验证失败");
            return false;
        }
    }
    
    /**
     * 解析返回参数
     * @param urlParams
     * @return
     */
    private Map<String, String> getParams(String urlParams) {
        Map<String, String> map = new HashMap<String, String>();
        if(StringUtils.isNotBlank(urlParams)) {
            String[] strs = urlParams.split("&");
            for(String str : strs) {
                String temp = StringUtils.trimToEmpty(str);
                int i = temp.indexOf("=");
                if (i >= 0) {
                    String tempKey = temp.substring(0, i);
                    String tempValue = temp.substring(i+1);
                    map.put(tempKey, tempValue);
                }
            }
        }
        return map;
    }

    @Override
    public PartnerResult<WithdrawCallbackData> withdrawSingle(String orderId, PartnerWithdrawData data) {
        logger.info("进入宝易互通单笔打款");

        orderId = config.getMchtNo() + orderId;
        
        logger.info("加上宝易互通前缀的订单号：" + orderId);
        
        PartnerResult<WithdrawCallbackData> result = new PartnerResult<WithdrawCallbackData>();
        WithdrawCallbackData backData = new WithdrawCallbackData();
        result.setObject(backData);
        backData.setOrderId(orderId.replaceAll(config.getMchtNo(), ""));
        backData.setAmount(data.getAmount());
        backData.setRequestUrl(config.getRequestUril());
        backData.setFee(feeCalculate(backData.getAmount()));
        backData.setCallbackTime(new Date());
        //请求的订单号作为渠道的订单号
        backData.setChannelOrderId(orderId);
        
        logger.info("封装请求报文...");
        
        String xmlStr;
        String signMac;
        try {
            //1.封装请求信息
            Header header = new Header();
            SingleWithdrawRequestBody body = new SingleWithdrawRequestBody();
            RequestMessage requestMessage = new RequestMessage(header, body);
            //封装报文头信息
            header.setVersion(VERSION);
            header.setMsgtype(MSG_TYPE_REQUEST); //请求报文
            header.setChannelno(CHANNEL_NO); //民生体系外商户填 99
            header.setMerchantno(config.getMchtNo());
            String dateStr = DateUtil.timeToStr(new Date(), DateUtil.DATE_FORMAT_PATTEN4);
            header.setTrandate(dateStr.substring(0, 8));
            header.setTrantime(dateStr.substring(8));
            header.setBussflowno(orderId);
            header.setTrancode(TradeCode.SINGLE_WITHDRAW);
            
            //获取提现的银行卡号信息
            String cardNo = data.getBankAccount();
            String bankName = this.getChannelBankId(data.getBankId());
            //String bankBranchName = data.getBankBranchName();
            String userName = data.getUserName();
            
            //封装报文体信息
            //body.setMerPlatAcctAlias(UmbpayConstant.getMchtNo());
            //body.setProtocolNo(""); //协议号，商户开通协议认证后必输
            //bankName = "上海银行";
            body.setBankName(bankName);//银行名称
            body.setAccountNo(cardNo);
            body.setAccountName(userName);
            if("1".equals(data.getAccountType())) {
                body.setAccountType("01"); //对公
            } else {
                body.setAccountType("00"); //对私
            }
            
            //城市ID转换成城市名称
            SProvince sProvince = CityData.PROVINCE_MAP.get(data.getBankProvinceId());
            SCity sCity = CityData.CITY_MAP.get(data.getBankCityId());
            if(null == sProvince || null == sCity) {
                throw new ServiceException("找不到对应的省份或城市信息");
            }
            
            body.setOpenProvince(formatArea(sProvince.getProvinceName())); //省，不要“省”
            body.setOpenCity(formatArea(sCity.getCityName()));//市
            //body.setOpenName(bankBranchName); //代付不需要输入
            body.setTranAmt(data.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            body.setCurType(CURTYPE);
            body.setBsnType("09100"); //业务类型必输 91汇款转账类
            body.setCertType(""); //非必输 代付不用
            body.setCertNo(""); //非必输 代付不用
            xmlStr = "";
            try {
                xmlStr = XMLUtils.generateDocument(requestMessage, CHAR_SET).asXML();
            } catch (Exception e) {
                throw new ServiceException("生成请求报文失败", e);
            } 
            //2.数据签名
            signMac = sign(xmlStr, config.getPwd());
        } catch (Exception e1) {
            logger.error("发送请求前异常", e1);
            backData.setWithdrawStatus(WithdrawStatus.FAILURE);
            result.setCode(PartnerResultCode.FAILURE);
            result.setMessage(e1.getMessage());
            logger.info("异常，退出宝易互通单笔打款...");
            logger.info("result:{}", result);
            return result;
        }
        
        result.setCode(PartnerResultCode.SUCCESS);
        result.setMessage("请求成功");
        
        try {
            //3.发送请求
            Map<String, String> params = new HashMap<>();
            params.put("xml", xmlStr);
            params.put("mac", signMac);
            logger.info("发送请求...");
            /*logger.info("请求报文: {}", 
                        params.toString()
                    );*/
            logger.info("请求报文: {}", 
                        params.toString().replaceAll("<accountNo>.+</accountNo>", "<accountNo>**** ****</accountNo>")
                        .replaceAll("<accountName>.+</accountName>", "<accountName>***</accountName>")
                        .replaceAll("<certNo>.+</certNo>", "<certNo>******</certNo>")
                        .replaceAll("<mobileNo>.+</mobileNo>", "<mobileNo>********</mobileNo>")
                    );
            String backStr = HttpUtils.doPost(config.getRequestUril(), params);
            logger.info("返回报文：{}", backStr);
            
            //4.解析返回结果
            Map<String, String> backParams = getParams(backStr);
            String backXmlStr = backParams.get("xml");
            String backMac = backParams.get("mac");
            
            if(StringUtils.isBlank(backXmlStr)) {
                throw new  ServiceException("返回的xml字符串为空");
            }
            
            SingleWithdrawRspMessage rspMessage = null;
            try {
                Document doc = DocumentHelper.parseText(backXmlStr);
                rspMessage = (SingleWithdrawRspMessage) XMLUtils.parseDocument(SingleWithdrawRspMessage.class, doc);
            } catch (Exception e) {
                throw new ServiceException("解析返回报文失败", e);
            } 
            backData.setResultStr(JSON.toJSONString(rspMessage));
            
            logger.info("订单号：{}， 返回结果：{}", orderId, rspMessage);
            
            //5.封装返回结果
            if(RET_CODE_SUCCESS.equals(rspMessage.getHeader().getRespcode())) { //交易请求成功
                //检查签名
                if(StringUtils.isBlank(backMac)) {
                    throw new  ServiceException("返回mac为空，签名验证失败");
                }
                boolean checkSign = checkSign(backXmlStr, backMac, config.getPwd());
                if(!checkSign) {
                    throw new  ServiceException("签名验证失败");
                }
                if(TRANSTATE_ACCEPT.equals(rspMessage.getBody().getTranState()) || TRANSTATE_HANDLING.equals(rspMessage.getBody().getTranState())) {
                    backData.setWithdrawStatus(WithdrawStatus.APPLYING);
                    backData.setMessage("已受理");
                } else if(TRANSTATE_SUCCESS.equals(rspMessage.getBody().getTranState())) {
                    backData.setWithdrawStatus(WithdrawStatus.SUCCESS);
                    backData.setMessage("代付成功");
                } else if(TRANSTATE_FAILURE.equals(rspMessage.getBody().getTranState())) {
                    backData.setWithdrawStatus(WithdrawStatus.FAILURE);
                    backData.setMessage("代付失败");
                } else {
                    logger.info("未知的交易状态, respCode:{}, respMsg:{}, tranState:{}", rspMessage.getHeader().getRespcode(), rspMessage.getHeader().getRespmsg(), rspMessage.getBody().getTranState());
                    throw new ServiceException("未知交易状态");
                }
            } else { 
                //民生技术确认，报文头返回码，除了 C000000000交易成功， W000000000 处理中， 其他为失败
                if(RET_CODE_HANDLING.equalsIgnoreCase(rspMessage.getHeader().getRespcode())) { //处理中，不确定状态
                    backData.setWithdrawStatus(WithdrawStatus.APPLYING);
                } else {  //失败
                    backData.setWithdrawStatus(WithdrawStatus.FAILURE);
                }
                backData.setMessage(rspMessage.getHeader().getRespmsg());
            }
            
            logger.info("交易完成, 退出宝易互通单笔打款...");
            logger.info("result:{}", result);
            return result;
        } catch (Exception e) {
            logger.error("发送请求后发生异常", e);
            backData.setWithdrawStatus(WithdrawStatus.APPLYING);  //异常也定义为申请中
            backData.setMessage(e.getMessage());
            logger.info("异常，退出宝易互通单笔打款...");
            logger.info("result:{}", result);
            return result;
        }
    }

    @Override
    public WithdrawChannel getChannelId() {
        return WithdrawChannel.UMBPAY_WITHDRAW;
    }
    
    @Override
    public BigDecimal feeCalculate(BigDecimal amount) {
        return new BigDecimal(config.getFixFee());
    }

    @Override
    public PartnerResult<WithdrawCallbackData> withdrawCallback(String backStr) {
        PartnerResult<WithdrawCallbackData> result = new PartnerResult<WithdrawCallbackData>();
        
        logger.info("宝易互通单款异步回调开始...");
        
        //放到controller做
        //backStr = new String(backStr.getBytes("gbk"), CHAR_SET);
        //backStr = URLDecoder.decode(backStr.toString(), CHAR_SET);
    
        /*logger.info("返回字符串， {}", backStr
                );*/
        logger.info("返回字符串， {}", backStr.replaceAll("<accountNo>.+</accountNo>", "<accountNo>**** ****</accountNo>")
                    .replaceAll("<accountName>.+</accountName>", "<accountName>***</accountName>")
                );

        //1.解析返回参数
        Map<String, String> backParams = getParams(backStr);
        String backXmlStr = backParams.get("xml");
        String backMac = backParams.get("mac");
        if(StringUtils.isBlank(backXmlStr)) {
            throw new  ServiceException("异步回调的xml字符串为空");
        }
        if(StringUtils.isBlank(backMac)) {
            throw new  ServiceException("返回mac为空，签名验证失败");
        }
        
        SingleWithdrawCallbackRspMessage rspMessage = null;
        try {
            Document doc = DocumentHelper.parseText(backXmlStr);
            rspMessage = (SingleWithdrawCallbackRspMessage) XMLUtils.parseDocument(SingleWithdrawCallbackRspMessage.class, doc);
            //logger.info("解析返回报文成功, 返回报文:{}", rspMessage);
        } catch (Exception e) {
            throw new ServiceException("解析异步回调报文失败", e);
        } 
        
        //2.检查签名
        boolean checkSign = checkSign(backXmlStr, backMac, config.getPwd());
        if(!checkSign) {
            throw new  CheckSignatureException("签名验证失败");
        }
        
        logger.info("签名验证成功...");
        
        if(!(TradeCode.SINGLE_WITHDRAW_CALLBACK.equals(rspMessage.getHeader().getTrancode()) || TradeCode.SINGLE_WITHDRAW.equals(rspMessage.getHeader().getTrancode()))) {
            throw new  ServiceException("非单笔打款异步回调");
        }
        
        //3.封装返回报文
        Header header = new Header();
        RequestMessage requestMessage = new RequestMessage(header, null);
        //封装报文头信息
        header.setVersion(VERSION);
        header.setMsgtype(MSG_TYPE_RESPONSE); //响应报文
        header.setChannelno(CHANNEL_NO); //民生体系外商户填 99
        header.setMerchantno(config.getMchtNo());
        String dateStr = DateUtil.timeToStr(new Date(), DateUtil.DATE_FORMAT_PATTEN4);
        header.setTrandate(dateStr.substring(0, 8));
        header.setTrantime(dateStr.substring(8));
        header.setBussflowno(rspMessage.getHeader().getBussflowno());
        header.setTrancode(TradeCode.SINGLE_WITHDRAW_CALLBACK);
        header.setRespcode(RET_CODE_SUCCESS); //接收成功
        
        String rspStr = "";
        try {
            String xmlStr = XMLUtils.generateDocument(requestMessage, CHAR_SET).asXML();
            //计算mac
            String signMac = sign(xmlStr, config.getPwd());
            rspStr = "xml=" + xmlStr + "&mac=" + signMac;
        } catch (IllegalArgumentException | IllegalAccessException e) {
            logger.info("生成相应的xml报文发生异常， 不响应回调", e);
        }
        
        //4.封装参数返回
        WithdrawCallbackData backData = new WithdrawCallbackData();
        //计算手续费(固定每笔2元)
        backData.setFee(feeCalculate(BigDecimal.ZERO));
        backData.setCallbackTime(new Date());
        backData.setRequestUrl(config.getRequestUril());
        
        //回调的参数存入请求数据库，敏感信息进行加密
        if(rspMessage.getBody() != null) {
            //卡号
            String bankAccount = desEncoder.encode(rspMessage.getBody().getAccountNo());
            rspMessage.getBody().setAccountNo(bankAccount);
            //用户名
            String userName = desEncoder.encode(rspMessage.getBody().getAccountName());
            rspMessage.getBody().setAccountName(userName);
        }
        backData.setCallbackStr(JSON.toJSONString(rspMessage));
        
        //未接受的订单，异步回调指挥返回报文头
        if(StringUtils.isNotBlank(rspMessage.getHeader().getRespcode()) && (!RET_CODE_SUCCESS.equals(rspMessage.getHeader().getRespcode()))) {
            logger.info("header respcode:{}, respmsg:{}", rspMessage.getHeader().getRespcode(), rspMessage.getHeader().getRespmsg());
            logger.info("宝易互通返回订单号:{}", rspMessage.getHeader().getBussflowno());
            String orderId = StringUtils.trimToEmpty(rspMessage.getHeader().getBussflowno()).replaceAll(config.getMchtNo(), "");
            logger.info("去除宝易互通商户号的订单号:{}", orderId);
            backData.setOrderId(orderId);
            backData.setMessage(rspMessage.getHeader().getRespmsg());
            backData.setChannelOrderId(rspMessage.getHeader().getBussflowno());
            
            //民生技术确认，报文头返回码，除了 C000000000交易成功， W000000000 处理中， 其他为失败
            if(RET_CODE_HANDLING.equalsIgnoreCase(rspMessage.getHeader().getRespcode())) { //处理中，不确定状态
                //不是最终结果，不正常回调，认为回调失败
                throw new ServiceException("通知状态处理中， Respcode：" + rspMessage.getHeader().getRespcode());
            } else {  //失败
                backData.setWithdrawStatus(WithdrawStatus.FAILURE);
                //成功或失败时才响应停止主动通知
                backData.setResponseStr(rspStr);
                logger.info("返回xml应答:{}", rspStr);
            }
            backData.setMessage(rspMessage.getHeader().getRespmsg());
            
            //先抛异常人工处理，稍后做错误码对应表
            throw new ServiceException("交易返回码Respcode：" + rspMessage.getHeader().getRespcode() + "交易状态不确定");
        } else {
            logger.info("body tranRespCode:{}, tranState:{}, tranRespMsg:{}", rspMessage.getBody().getTranRespCode(), rspMessage.getBody().getTranState(), rspMessage.getBody().getTranRespMsg());
            logger.info("宝易互通返回订单号:{}", rspMessage.getBody().getOrgTranFlow());
            String orderId = StringUtils.trimToEmpty(rspMessage.getBody().getOrgTranFlow()).replaceAll(config.getMchtNo(), "");
            logger.info("去除宝易互通商户号的订单号:{}", orderId);
            backData.setOrderId(orderId);
            backData.setMessage(rspMessage.getBody().getTranRespMsg());
            backData.setChannelOrderId(rspMessage.getBody().getOrgTranFlow());
            //民生技术确认以TranState为准，不用判断TranRespCode
            //if(RET_CODE_SUCCESS.equals(rspMessage.getBody().getTranRespCode())) {
                if(TRANSTATE_SUCCESS.equals(rspMessage.getBody().getTranState())) {
                    //成功或失败时才响应停止主动通知
                    backData.setResponseStr(rspStr);
                    logger.info("返回xml应答:{}", rspStr);
                    backData.setAmount(new BigDecimal(rspMessage.getBody().getTranAmt()));
                    backData.setWithdrawStatus(WithdrawStatus.SUCCESS); //成功
                } else if(TRANSTATE_FAILURE.equals(rspMessage.getBody().getTranState())) {
                    //成功或失败时才响应停止主动通知
                    backData.setResponseStr(rspStr);
                    logger.info("返回xml应答:{}", rspStr);
                    backData.setAmount(new BigDecimal(rspMessage.getBody().getTranAmt()));
                    backData.setWithdrawStatus(WithdrawStatus.FAILURE); //失败
                } else {
                    //不是最终结果，不正常回调，认为回调失败
                    throw new ServiceException("通知的状态未知， TranState：" + rspMessage.getBody().getTranState());
                }
            //} else {
            //    //通知失败，不应该出现这种情况
            //    throw new ServiceException("交易响应码为失败， TranRespCode：" + rspMessage.getBody().getTranRespCode());
            //}
        }
        
        result.setCode(PartnerResultCode.SUCCESS);
        result.setMessage("回调处理成功");
        result.setObject(backData);
        logger.info("回调处理完成...");
        logger.info("result:{}", result);
        return result;
    }

    @Override
    public PartnerResult<WithdrawCallbackData> withdrawSingleQuery(
            String orderId) {
        logger.info("宝易互通单笔打款状态查询开始...");

        logger.info("请求参数：orderId = {}", orderId);
        
        orderId = config.getMchtNo() + orderId;
        
        logger.info("加上宝易互通前缀的订单号：" + orderId);
        
        PartnerResult<WithdrawCallbackData> result = new PartnerResult<>();
    
        //1.封装请求信息
        logger.info("封装请求的参数...");
        Header header = new Header();
        SingleWithdrawQueryRequestBody body = new SingleWithdrawQueryRequestBody();
        RequestMessage requestMessage = new RequestMessage(header, body);
        //封装报文头信息
        header.setVersion(VERSION);
        header.setMsgtype(MSG_TYPE_REQUEST); //请求报文
        header.setChannelno(CHANNEL_NO); //民生体系外商户填 99
        header.setMerchantno(config.getMchtNo());
        String dateStr = DateUtil.timeToStr(new Date(), DateUtil.DATE_FORMAT_PATTEN4);
        header.setTrandate(dateStr.substring(0, 8));
        header.setTrantime(dateStr.substring(8));
        header.setBussflowno(config.getMchtNo() + OrderIdHelper.newOrderId15());  //是查询的新的流水号，并不是代付时的原流水号
        header.setTrancode(TradeCode.SINGLE_WITHDRAW_QUERY);
        
        //封装报文体信息
        body.setOrgTranFlow(orderId);
        String xmlStr = "";
        try {
            xmlStr = XMLUtils.generateDocument(requestMessage, CHAR_SET).asXML();
        } catch (Exception e) {
            throw new ServiceException("生成请求报文失败", e);
        } 
        
        //2.数据签名
        String signMac = sign(xmlStr, config.getPwd());
        
        //3.发送请求
        logger.info("发送请求...");
        Map<String, String> params = new HashMap<>();
        params.put("xml", xmlStr);
        params.put("mac", signMac);
        logger.info("请求报文: {}", params);
        String backStr = HttpUtils.doPost(config.getRequestUril(), params);
        logger.info("返回报文：{}", backStr);
        
        //4.解析返回结果
        Map<String, String> backParams = getParams(backStr);
        String backXmlStr = backParams.get("xml");
        String backMac = backParams.get("mac");
        if(StringUtils.isBlank(backXmlStr)) {
            throw new  ServiceException("返回的xml字符串为空");
        }
        
        logger.info("解析返回报文...");
        SingleWithdrawQueryRspMessage rspMessage = null;
        try {
            Document doc = DocumentHelper.parseText(backXmlStr);
            rspMessage = (SingleWithdrawQueryRspMessage) XMLUtils.parseDocument(SingleWithdrawQueryRspMessage.class, doc);
        } catch (Exception e) {
            throw new ServiceException("解析返回报文失败", e);
        } 
        
        logger.info("respCode:{}, respMsg:{}, tranState:{}", rspMessage.getHeader().getRespcode(), rspMessage.getHeader().getRespmsg(), rspMessage.getBody().getTranState());
        
        //5.封装返回结果
        if(RET_CODE_SUCCESS.equals(rspMessage.getHeader().getRespcode())) { //查询成功
            
            /*WithdrawSingleQueryData queryData = new WithdrawSingleQueryData();
            
            result.setObject(queryData);
            result.setCode(PartnerResultCode.SUCCESS);
            result.setMessage("查询成功");

            queryData.setOrderId(orderId);
            queryData.setMessage(rspMessage.getBody().getTranRespMsg());
            
            if(RECORD_NOT_EXIST_CODE.equals(rspMessage.getBody().getTranRespCode())) { //订单不存在
                logger.info("订单号不存在：{}", rspMessage.getBody().getTranRespCode());
                queryData.setWithdrawStatus(WithdrawStatus.FAILURE);
                return result;
            } else {
                //检查签名
                if(StringUtils.isBlank(backMac)) {
                    throw new  CheckSignatureException("返回mac为空，签名验证失败");
                }
                boolean checkSign = checkSign(backXmlStr, backMac, config.getPwd());
                if(!checkSign) {
                    throw new  CheckSignatureException("签名验证失败");
                }
                
                if(TRANSTATE_ACCEPT.equals(rspMessage.getBody().getTranState()) || TRANSTATE_HANDLING.equals(rspMessage.getBody().getTranState())) {
                    queryData.setWithdrawStatus(WithdrawStatus.APPLYING);
                } else if(TRANSTATE_SUCCESS.equals(rspMessage.getBody().getTranState())) {
                    queryData.setWithdrawStatus(WithdrawStatus.SUCCESS);
                } else if(TRANSTATE_FAILURE.equals(rspMessage.getBody().getTranState())) {
                    queryData.setWithdrawStatus(WithdrawStatus.FAILURE);
                } else { //未知
                    logger.error("未知的交易状态，tranState:{}", rspMessage.getBody().getTranState());
                    throw new  ServiceException("未知的处理状态:", rspMessage.getBody().getTranState());
                }
                return result;
            }*/
            
            WithdrawCallbackData backData = new WithdrawCallbackData();
            //计算手续费(固定每笔2元)
            backData.setFee(feeCalculate(BigDecimal.ZERO));
            backData.setCallbackTime(new Date());
            backData.setRequestUrl(config.getRequestUril());
            
            backData.setOrderId(orderId.replaceAll(config.getMchtNo(), ""));
            backData.setMessage(rspMessage.getHeader().getRespmsg());
            backData.setChannelOrderId(orderId);
            backData.setCallbackStr(JSON.toJSONString(rspMessage));
            //backData.setAmount(null);
            
            
            if(RECORD_NOT_EXIST_CODE.equals(rspMessage.getBody().getTranRespCode())) { //订单不存在
                logger.info("订单号不存在：{}", rspMessage.getBody().getTranRespCode());
                backData.setWithdrawStatus(WithdrawStatus.FAILURE);
            } else {
                //检查签名
                if(StringUtils.isBlank(backMac)) {
                    throw new  CheckSignatureException("返回mac为空，签名验证失败");
                }
                boolean checkSign = checkSign(backXmlStr, backMac, config.getPwd());
                if(!checkSign) {
                    throw new  CheckSignatureException("签名验证失败");
                }
                
                if(TRANSTATE_ACCEPT.equals(rspMessage.getBody().getTranState()) || TRANSTATE_HANDLING.equals(rspMessage.getBody().getTranState())) {
                    backData.setWithdrawStatus(WithdrawStatus.APPLYING);
                } else if(TRANSTATE_SUCCESS.equals(rspMessage.getBody().getTranState())) {
                    backData.setWithdrawStatus(WithdrawStatus.SUCCESS);
                } else if(TRANSTATE_FAILURE.equals(rspMessage.getBody().getTranState())) {
                    backData.setWithdrawStatus(WithdrawStatus.FAILURE);
                } else { //未知
                    logger.error("未知的交易状态，tranState:{}", rspMessage.getBody().getTranState());
                    throw new  ServiceException("未知的处理状态:", rspMessage.getBody().getTranState());
                }
            }
            
            result.setCode(PartnerResultCode.SUCCESS);
            result.setMessage("查询成功");
            result.setObject(backData);
            
            return result; 
            
        } else {
            result.setCode(PartnerResultCode.FAILURE);
            result.setMessage(rspMessage.getHeader().getRespmsg());
            return result;
        }
    }
}
