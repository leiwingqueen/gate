package com.elend.gate.channel.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.elend.gate.channel.util.YeepayUtil;
import com.elend.gate.conf.facade.YeepayConfig;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.ServiceException;
import com.elend.p2p.gson.JSONUtils;
import com.elend.p2p.util.DateUtil;
import com.yeepay.DigestUtil;
import com.yeepay.HttpUtils;
import com.yeepay.QueryResult;

/**
 * 易宝支付渠道
 * 
 * @author liyongquan 2015年5月21日
 */
@Service
public class YeepayChannel extends PayChannelService {
    private final static Logger logger = LoggerFactory.getLogger(YeepayChannel.class);
    //易宝成功的返回码
    private final static String YEEPAY_SUCCESS_CODE="1";
    //易宝标识本次是否页面重定向的通知
    private final static String YEEPAY_REDIRECT="1";
    //易宝标识本次是否点对点的通知
    private final static String YEEPAY_P2P="2";
    

    @Autowired
    private YeepayConfig config;
    
    //易宝比较特殊，传入银行卡编号的时候需要在后面增加-B2C的字符串，而返回的时候却没有-B2C这个后缀
    @Override
    protected String getChannelBankId(BankIdConstant bankId){
        String channelBandId=super.getChannelBankId(bankId);
        if(StringUtils.isNotBlank(channelBandId)){//
            channelBandId=channelBandId+"-B2C";
        }
        return channelBandId;
    }

    @Override
    public RequestFormData charge(String orderId,PartnerChargeData chargeData){
        logger.info("易宝充值请求,orderId:{},amount:{}", orderId, chargeData.getAmount());
        String keyValue = config.getPriKey(); // 商家密钥
        // 商家设置用户购买商品的支付信息
        String p0_Cmd = "Buy"; // 在线支付请求，固定值 ”Buy”
        String p1_MerId = config.getMerId(); // 商户编号
        String p2_Order = orderId; // 商户订单号
        String p3_Amt = String.format("%.2f",
                                      chargeData.getAmount().setScale(2,
                                                      BigDecimal.ROUND_DOWN)); // 支付金额
        String p4_Cur = "CNY"; // 交易币种
        String p5_Pid = "GZYD";
        String p6_Pcat = "";
        String p7_Pdesc = "";
        String p8_Url = config.getCallbackUrl(); // 商户接收支付成功数据的地址
        String p9_SAF = "";
        String pa_MP = "";
        //银行ID转换成渠道对应的银行
        String channelBandId=this.getChannelBankId(chargeData.getBankId());
        //String channelBandId = "";
        String pd_FrpId = channelBandId;
        String pr_NeedResponse = "1";
        // 获得MD5-HMAC签名
        String hmac = YeepayUtil.getReqMd5HmacForOnlinePayment(p0_Cmd,
                                                               p1_MerId,
                                                               p2_Order,
                                                               p3_Amt,
                                                               p4_Cur,
                                                               p5_Pid,
                                                               p6_Pcat,
                                                               p7_Pdesc,
                                                               p8_Url,
                                                               p9_SAF,
                                                               pa_MP,
                                                               pd_FrpId,
                                                               pr_NeedResponse,
                                                               keyValue);
        RequestFormData form = new RequestFormData();
        form.setRequestUrl(config.getYeepayCommonReqURL());
        form.addParam("p0_Cmd", p0_Cmd);
        form.addParam("p1_MerId", p1_MerId);
        form.addParam("p2_Order", orderId);
        form.addParam("p3_Amt", p3_Amt);
        form.addParam("p4_Cur", p4_Cur);
        form.addParam("p5_Pid", p5_Pid);
        form.addParam("p8_Url", p8_Url);
        form.addParam("pr_NeedResponse", pr_NeedResponse);
        form.addParam("pd_FrpId", pd_FrpId);
        form.addParam("hmac", hmac);
        logger.info("易宝充值请求表单生成,form:{}", JSONUtils.toJson(form, false));
        return form;
    }

    @Override
    public ChannelIdConstant getChannelId() {
        return ChannelIdConstant.YEEPAY;
    }

