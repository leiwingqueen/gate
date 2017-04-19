package com.elend.gate.channel.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cfca.util.pki.PKIException;
import com.cfca.util.pki.api.CertUtil;
import com.cfca.util.pki.api.KeyUtil;
import com.cfca.util.pki.api.SignatureUtil;
import com.cfca.util.pki.cert.X509Cert;
import com.cfca.util.pki.cipher.JCrypto;
import com.cfca.util.pki.cipher.JKey;
import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.PartnerResultCode;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.constant.WithdrawStatus;
import com.elend.gate.channel.exception.CheckSignatureException;
import com.elend.gate.channel.facade.vo.PartnerWithdrawData;
import com.elend.gate.channel.facade.vo.WithdrawCallbackData;
import com.elend.gate.channel.service.WithdrawChannelService;
import com.elend.gate.channel.service.vo.yeepay.YeepayBatchDetailQueryRequestVO;
import com.elend.gate.channel.service.vo.yeepay.YeepayBatchDetailQueryRspVO;
import com.elend.gate.channel.service.vo.yeepay.YeepayBillDetailVO;
import com.elend.gate.channel.service.vo.yeepay.YeepaySingleWithdrawRequestVO;
import com.elend.gate.channel.service.vo.yeepay.YeepaySingleWithdrawRspVO;
import com.elend.gate.channel.service.vo.yeepay.YeepayWithdrawCallbackRspVO;
import com.elend.gate.channel.service.vo.yeepay.YeepayWithdrawCallbackVO;
import com.elend.gate.channel.util.yeepay.CallbackUtils;
import com.elend.gate.channel.util.yeepay.Digest;
import com.elend.gate.conf.facade.WithdrawYeepayConfig;
import com.elend.gate.util.XMLUtils;
import com.elend.p2p.ServiceException;

/**
 * 易宝提现渠道
 * @author mgt
 */
@Service
public class WithdrawYeepayChannel extends WithdrawChannelService {
    
    private static Logger logger = LoggerFactory.getLogger(WithdrawYeepayChannel.class);
    
    private static com.cfca.util.pki.cipher.Session tempsession = null;
    private static JCrypto jcrypto = null;
    /**返回成功码*/
    public static final String SUCCESS_CODE = "1";
    /**最多只读取500000字符*/
    public static final int MAX_FETCHSIZE = 500000;
    /**编码*/
    public static final String CHAR_SET = "GBK";
    /**
     * 成功接收通知返回 S
     */
    public static final String RET_CODE_SUCCESS = "S";
    
    public static final String RECORD_NOT_EXIST_CODE = "0048";
    
    @Autowired
    private WithdrawYeepayConfig config;
    
    
    /**
     * 易宝代付的返回码
     * @author mgt
     *
     */
    public static final class RetCode {
        /**请求成功*/
        public static final String SUCCESS = "1";
        /**系统异常*/
        public static final String EXCEPTION = "9999";
    }
    
    /**
     * 银行处理状态
     * @author mgt
     *
     */
    public static enum BankHandleStatusType {
        /**
         * 已成功
         */
        SUCCESS("S","已成功"),
        /**
         * 银行处理中
         */
        HANDLING("I","银行处理中"),
        /**
         * 出款失败
         */
        FAILURE("F","出款失败"),
        /**
         * 未出款
         */
        UN_TAKEOUT("W","未出款"),
        /**
         * 未知
         */
        UNKNOWN("U","未知");
        
        private String type;
        private String desc;
        
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public String getDesc() {
            return desc;
        }
        private BankHandleStatusType(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }
        public void setDesc(String desc) {
            this.desc = desc;
        }
        public static BankHandleStatusType from(String type) {
            for (BankHandleStatusType one : values()) {
                if (one.getType().equals(type)) {
                    return one;
                }
                
            }
            throw new IllegalArgumentException("illegal type:"+ type);
        }
    }

    
    /**
     * 易宝代付打款状态码
     * @author mgt
     *
     */
    public static final class R1Code {
        /**已接收*/
        public static final String RECEIVE = "0025"; 
        /**已汇出*/
        public static final String EXPORT = "0026"; 
        /**已退款*/
        public static final String REFUND = "0027"; 
        /**已拒绝*/
        public static final String REFUSE = "0028"; 
        /**等待复核*/
        public static final String WAIT_CHECK = "0029"; 
        /**未知*/
        public static final String UNKNOW = "0030"; 
    }
    
