package com.elend.gate.channel.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
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
import com.elend.gate.channel.util.UmbpayUtil;
import com.elend.gate.conf.facade.SystemConfig;
import com.elend.gate.conf.facade.UmbpayConfig;
import com.elend.gate.util.GateHttpUtil;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.ServiceException;
import com.elend.p2p.gson.JSONUtils;
import com.elend.p2p.util.DateUtil;

/**
 * 宝易互通支付渠道
 * @author mgt
 */
@Service
public class UmbpayChannel extends PayChannelService {
    private final static Logger logger = LoggerFactory.getLogger(UmbpayChannel.class);
    //易宝成功的返回码
    private final static String UMBPAY_SUCCESS_CODE1 = "1";
    private final static String UMBPAY_UNPAY = "0";
    private final static String UMBPAY_FAILURE = "3";

    @Autowired
    private UmbpayConfig config;
    
    @Autowired
    private SystemConfig systemConfig;
    
    @Override
    public RequestFormData charge(String orderId, PartnerChargeData chargeData){
        logger.info("宝易互通充值请求, orderId:{}, amount:{}", orderId, chargeData.getAmount());
        
        String merchantid = config.getMerId();	//商户号
        String merorderid = orderId;			//订单编号
        String amountsum = String.format("%.2f", chargeData.getAmount().setScale(2, BigDecimal.ROUND_DOWN)); //订单金额
        String subject = "empty";	//商品品种, 没有返回empty
        String currencytype = "01"; 	//币种 01 人民币
        String autojump = "1";	//自动跳转取货页面
        String waittime = "0";	//跳转的等待时间  单位秒（商户选择银行为0）
        String merurl = config.getRedirectUrl();	//同步返回页面
        String informmer = "1";	//是否通知商户 1 通知（点对点）
        String informurl = config.getCallbackUrl();	//回调地址
        String confirm = "1";	//商户返回确认 1 返回(点对点通知收到确认)
        
        String merbank = "";	//支付银行
        String bankInput = "";	//是否在商户端选择银行
        //不指定银行
        if(chargeData.getBankId() == null || chargeData.getBankId() == BankIdConstant.NO_DESIGNATED) {
        //if(1 == 1) {
        	merbank = "empty"; 	//支付银行
        	bankInput = "0";	//在支付平台选择银行
        } else {
        	//银行ID转换成渠道对应的银行
        	String channelBandId=this.getChannelBankId(chargeData.getBankId());
        	merbank = channelBandId; 	//支付银行
        	//测试环境只能使用BOS 上海银行
        	if(systemConfig.isUmbpayChargeTest()) {
        		merbank = "BOS"; 	//支付银行
        	}
        	bankInput = "1";		//是否在商户端选择银行       1 是
        }
        String tradetype = "0";	//交易类型， 0 即时到帐，1 担保交易, bankInput为1时，只能为0
        String umb_interface = "5.00";	//接口版本
        String bankcardtype = "01";	//支付银行卡类型  00 借贷混合 01 纯借记卡 , 充值投资理财类只能选择纯借记卡
        String pdtdetailurl = "";	//商品详情地址
        //拼接加密的源字符串
        String mac_src="merchantid="+merchantid+"&merorderid="+merorderid
        			+"&amountsum="+amountsum+"&subject="+subject
        			+"&currencytype="+currencytype+"&autojump="+autojump
        			+ "&waittime=" + waittime +"&merurl="+merurl
        			+ "&informmer=" + informmer +"&informurl=" +informurl
        			+ "&confirm=" + confirm + "&merbank=" + merbank
        			+ "&tradetype=" + tradetype + "&bankInput=" + bankInput
        			+ "&interface=" + umb_interface + "&bankcardtype=" + bankcardtype
        			+ "&pdtdetailurl=" + pdtdetailurl + "&merkey=" + config.getPriKey();
        logger.info("算mac原串：{}", mac_src);
        String mac = UmbpayUtil.GetMessageDigest(mac_src);
        logger.info("mac值：{}", mac);
        String remark = "";
        String pdtdnm = "充值";	//商品名称

        RequestFormData form = new RequestFormData();
        form.setRequestUrl(config.getUmbpayCommonReqURL());
        form.addParam("merchantid", merchantid);
        form.addParam("merorderid", merorderid);
        form.addParam("amountsum", amountsum);
        form.addParam("subject", subject);
        form.addParam("currencytype", currencytype);
        form.addParam("autojump", autojump);
        form.addParam("waittime", waittime);
        form.addParam("merurl", merurl);
        form.addParam("informmer", informmer);
        form.addParam("informurl", informurl);
        form.addParam("confirm", confirm);
        form.addParam("merbank", merbank);
        form.addParam("tradetype", tradetype);
        form.addParam("bankInput", bankInput);
        form.addParam("interface", umb_interface);
        form.addParam("bankcardtype", bankcardtype);
        form.addParam("pdtdetailurl", pdtdetailurl);
        form.addParam("mac", mac);
        form.addParam("remark", remark);
        form.addParam("pdtdnm", pdtdnm);
        form.addParam("banktype", "00");
        
        logger.info("宝易互通充值请求表单生成, form:{}", JSONUtils.toJson(form, false));
        
        return form;
    }