    @Override
    public PartnerResult<ChargeCallbackData> chargeCallback(
            ChannelIdConstant channelId, Map<String, String> params)
            throws CheckSignatureException, ServiceException {
        logger.info("charge callback,channelId:{},params:{}", channelId,
                    JSONUtils.toJson(params, false));
        String keyValue = config.getPriKey(); // 商家密钥
        String r0_Cmd = params.get("r0_Cmd"); // 业务类型
        String p1_MerId = formatString(params.get("p1_MerId")); // 商户编号
        String r1_Code = formatString(params.get("r1_Code"));// 支付结果
        String r2_TrxId = formatString(params.get("r2_TrxId"));// 易宝支付交易流水号
        String r3_Amt = formatString(params.get("r3_Amt"));// 支付金额
        String r4_Cur = formatString(params.get("r4_Cur"));// 交易币种
        String r5_Pid;
        String r8_MP;
        try {
            r5_Pid = new String(
                                formatString(params.get("r5_Pid")).getBytes("iso-8859-1"),
                                "gbk");
            r8_MP = new String(
                               formatString(params.get("r8_MP")).getBytes("iso-8859-1"),
                               "gbk");// 商户扩展信息
        } catch (UnsupportedEncodingException e) {
            logger.error("编码失败");
            throw new ServiceException("编码失败");
        }
        // 商品名称
        String r6_Order = formatString(params.get("r6_Order"));// 商户订单号
        String r7_Uid = formatString(params.get("r7_Uid"));// 易宝支付会员ID
        String r9_BType = formatString(params.get("r9_BType"));// 交易结果返回类型
        String hmac = formatString(params.get("hmac"));// 签名数据
        boolean isOK = false;
        // 校验返回数据包
        isOK = YeepayUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code,
                                         r2_TrxId, r3_Amt, r4_Cur, r5_Pid,
                                         r6_Order, r7_Uid, r8_MP, r9_BType,
                                         keyValue);
        if (isOK) {
            ChargeCallbackData callbackData = new ChargeCallbackData();
            callbackData.setAmount(new BigDecimal(r3_Amt));
            callbackData.setCallbackStr("SUCCESS");
            callbackData.setChannelOrderId(r2_TrxId);
            callbackData.setOrderId(r6_Order);
            if (r1_Code.equals(YEEPAY_SUCCESS_CODE)) {
                callbackData.setChannelBankId(params.get("rb_BankId"));
                String bankId=getBankId(callbackData.getChannelBankId());
                callbackData.setBankId(bankId);
                callbackData.setPayTime(DateUtil.strToTime(params.get("rp_PayDate"),DateUtil.DATE_FORMAT_PATTEN4));
                callbackData.setNoticeTime(DateUtil.strToTime(params.get("ru_Trxtime"),DateUtil.DATE_FORMAT_PATTEN4));
                if (r9_BType.equals(YEEPAY_REDIRECT)) {
                    callbackData.setNotify(false);
                } else if (r9_BType.equals(YEEPAY_P2P)) {
                    callbackData.setNotify(true);
                }
                //获取手续费 modify by liyongquan 20170209
                BigDecimal fee=BigDecimal.ZERO;
                try {
                    fee = StringUtils.isBlank(params.get("rq_TargetFee"))?BigDecimal.ZERO:new BigDecimal(params.get("rq_TargetFee"));
                } catch (Exception e) {
                    logger.error("获取手续费失败...fee:{}",params.get("rq_TargetFee"));
                }
                callbackData.setFee(fee);
                logger.info("易宝交易成功,callbackData:{}", callbackData);
                return new PartnerResult<ChargeCallbackData>(PartnerResultCode.SUCCESS,
                                                      callbackData);
            } else {
                logger.error("易宝交易失败,失败返回码:{},callbackData:{}", r1_Code,
                             callbackData);
                return new PartnerResult<ChargeCallbackData>(PartnerResultCode.FAILURE,
                                                      callbackData, "错误码:"
                                                              + r1_Code);
            }
        } else {
            logger.error("交易签名被篡改.channelId:{},params:{}", channelId,
                         JSONUtils.toJson(params, false));
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
		logger.info("singleQueryCharge, channelId:{}, orderId:{}", channelId, orderId);
		if(StringUtils.isBlank(orderId) || StringUtils.trimToEmpty(orderId).length() > 50) {
			logger.error("无效的订单号:" + orderId);
			throw new ServiceException("无效的订单号:" + orderId);
		}
		String p1_MerId 			= config.getMerId(); 				// 商家ID
		String queryRefundReqURL 	= config.getQueryChargeReqURL();	// 请求地址
		String keyValue 			= config.getPriKey();				// 商家密钥
		String query_Cmd  			= "QueryOrdDetail";       			// 订单查询请求，固定值” QueryOrdDetail”
		String decodeCharset 		= "GBK";			   				// 定义编码格式
		
		QueryResult qr = null;
		String hmac = DigestUtil.getHmac(new String[] {query_Cmd, p1_MerId, orderId}, keyValue);
		Map<String, String> reParams = new HashMap<>();
		reParams.put("p0_Cmd", query_Cmd);
		reParams.put("p1_MerId", p1_MerId);
		reParams.put("p2_Order", orderId);
		reParams.put("hmac", hmac);
		List<String> responseStr = null; 
		
		try {
			logger.info("发送请求...");
			logger.info("请求数据:{}", reParams.toString());
			responseStr = HttpUtils.URLGet(queryRefundReqURL, reParams);
			logger.info("返回数据:{}", responseStr);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		if (responseStr.size() == 0) {
			throw new ServiceException("No response.");
		}
		qr = new QueryResult();
		for (int t = 0; t < responseStr.size(); t++) {
			String currentResult = (String) responseStr.get(t);
			if (currentResult == null || currentResult.equals("")) {
				continue;
			}
			int i = currentResult.indexOf("=");
			int j = currentResult.length();
			if (i >= 0) {
				String sKey = currentResult.substring(0, i);
				String sValue = currentResult.substring(i + 1);
				try {
					sValue = URLDecoder.decode(sValue, decodeCharset);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e.getMessage());
				}
				if (sKey.equals("r0_Cmd")) {
					qr.setR0_Cmd(sValue);
				} else if (sKey.equals("r1_Code")) {
					qr.setR1_Code(sValue);
				} else if (sKey.equals("r2_TrxId")) {
					qr.setR2_TrxId(sValue);
				} else if (sKey.equals("r3_Amt")) {
					qr.setR3_Amt(sValue);
				} else if (sKey.equals("r4_Cur")) {
					qr.setR4_Cur(sValue);
				} else if (sKey.equals("r5_Pid")) {
					qr.setR5_Pid(sValue);
				} else if (sKey.equals("r6_Order")) {
					qr.setR6_Order(sValue);
				} else if (sKey.equals("r8_MP")) {
					qr.setR8_MP(sValue);
				} else if (sKey.equals("rb_PayStatus")) {
					qr.setRb_PayStatus(sValue);
				} else if (sKey.equals("rc_RefundCount")) {
					qr.setRc_RefundCount(sValue);
				} else if (sKey.equals("rd_RefundAmt")) {
					qr.setRd_RefundAmt(sValue);
				} else if (sKey.equals("hmac")) {
					qr.setHmac(sValue);
				}
			}
		}
		
		PartnerSingleQueryChargeData backDate = new PartnerSingleQueryChargeData();
		backDate.setOrderId(orderId);
		logger.info("R1_Code:{}", qr.getR1_Code());
		if("50".equals(qr.getR1_Code())) {
			backDate.setStatus(ChargeQueryStatus.NOT_EXIST);
			logger.error("订单不存在:{}", orderId);
			return new PartnerResult<PartnerSingleQueryChargeData>(PartnerResultCode.SUCCESS, backDate);
		}
		
		if(!qr.getR1_Code().equals("1")) {
			logger.error("查询失败:{}", qr.getR1_Code());
			return new PartnerResult<PartnerSingleQueryChargeData>(PartnerResultCode.FAILURE, backDate, "错误码：" + qr.getR1_Code());
		} 
		
		String newHmac = "";
		newHmac = DigestUtil.getHmac(new String[] { qr.getR0_Cmd(), qr.getR1_Code(), qr.getR2_TrxId(),
				  qr.getR3_Amt(), qr.getR4_Cur(), qr.getR5_Pid(), qr.getR6_Order(), qr.getR8_MP(),
				  qr.getRb_PayStatus(), qr.getRc_RefundCount(),qr.getRd_RefundAmt()}, keyValue);
		if (!newHmac.equals(qr.getHmac())) {
			throw new CheckSignatureException("签名校验失败");
		}
		
		backDate.setAmount(new BigDecimal(qr.getR3_Amt()));
		backDate.setOrderId(qr.getR6_Order());
		backDate.setChannelOrderId(qr.getR2_TrxId());
		logger.info("Rb_PayStatus:{}", qr.getRb_PayStatus());
		if("INIT".equals(qr.getRb_PayStatus())) {	//未支付
			backDate.setStatus(ChargeQueryStatus.HANDLING);
		} else if("CANCELED".equals(qr.getRb_PayStatus())) { //已取消
			backDate.setStatus(ChargeQueryStatus.FAILURE);
		} else if("SUCCESS".equals(qr.getRb_PayStatus())) { //已支付
			backDate.setStatus(ChargeQueryStatus.SUCCESS);
		} else {
			logger.error("未知的支付状态:{}", qr.getRb_PayStatus());
			throw new ServiceException("未知的支付状态");
		}
		logger.info("查询结束");
		return new PartnerResult<PartnerSingleQueryChargeData>(PartnerResultCode.SUCCESS, backDate);
	}

    @Override
    public BigDecimal feeCalculate(BigDecimal amount, PayType payType) {
        
        //企业费率每笔10元
        if(payType == PayType.ENTERPRISE) {
            return config.getFeeEnterprise();
        }
        
        return config.getFeePercentage().multiply(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