    /**
     * 易宝代付银行处理状态
     * @author mgt
     *
     */
    public static final class BankStatus {
        /**已成功*/
        public static final String SUCCESS = "S"; 
        /**处理中*/
        public static final String HANDLING = "I"; 
        /**出款失败*/
        public static final String FAILURE = "F"; 
        /**未出款*/
        public static final String UNEXPORT = "W"; 
        /**未知*/
        public static final String UNKNOW = "U"; 
    }
    
    /**
     * 获取加密库session
     * @return
     */
    private com.cfca.util.pki.cipher.Session getSession() {
        if (tempsession == null) {
            synchronized (this) {
                if (tempsession == null) {
                    logger.info("初始化加密库session");
                    try {
                        jcrypto = JCrypto.getInstance();
                        jcrypto.initialize(JCrypto.JSOFT_LIB, null);
                        tempsession = jcrypto.openSession(JCrypto.JSOFT_LIB);
                        logger.info("加密库初始化成功");
                    } catch (PKIException e) {
                        logger.error("初始化加密库并获得session失败", e);
                        throw new ServiceException("初始化加密库并获得session失败", e);
                    }
                }
            }
        }
        return tempsession;
    }
    
    /**
     * 对请求数据进行签名
     * @param args
     * @return
     */
    private String sign(String ...args) {
        //先初始化加密库，获取JKEY要先初始化好加密库，否则异常
        getSession();
        if(args == null) {
            throw new ServiceException("签名参数为空");
        }
        String hmacStr = "";
        for(String str : args) {
            hmacStr += (StringUtils.isBlank(str) ? "" : str);
        }
        //logger.info(("签名之前的源数据为---[" + hmacStr+"]"));
        
        //下面用数字证书进行签名
        String ALGORITHM = SignatureUtil.SHA1_RSA;
        String signMessage ="";
        SignatureUtil signUtil =null;
        try{
            //logger.info(" YeepayConstant.SECURE_CERTIFICATE_NAME  "+YeepayConstant.SECURE_CERTIFICATE_NAME);
            JKey jkey = KeyUtil.getPriKey(config.getSecureCertificateName(), config.getPfxPwd());
            X509Cert cert = CertUtil.getCert(config.getSecureCertificateName(), config.getPfxPwd());
            System.out.println(cert.getSubject());
            X509Cert[] cs=new X509Cert[1];
            cs[0]=cert;
            // (1):对请求的串进行MD5对数据进行签名
            String yphs = Digest.hmacSign(hmacStr);
            signUtil = new SignatureUtil();
            byte[] b64SignData;
            // (2):对MD5签名之后数据调用CFCA提供的api方法用商户自己的数字证书进行签名
            b64SignData = signUtil.p7SignMessage(true, yphs.getBytes(),ALGORITHM, jkey, cs, getSession());
            /*if(jcrypto!=null){
                jcrypto.finalize (com.cfca.util.pki.cipher.JCrypto.JSOFT_LIB,null);
            }*/
            signMessage = new String(b64SignData, "UTF-8");
        } catch(Exception e){
            logger.error("数据签名时失败...",e);
            throw new ServiceException("数据签名时失败", e);
        }
        logger.info("经过md5和数字证书签名之后的数据为---["+signMessage+"]"); 
        
        return signMessage;
    }
    