    @Override
    public ChannelIdConstant getChannelId() {
        return ChannelIdConstant.UMBPAY;
    }

    @Override
    public PartnerResult<ChargeCallbackData> chargeCallback(
            ChannelIdConstant channelId, Map<String, String> params)
            throws CheckSignatureException, ServiceException {
        logger.info("宝易互通充值回调, channelId:{}, params:{}", channelId,
                    JSONUtils.toJson(params, false));
        
		String merchantid = params.get("merchantid"); 		// 商户编号
		String merorderid = params.get("merorderid"); 		// 订单号
		String amountsum = params.get("amountsum"); 		// 金额
		String currencytype = params.get("currencytype"); 	// 币种
		String subject = params.get("subject"); 			// 商品种类
		String remark = params.get("remark"); 				// 备注
		String state = params.get("state"); 				// 支付状态 1--支付成功
		String paybank = params.get("paybank"); 			// 支付银行
		String banksendtime = params.get("banksendtime"); 	// 发送到银行时间
		String merrecvtime = params.get("merrecvtime"); 	// 返回到商户时间
		String strInterface = params.get("interface"); 		// 接口版本
		String mac_rec = params.get("mac"); 				// 加密数据串
		String merkey = config.getPriKey(); 				// 商户支付密钥
		String payorderid = params.get("payorderid")==null?"":params.get("payorderid"); 		// 渠道订单号
		
		logger.info("校验签名");
		
		// 校验返回信息
		String mac_src = "merchantid="+merchantid+"&merorderid="+merorderid
		  			+"&amountsum="+amountsum+"&currencytype="+currencytype
		  			+"&subject="+subject+"&state="+state+"&paybank="+paybank
		  			+"&banksendtime="+banksendtime+"&merrecvtime="+merrecvtime+
		  			"&interface="+strInterface+"&merkey="+merkey;
		
		logger.info("签名原串：{}", mac_src);
		String mac = UmbpayUtil.GetMessageDigest(mac_src);
        logger.info("mac：{}", mac);
		
        if(mac.equals(mac_rec)) {
        	logger.info("签名校验成功");
        	ChargeCallbackData callbackData = new ChargeCallbackData();
            callbackData.setAmount(new BigDecimal(amountsum));
            callbackData.setCallbackStr("SUCCESS");
            callbackData.setChannelOrderId(payorderid);
            callbackData.setOrderId(merorderid);
            logger.info("充值状态state:{}", state);
            if (UMBPAY_SUCCESS_CODE1.equals(state)/* || state.equals(UMBPAY_SUCCESS_CODE2)*/) {
                callbackData.setChannelBankId(paybank);
                String bankId=getBankId(callbackData.getChannelBankId());
                callbackData.setBankId(bankId);
                callbackData.setPayTime(DateUtil.strToTime(banksendtime,DateUtil.DATE_FORMAT_PATTEN));
                callbackData.setNoticeTime(DateUtil.strToTime(merrecvtime,DateUtil.DATE_FORMAT_PATTEN4));
                logger.info("宝易互通充值回调, 交易成功, callbackData:{}", callbackData);
                return new PartnerResult<ChargeCallbackData>(PartnerResultCode.SUCCESS,
                                                      callbackData);
            } else if(UMBPAY_FAILURE.equals(state)) {
            	logger.error("宝易互通充值回调, 交易失败, state:{}, callbackData:{}", state, callbackData);
            	return new PartnerResult<ChargeCallbackData>(PartnerResultCode.FAILURE, callbackData, "失败状态:" + state);
            } else {	//处理中或未识别的结果，不是最终状态，丢弃不做处理
                logger.error("宝易互通充值回调, 非最终状态, state:{}, callbackData:{}", state, callbackData);
                throw new ServiceException("非最终结果，丢弃，state:" + state);
            }
        } else {
            logger.error("交易签名被篡改.channelId:{}, params:{}", channelId, JSONUtils.toJson(params, false));
            throw new CheckSignatureException("交易签名被篡改");
        }
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

	@Override
	public PartnerResult<PartnerSingleQueryChargeData> singleQueryCharge(ChannelIdConstant channelId, String orderId) throws CheckSignatureException, ServiceException {
		logger.info("宝易互通singleQueryCharge, channelId:{}, orderId:{}", channelId, orderId);
		
		if(StringUtils.isBlank(orderId)) {
			logger.error("无效的订单号:" + orderId);
			throw new ServiceException("无效的订单号:" + orderId);
		}
		
		String merchantId = config.getMerId(); 				// 商家ID
		String merOrderId = orderId;						//订单号
		String version = "1.00";							//版本
		String macSrc = "merchantId="+merchantId+"&merOrderId="+merOrderId+"&version="+version+"&key="+config.getPriKey();
		logger.info("签名原串：{}", macSrc);
		String mac = UmbpayUtil.GetMessageDigest(macSrc);
		logger.info("mac:{}", mac);
		
		Map<String, String> reParams = new HashMap<>();
		reParams.put("merchantId", merchantId);
		reParams.put("merOrderId", merOrderId);
		reParams.put("version", version);
		reParams.put("mac", mac);
		String responseStr = null; 
		
		try {
			logger.info("发送请求...");
			logger.info("请求数据:{}", reParams.toString());
			responseStr = GateHttpUtil.doPost(config.getQueryChargeReqURL(), reParams);
			logger.info("返回数据:{}", responseStr);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		if (StringUtils.isBlank(responseStr)) {
			throw new ServiceException("No response.");
		}
		
		String recordLength = "";
    	String status = "";
    	String macb = "";
    	String merchantIdb = "";
    	String merOrderIdb = "";
    	String versionb = "";
    	String payOrderId = "";
    	String merOrderIdb2 = "";
    	String merSendTime = "";
    	String amountSum = "";
    	String payBank = "";
    	String state = "";
    	String type = "";
    	PartnerSingleQueryChargeData backData = new PartnerSingleQueryChargeData();
    	backData.setOrderId(orderId);
        try {
        	Document doc = DocumentHelper.parseText(responseStr);
        	//获取头信息
        	Element recordLengthElement = (Element) doc.selectSingleNode("accountCheckResult/recordLength");
        	recordLength = recordLengthElement.getTextTrim();
        	Element statusElement = (Element) doc.selectSingleNode("accountCheckResult/status");
        	status = statusElement.getTextTrim();
        	Element macElement = (Element) doc.selectSingleNode("accountCheckResult/mac");
        	macb = macElement.getTextTrim();
        	
        	//获取请求信息
        	Element merchantIdElement = (Element) doc.selectSingleNode("accountCheckResult/requestInfo/merchantId");
        	merchantIdb = merchantIdElement.getTextTrim();
        	Element merOrderIdElement = (Element) doc.selectSingleNode("accountCheckResult/requestInfo/merOrderId");
        	merOrderIdb = merOrderIdElement.getTextTrim();
        	Element versionElement = (Element) doc.selectSingleNode("accountCheckResult/requestInfo/version");
        	versionb = versionElement.getTextTrim();
        	
        	logger.info("校验签名");
            String macSrcb = "merchantId=" + merchantIdb + "&"
            		+ "version=" + versionb + "&"
            		+ "merOrderId=" + merOrderIdb + "&"
            		+ "key=" + config.getPriKey();
            
            logger.info("签名原串：{}", macSrcb);
            String mac2 = UmbpayUtil.GetMessageDigest(macSrcb);
            logger.info("算出的mac：{}", mac2);
            if(!mac2.equals(macb)) {
            	throw new CheckSignatureException("签名校验失败");
            }
            logger.info("签名校验成功");
        	
        	if(!"success".equalsIgnoreCase(status)) {
        		logger.error("查询失败 status:{}", status);
        		return new PartnerResult<PartnerSingleQueryChargeData>(PartnerResultCode.FAILURE, backData, "错误状态：" + status);
        	}
        	
        	//记录数小于一  订单不存在
        	if(StringUtils.isBlank(recordLength) || Integer.parseInt(recordLength) < 1) {
        		logger.error("订单不存在:{}", orderId);
        		backData.setStatus(ChargeQueryStatus.NOT_EXIST);
        		return new PartnerResult<PartnerSingleQueryChargeData>(PartnerResultCode.SUCCESS, backData);
        	}

        	//获取查询到的订单信息
        	Element payOrderIdElement = (Element) doc.selectSingleNode("accountCheckResult/payOrder/payOrderId");
        	payOrderId = payOrderIdElement.getTextTrim();
        	Element merOrderIdElement2 = (Element) doc.selectSingleNode("accountCheckResult/payOrder/merOrderId");
        	merOrderIdb2 = merOrderIdElement2.getTextTrim();
        	Element merSendTimeElement = (Element) doc.selectSingleNode("accountCheckResult/payOrder/merSendTime");
        	merSendTime = merSendTimeElement.getTextTrim();
        	Element amountSumElement = (Element) doc.selectSingleNode("accountCheckResult/payOrder/amountSum");
        	amountSum = amountSumElement.getTextTrim();
        	Element payBankElement = (Element) doc.selectSingleNode("accountCheckResult/payOrder/payBank");
        	payBank = payBankElement.getTextTrim();
        	Element stateElement = (Element) doc.selectSingleNode("accountCheckResult/payOrder/state");
        	state = stateElement.getTextTrim();
        	Element typeElement = (Element) doc.selectSingleNode("accountCheckResult/payOrder/type");
        	type = typeElement.getTextTrim();
        } catch(CheckSignatureException e) {
        	throw e;
        } catch (Exception e) {
        	logger.error("解析返回报文失败", e);
        	throw new ServiceException("解析返回报文失败：" + e.getMessage());
        }
        
		backData.setAmount(new BigDecimal(amountSum));
		backData.setOrderId(merOrderIdb);
		backData.setChannelOrderId(payOrderId);
		backData.setCreateTime(DateUtil.strToTime(merSendTime, DateUtil.DATE_FORMAT_PATTEN));

		logger.info("state : {}", state);
		
		if(UMBPAY_SUCCESS_CODE1.equals(state)) {	//未支付
			backData.setStatus(ChargeQueryStatus.SUCCESS);
		} else if(UMBPAY_UNPAY.equals(state)) { //已取消
			backData.setStatus(ChargeQueryStatus.HANDLING);
		} else if(UMBPAY_FAILURE.equals(state)) { //已支付
			backData.setStatus(ChargeQueryStatus.FAILURE);
		} else {
			logger.error("未知的支付状态:{}", state);
			throw new ServiceException("未知的支付状态, state:" + state);
		}
		logger.info("查询结束");
		return new PartnerResult<PartnerSingleQueryChargeData>(PartnerResultCode.SUCCESS, backData);
		//return new PartnerResult<PartnerSingleQueryChargeData>(PartnerResultCode.SUCCESS);
	}

    @Override
    public BigDecimal feeCalculate(BigDecimal amount, PayType payType) {
        return config.getFeePercentage().multiply(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