    /**
     * 验证返回数据的签名
     * @param hamc
     * @param args
     */
    private void checkSign(String hamc, String ...args) {
        //先初始化加密库
        getSession();
        boolean sigerCertFlag = true;
        String cmdValue = hamc;
        SignatureUtil signUtil =null;
        try {
            if(StringUtils.isNotBlank(cmdValue)){
                signUtil = new SignatureUtil();
                sigerCertFlag = signUtil.p7VerifySignMessage(cmdValue.getBytes(), getSession());
                String backmd5hmac = "";
                if(sigerCertFlag){
                    logger.info("证书验签成功");
                    backmd5hmac = new String(signUtil.getSignedContent());
                    //logger.info("证书验签获得的MD5签名数据为----" + backmd5hmac);
                    //logger.info("证书验签获得的证书dn为----" + new String(signUtil.getSigerCert()[0].getSubject()));
                    
                    // 将验签出来的结果数据与自己针对响应数据做MD5签名之后的数据进行比较是否相等
                    if(args != null) {
                        String backHmacStr = "";
                        for(String str : args) {
                            backHmacStr += (StringUtils.isBlank(str) ? "" : str);
                        }
                        String newmd5hmac = Digest.hmacSign(backHmacStr);
                        //logger.info("提交返回源数据为---[" + backHmacStr+"]");
                        //logger.info("经过md5签名后的验证返回hmac为---[" + newmd5hmac+"]");
                        //logger.info("提交返回的hmac为---[" + backmd5hmac+"]");
                        if(newmd5hmac.equals(backmd5hmac)){
                            logger.info("md5验签成功");
                            //判断该证书DN是否为易宝
                            if(signUtil.getSigerCert()[0].getSubject().toUpperCase().indexOf("OU=YEEPAY,") > 0){
                                logger.info("证书DN验证成功");
                            }else{
                                throw new CheckSignatureException("证书DN不是易宝的");
                            }
                        }else{
                            throw new CheckSignatureException("md5验签失败");
                        }
                    }
                }else{
                    throw new CheckSignatureException("证书验签失败....");
                }
            } else {
                throw new CheckSignatureException("返回签名信息hamc为空");
            }
        } catch (Exception e) {
            logger.error("对服务器响应报文进行验证签名失败", e);
            throw new CheckSignatureException("对服务器响应报文进行验证签名失败", e);
        }
    }

    @Override
    public WithdrawChannel getChannelId() {
        return WithdrawChannel.YEEPAY_WITHDRAW;
    }

    @Override
    public BigDecimal feeCalculate(BigDecimal amount) {
        return new BigDecimal(config.getFixFee());
    }
    
    @Override
    public PartnerResult<WithdrawCallbackData> withdrawSingle(String orderId, PartnerWithdrawData data) {
        logger.info("请求参数：orderId = {}, data:{}", orderId, data);
        
        logger.info("易宝单笔单款开始...");
        
        PartnerResult<WithdrawCallbackData> result = new PartnerResult<WithdrawCallbackData>();
        WithdrawCallbackData backData = new WithdrawCallbackData();
        result.setObject(backData);
        backData.setOrderId(orderId);
        backData.setFee(feeCalculate(backData.getAmount()));
        backData.setAmount(data.getAmount());
        backData.setRequestUrl(config.getRequestUril());
        YeepaySingleWithdrawRspVO rspVo;
        Document doc;
        try {
            
            if(StringUtils.isBlank(orderId) || orderId.length() != 15) {
                throw new ServiceException("无效的订单号 :" + orderId);
            }

            YeepaySingleWithdrawRequestVO vo = new YeepaySingleWithdrawRequestVO();
            
            logger.info("封装请求参数...");

            //1、按接口补充请求参数
            //获取提现的银行卡号信息
            String cardNo = StringUtils.trimToEmpty(data.getBankAccount());
            String userName = StringUtils.trimToEmpty(data.getUserName());

            String bankBranchName = StringUtils.trimToEmpty(data.getBankBranchName());
            
            String bankCode = this.getChannelBankId(data.getBankId());
            if(StringUtils.isBlank(bankCode)) {
                throw new ServiceException("错误的银行ID：" + data.getBankId().getBankId());
            }
            
            vo.setCmd("TransferSingle");
            vo.setVersion("1.1");
            vo.setGroupId(config.getMchtNo());
            vo.setMerId(config.getMchtNo());
            vo.setBatchNo(orderId);
            vo.setBankCode(bankCode);
            vo.setOrderId(orderId);
            //vo.setCnaps("");  
            //vo.setBankName(bankName);
            vo.setBranchBankName(bankBranchName);
            vo.setAmount(data.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            vo.setAccountName(userName);
            vo.setAccountNumber(cardNo);
            
            //对公对私
            if("1".equals(data.getAccountType())) {
                vo.setAccoutType("pu"); //对公
            } else {
                vo.setAccoutType("pr"); //对私
            }
            
            //易宝要求的是六位的城市编号，我们记录的4位的城市编号
            //vo.setProvince(card.getBankProvince() > 0 ? card.getBankProvince()+"" : "");
            //vo.setCity(card.getBankCity() > 0 ? card.getBankCity() + "" : "");
            
            //手续费一律商户承担，打款前对用户的提现金额进行扣除
            vo.setFeeType("SOURCE");
            
            //默认普通提现（后续看是否修改）
            vo.setUrgency("0");

            logger.info("请求参数签名...");
            
            // 2、将请求的数据进行签名
            // cmd,mer_Id,batch_No,order_Id,amount,account_Number,hmacKey
            String signMessage = sign(vo.getCmd(), vo.getMerId(), vo.getBatchNo(), vo.getOrderId(), vo.getAmount(), vo.getAccountNumber(), config.getPwd());
            vo.setHmac(signMessage);
            
            try {
                doc = XMLUtils.generateDocument(vo, CHAR_SET);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new ServiceException("请求对象转化XML失败", e);
            }
        } catch (Exception e) {
            logger.error("发送请求前异常", e);
            backData.setWithdrawStatus(WithdrawStatus.FAILURE);
            result.setCode(PartnerResultCode.FAILURE);
            result.setMessage(e.getMessage());
            logger.info("异常，退出易宝单笔打款...");
            return result;
        }
            
        result.setCode(PartnerResultCode.SUCCESS);
        result.setMessage("提现发送请求成功");

        try {
            // 3、发送https请求
            logger.info("发送请求...");
            logger.info("请求报文：{}", doc.asXML().replaceAll("<account_Name>.+</account_Name>", "<account_Name>***</account_Name>")
                        .replaceAll("<account_Number>.+</account_Number>", "<account_Number>**** ****</account_Number>")
                    );
            String responseMsg = CallbackUtils.httpRequest(config.getRequestUril(), doc.asXML(), "POST", CHAR_SET, "text/xml ;charset=gbk", false);
            if(StringUtils.isBlank(responseMsg)) {
                throw new ServiceException("获取返回报文失败");
            }
            logger.info("返回报文：{}", responseMsg);
            logger.info("解析返回报文...");
            //4、解析返回报文
            try {
                doc = DocumentHelper.parseText(responseMsg);
                rspVo = (YeepaySingleWithdrawRspVO) XMLUtils.parseDocument(YeepaySingleWithdrawRspVO.class, doc);
            } catch (Exception e) {
                throw new ServiceException("解析返回报文失败", e);
            } 
            
            logger.info("验证返回报文的签名...");
            
            // 5、对服务器响应报文进行验证签名
            checkSign(rspVo.getHmac(), rspVo.getCmd(), rspVo.getRetCode(), rspVo.getR1Code(), config.getPwd());
            
            logger.info("判断返回状态...");
            logger.info("retCode:{}, r1Code:{}, bankStatus:{}", rspVo.getRetCode(), rspVo.getR1Code(), rspVo.getBankStatus());
            
            //响应正常，签名校验成功
            backData.setMessage(rspVo.getErrMsg());
            backData.setOrderId(rspVo.getOrderId());
            if(!(RetCode.SUCCESS.equals(rspVo.retCode) || RetCode.EXCEPTION.equals(rspVo.retCode))) { // 非 1 非9999
                backData.setWithdrawStatus(WithdrawStatus.FAILURE);
            } else if(RetCode.SUCCESS.equals(rspVo.retCode)) { //1
                if(R1Code.REFUSE.equals(rspVo.getR1Code()) || R1Code.REFUND.equals(rspVo.getR1Code())) { // 0027 已退款  0028 已拒绝
                    backData.setWithdrawStatus(WithdrawStatus.FAILURE);
                } else if(R1Code.RECEIVE.equals(rspVo.getR1Code())) {  //已经收
                    backData.setWithdrawStatus(WithdrawStatus.APPLYING);//申请中
                } else if(R1Code.UNKNOW.equals(rspVo.getR1Code())) {  //未知
                    backData.setWithdrawStatus(WithdrawStatus.APPLYING); 
                } else if(R1Code.WAIT_CHECK.equals(rspVo.getR1Code())) {//等待复核
                    backData.setWithdrawStatus(WithdrawStatus.APPLYING);
                } else if(R1Code.EXPORT.equals(rspVo.getR1Code())) {
                    if(BankStatus.SUCCESS.equals(rspVo.getBankStatus())) {
                        backData.setWithdrawStatus(WithdrawStatus.SUCCESS);
                    } else if(BankStatus.FAILURE.equals(rspVo.getBankStatus())) {
                        backData.setWithdrawStatus(WithdrawStatus.FAILURE);
                    } else if(BankStatus.HANDLING.equals(rspVo.getBankStatus()) || BankStatus.UNEXPORT.equals(rspVo.getBankStatus())) {
                        backData.setWithdrawStatus(WithdrawStatus.APPLYING);
                    } else {
                        backData.setWithdrawStatus(WithdrawStatus.APPLYING);
                    }
                } else {
                    backData.setWithdrawStatus(WithdrawStatus.APPLYING);
                }
            } else {
                backData.setWithdrawStatus(WithdrawStatus.APPLYING);
            }
            result.setObject(backData);
            logger.info("单笔打款请求完成，正常退出");
            return result;
        } catch (Exception e) {
            logger.error("提现请求通讯过程发生异常", e);
            backData.setWithdrawStatus(WithdrawStatus.APPLYING);
            backData.setMessage(e.getMessage());
            logger.info("异常，退出宝易互通单笔打款...");
            return result;
        }
    }

    @Override
    public PartnerResult<WithdrawCallbackData> withdrawCallback(String xmlStr) {
        logger.info("打款回调开始...");
        
        PartnerResult<WithdrawCallbackData> result = new PartnerResult<WithdrawCallbackData>();
        
        logger.info("通知xmlStr:{}", xmlStr);
        
        //1.解析返回报文
        YeepayWithdrawCallbackVO backVo = null;
        try {
            Document doc = DocumentHelper.parseText(xmlStr.toString());
            backVo = (YeepayWithdrawCallbackVO) XMLUtils.parseDocument(YeepayWithdrawCallbackVO.class, doc);
            logger.info("解析后的报文返回,backVo:{}",backVo.toString());
        } catch (Exception e) {
            throw new ServiceException("解析返回报文失败", e);
        } 
        
        //2.验证信息的签名
        checkSign(backVo.getHmac(), backVo.getCmd(), backVo.getMerId(), backVo.getBatchNo(), backVo.getOrderId(), backVo.getStatus(), backVo.getMessage(), config.getPwd());
        
        //3.封装返回报文
        YeepayWithdrawCallbackRspVO rspVO = new YeepayWithdrawCallbackRspVO();
        rspVO.setCmd("TransferNotify");
        rspVO.setMerId(backVo.getMerId());
        rspVO.setBatchNo(backVo.getBatchNo());
        rspVO.setOrderId(backVo.getOrderId());
        rspVO.setRetCode(RET_CODE_SUCCESS);
        //数据签名
        String hmac = sign(rspVO.getCmd(), rspVO.getMerId(), rspVO.getBatchNo(), rspVO.getOrderId(), rspVO.getRetCode(), config.getPwd());
        rspVO.setHmac(hmac);
        
        //7.封装参数返回
        WithdrawCallbackData backData = new WithdrawCallbackData();
        
        backData.setFee(feeCalculate(BigDecimal.ZERO));
        backData.setCallbackTime(new Date());
        backData.setRequestUrl(config.getRequestUril());
        backData.setOrderId(backVo.getOrderId());
        
        logger.info("status : {}", backVo.getStatus());
        
        BankHandleStatusType status = BankHandleStatusType.from(backVo.getStatus());
        if(status == BankHandleStatusType.SUCCESS) {
            backData.setWithdrawStatus(WithdrawStatus.SUCCESS);
        } else if(status == BankHandleStatusType.FAILURE) {
            backData.setWithdrawStatus(WithdrawStatus.FAILURE);
        } else {
            throw new ServiceException("异步回调异常， 返回的不是最终的结果");
        }
        backData.setMessage(backVo.getMessage());
        result.setObject(backData);
        try {
            backData.setResponseStr(XMLUtils.generateDocument(rspVO, CHAR_SET).asXML());
        } catch (Exception e) {
            logger.error("响应对象转换xml失败", e);
        }

        //hmac太长，去掉
        backVo.setHmac("");
        //回调参数
        backData.setCallbackStr(JSON.toJSONString(backVo));
        
        result.setCode(PartnerResultCode.SUCCESS);
        result.setMessage("打款回调成功");
        logger.info("回调处理结束..."); 
        return result;
    }

    @Override
    public PartnerResult<WithdrawCallbackData> withdrawSingleQuery(
            String orderId) {

        logger.info("单笔提现查询开始...");
        PartnerResult<WithdrawCallbackData> result = new PartnerResult<WithdrawCallbackData>();
        
        YeepayBatchDetailQueryRspVO rspVo;

        String pageNo = "1";
            
        // 1.补全请求信息
        YeepayBatchDetailQueryRequestVO vo = new YeepayBatchDetailQueryRequestVO();
        vo.setCmd("BatchDetailQuery");
        vo.setVersion("1.0");
        vo.setGroupId(config.getMchtNo());
        vo.setMerId(config.getMchtNo());
        vo.setQueryMode("3");
        vo.setProduct("");
        vo.setBatchNo(orderId);
        vo.setOrderId(orderId);
        vo.setPageNo(pageNo);

        // 2、将请求的数据进行签名
        // cmd 、 mer_Id 、 batch_No 、order_Id 、page_No 的参数值+商户密钥组成字符串
        String signMessage = sign(vo.getCmd(), vo.getMerId(),
                                  vo.getBatchNo(), vo.getOrderId(),
                                  vo.getPageNo(),
                                  config.getPwd());
        vo.setHmac(signMessage);

        // 3、发送https请求
        Document doc;
        try {
            doc = XMLUtils.generateDocument(vo, CHAR_SET);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            throw new ServiceException("请求对象转化XML失败", e);
        }

        logger.info("请求报文：{}", doc.asXML());
        String responseMsg = CallbackUtils.httpRequest(config.getQueryBalanceUrl(),
                                                       doc.asXML(),
                                                       "POST",
                                                       "gbk",
                                                       "text/xml ;charset=gbk",
                                                       false);
        if (StringUtils.isBlank(responseMsg)) {
            logger.info("返回报文为空");
            throw new ServiceException("获取返回报文失败");
        }
        logger.info("返回报文：{}", responseMsg.replaceAll("<payee_Name>.+</payee_Name>", "<payee_Name>***</payee_Name>")
                    .replaceAll("<payee_Bank_Account>.+</payee_Bank_Account>", "<payee_Bank_Account>**** ****</payee_Bank_Account>")
                );
        // 4、解析返回报文
        try {
            doc = DocumentHelper.parseText(responseMsg);
            rspVo = (YeepayBatchDetailQueryRspVO) XMLUtils.parseDocument(YeepayBatchDetailQueryRspVO.class, doc);
        } catch (Exception e) {
            throw new ServiceException("解析返回报文失败", e);
        }

        // 5、对服务器响应报文进行验证签名
        // cmd、ret_Code、batch_No、total_Num、end_Flag 的参数值+商户密钥组成字符串
        checkSign(rspVo.getHmac(), rspVo.getCmd(), rspVo.getRetCode(),
                  rspVo.getBatchNo(), rspVo.getTotalNum(),
                  rspVo.getEndFlag(), config.getPwd());
        
        // 6、返回结果
        /*WithdrawSingleQueryData queryData = new WithdrawSingleQueryData();
        
        result.setObject(queryData);
        queryData.setOrderId(orderId);
        
        logger.info("retCode:{}, batchNo:{}, totalNum:{}, errorMsg:{}", rspVo.getRetCode(), rspVo.getBatchNo(), rspVo.getTotalNum(), rspVo.getErrorMsg());
        
        if(RECORD_NOT_EXIST_CODE.equals(rspVo.getRetCode())) {  //记录不存在
            logger.info("记录不存在：{}", orderId);
            
            result.setCode(PartnerResultCode.SUCCESS);
            result.setMessage("查询成功");

            queryData.setMessage(rspVo.getErrorMsg());
            queryData.setWithdrawStatus(WithdrawStatus.FAILURE);

        } else if (SUCCESS_CODE.equals(rspVo.getRetCode())) { //查询成功
            
            result.setCode(PartnerResultCode.SUCCESS);
            result.setMessage("查询成功");
            
            if(!"1".equals(rspVo.getTotalNum())) { //传输订单号，记录数只能为1
                logger.info("返回记录数异常：{}", rspVo.getTotalNum());
                throw new ServiceException("返回记录数异常：" + rspVo.getTotalNum());
            }
            
            if(rspVo.getList() == null || rspVo.getList().size() != 1) {
                logger.info("返回实际记录数异常：{}", rspVo.getList());
                throw new ServiceException("返回实际记录数异常");
            }
            
            YeepayBillDetailVO yb = rspVo.getList().get(0);
            
            queryData.setMessage(yb.getFail_Desc());
            
            if(R1Code.REFUSE.equals(yb.getR1Code()) || R1Code.REFUND.equals(yb.getR1Code())) { // 0027 已退款  0028 已拒绝
                queryData.setWithdrawStatus(WithdrawStatus.FAILURE);
            } else if(R1Code.RECEIVE.equals(yb.getR1Code())) {
                queryData.setWithdrawStatus(WithdrawStatus.APPLYING);
            } else if(R1Code.UNKNOW.equals(yb.getR1Code())) {
                queryData.setWithdrawStatus(WithdrawStatus.APPLYING);
            } else if(R1Code.WAIT_CHECK.equals(yb.getR1Code())) {
                queryData.setWithdrawStatus(WithdrawStatus.APPLYING);
            } else if(R1Code.EXPORT.equals(yb.getR1Code())) {
                if(BankStatus.SUCCESS.equals(yb.getBankStatus())) {
                    queryData.setWithdrawStatus(WithdrawStatus.SUCCESS);
                } else if(BankStatus.FAILURE.equals(yb.getBankStatus())) {
                    queryData.setWithdrawStatus(WithdrawStatus.FAILURE);
                } else if(BankStatus.HANDLING.equals(yb.getBankStatus()) || BankStatus.UNEXPORT.equals(yb.getBankStatus())) {
                    queryData.setWithdrawStatus(WithdrawStatus.APPLYING);
                } else {
                    queryData.setWithdrawStatus(WithdrawStatus.APPLYING);
                }
            } else {
                queryData.setWithdrawStatus(WithdrawStatus.APPLYING);
            }
        
        } else {
            logger.error("error code : {}, error msg : {}", rspVo.getRetCode(), rspVo.getErrorMsg());
            
            result.setCode(PartnerResultCode.FAILURE);
            result.setMessage(rspVo.getErrorMsg()); // 错误描述
        }*/
        
        logger.info("retCode:{}, batchNo:{}, totalNum:{}, errorMsg:{}", rspVo.getRetCode(), rspVo.getBatchNo(), rspVo.getTotalNum(), rspVo.getErrorMsg());

        WithdrawCallbackData backData = new WithdrawCallbackData();
        
        backData.setFee(feeCalculate(BigDecimal.ZERO));
        backData.setCallbackTime(new Date());
        backData.setRequestUrl(config.getRequestUril());
        backData.setOrderId(orderId);
        backData.setChannelOrderId(orderId);
        result.setObject(backData);
        
        //回调参数
        backData.setCallbackStr(JSON.toJSONString(rspVo));
        
        if(RECORD_NOT_EXIST_CODE.equals(rspVo.getRetCode())) {  //记录不存在
            logger.info("记录不存在：{}", orderId);
            
            result.setCode(PartnerResultCode.SUCCESS);
            
            backData.setWithdrawStatus(WithdrawStatus.FAILURE);
            backData.setMessage(rspVo.getErrorMsg());

        } else if (SUCCESS_CODE.equals(rspVo.getRetCode())) { //查询成功
            
            result.setCode(PartnerResultCode.SUCCESS);
            
            if(!"1".equals(rspVo.getTotalNum())) { //传输订单号，记录数只能为1
                logger.info("返回记录数异常：{}", rspVo.getTotalNum());
                throw new ServiceException("返回记录数异常：" + rspVo.getTotalNum());
            }
            
            if(rspVo.getList() == null || rspVo.getList().size() != 1) {
                logger.info("返回实际记录数异常：{}", rspVo.getList());
                throw new ServiceException("返回实际记录数异常");
            }
            
            YeepayBillDetailVO yb = rspVo.getList().get(0);
            backData.setMessage(yb.getFail_Desc());
            
            if(R1Code.REFUSE.equals(yb.getR1Code()) || R1Code.REFUND.equals(yb.getR1Code())) { // 0027 已退款  0028 已拒绝
                backData.setWithdrawStatus(WithdrawStatus.FAILURE);
            } else if(R1Code.RECEIVE.equals(yb.getR1Code())) {
                backData.setWithdrawStatus(WithdrawStatus.APPLYING);
            } else if(R1Code.UNKNOW.equals(yb.getR1Code())) {
                backData.setWithdrawStatus(WithdrawStatus.APPLYING);
            } else if(R1Code.WAIT_CHECK.equals(yb.getR1Code())) {
                backData.setWithdrawStatus(WithdrawStatus.APPLYING);
            } else if(R1Code.EXPORT.equals(yb.getR1Code())) {
                if(BankStatus.SUCCESS.equals(yb.getBankStatus())) {
                    backData.setWithdrawStatus(WithdrawStatus.SUCCESS);
                } else if(BankStatus.FAILURE.equals(yb.getBankStatus())) {
                    backData.setWithdrawStatus(WithdrawStatus.FAILURE);
                } else if(BankStatus.HANDLING.equals(yb.getBankStatus()) || BankStatus.UNEXPORT.equals(yb.getBankStatus())) {
                    backData.setWithdrawStatus(WithdrawStatus.APPLYING);
                } else {
                    backData.setWithdrawStatus(WithdrawStatus.APPLYING);
                }
            } else {
                backData.setWithdrawStatus(WithdrawStatus.APPLYING);
            }
        } else {
            logger.error("error code : {}, error msg : {}", rspVo.getRetCode(), rspVo.getErrorMsg());
            result.setMessage(rspVo.getErrorMsg()); // 错误描述
            result.setCode(PartnerResultCode.FAILURE);
        }
        
        logger.info("查询结束");
        return result;
    }
}
