package com.elend.gate.channel.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.PartnerResultCode;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.constant.WithdrawStatus;
import com.elend.gate.channel.facade.vo.PartnerWithdrawData;
import com.elend.gate.channel.facade.vo.WithdrawCallbackData;
import com.elend.gate.channel.facade.vo.WithdrawSingleQueryData;
import com.elend.gate.channel.service.WithdrawChannelService;
import com.elend.gate.channel.service.vo.baofoo.TransConstant;
import com.elend.gate.channel.service.vo.baofoo.TransContent;
import com.elend.gate.channel.service.vo.baofoo.request.TransReqBF0040001;
import com.elend.gate.channel.service.vo.baofoo.request.TransReqBF0040002;
import com.elend.gate.channel.service.vo.baofoo.response.TransRespBF0040001;
import com.elend.gate.channel.service.vo.baofoo.response.TransRespBF0040002;
import com.elend.gate.channel.util.baofoo.RsaCodingUtil;
import com.elend.gate.conf.data.CityData;
import com.elend.gate.conf.facade.WithdrawBaofooConfig;
import com.elend.gate.conf.facade.vo.SCity;
import com.elend.gate.conf.facade.vo.SProvince;
import com.elend.p2p.ServiceException;
import com.elend.p2p.util.HttpUtils;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;

/**
 * 宝付提现渠道
 * @author mgt
 * @date 2016年8月31日
 */
@Service
public class WithdrawBaofooChannel extends WithdrawChannelService {
    
    private static Logger logger = LoggerFactory.getLogger(WithdrawBaofooChannel.class);
    
    @Autowired
    private WithdrawBaofooConfig config;
    
    /**
     * 数据类型
     */
    public final static String DATA_TYPE = TransConstant.data_type_xml;
    
    
    private DesPropertiesEncoder desEncoder = new DesPropertiesEncoder();
    
    /**
     * 响应吗
     * @author mgt
     * @date 2016年8月31日
     */
    public static final class ReturnCode {
        /**
         * 成功
         */
        public static final String SUCCESS = "0000";
    }
    
    /**
     * 出款状态
     * @author mgt
     * @date 2016年8月31日
     */
    public static final class WithdrawState {
        /**
         * 处理中
         */
        public static final String HANDLING = "0";
        /**
         * 成功
         */
        public static final String SUCCESS = "1";
        /**
         * 失败
         */
        public static final String FAILURE = "-1";
        /**
         * 退款
         */
        public static final String REFUND = "2";
    }
    
    @Override
    public PartnerResult<WithdrawCallbackData> withdrawSingle(String orderId, PartnerWithdrawData data) {
        logger.info("进入宝付单笔打款");

        orderId = config.getMchtNo() + orderId;
        
        logger.info("加上宝付前缀的订单号：" + orderId);
        
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
        
        Map<String, String> params = new HashMap<>();
        
        String pub_key = config.getPublicKeyFile();
        
        try {
            
            TransContent<TransReqBF0040001> transContent = new TransContent<TransReqBF0040001>(DATA_TYPE);
            
            //封装请求信息
            List<TransReqBF0040001> list = new ArrayList<TransReqBF0040001>();

            TransReqBF0040001 transReqData = new TransReqBF0040001();
            list.add(transReqData);
            
            transReqData.setTrans_no(orderId);
            transReqData.setTrans_money(data.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            transReqData.setTo_acc_name(data.getUserName());
            transReqData.setTo_acc_no(data.getBankAccount());
            transReqData.setTrans_card_id(data.getIdentityCard());
            
            String bankName = this.getChannelBankId(data.getBankId());
            
            transReqData.setTo_bank_name(bankName);
            
            //如果是对公
            if(PartnerWithdrawData.ACCOUNT_TYPE_PUBLIC.equals(data.getAccountType())) {
                SProvince sProvince = CityData.PROVINCE_MAP.get(data.getBankProvinceId());
                SCity sCity = CityData.CITY_MAP.get(data.getBankCityId());
                if(null == sProvince || null == sCity) {
                    throw new ServiceException("找不到对应的省份或城市信息");
                }
                transReqData.setTo_pro_name(sProvince.getProvinceName());
                transReqData.setTo_city_name(sCity.getCityName());
                /*transReqData.setTo_pro_name("广东省");
                transReqData.setTo_city_name("广州市");*/
                transReqData.setTo_acc_dept(data.getBankBranchName());
            } else {
                transReqData.setTo_pro_name("");
                transReqData.setTo_city_name("");
                transReqData.setTo_acc_dept("");
            }
            
            transContent.setTrans_reqDatas(list);
            
            String contentString = transContent.obj2Str(transContent);
            
            logger.info("xmlContent:{}", desEncoder.encode(contentString));

            String keyStorePath = config.getPrivateKeyFile();
            String keyStorePassword = config.getPwd();
            
            /**
             * 加密规则：项目编码UTF-8 
             * 第一步：BASE64 加密
             * 第二步：商户私钥加密
             */
            String contentBase64 =  new String(Base64.encodeBase64(contentString.getBytes()));//Base64.encode(origData);
            
            logger.info("contentBase64:{}", contentBase64);
            
            String encryptData = RsaCodingUtil.encryptByPriPfxFile(contentBase64,
                            keyStorePath, keyStorePassword);
            
            logger.info("encryptData:{}", encryptData);
            
            params.put("member_id", config.getMchtNo());
            params.put("terminal_id", config.getTerminalId());
            params.put("data_type", DATA_TYPE);
            params.put("data_content", encryptData);// 加密后数据
            params.put("version", "4.0.0");
            
        } catch (Exception e1) {
            logger.error("发送请求前异常", e1);
            backData.setWithdrawStatus(WithdrawStatus.FAILURE);
            result.setCode(PartnerResultCode.FAILURE);
            result.setMessage(e1.getMessage());
            logger.info("异常，退出宝付单笔打款...");
            logger.info("result:{}", result);
            return result;
        }
        
        result.setCode(PartnerResultCode.SUCCESS);
        result.setMessage("请求成功");
        
        try {
            logger.info("请求报文: {}",  params.toString());
            String backStr = HttpUtils.doPost(config.getRequestUril(), params);
            logger.info("返回报文：{}", backStr);
            
            if(StringUtils.isBlank(backStr)) {
                throw new  ServiceException("宝付代付返回内容为空");
            }
            
            //解析返回结果
            
            TransContent<TransRespBF0040001> respObj = new TransContent<TransRespBF0040001>(DATA_TYPE);

            //明文返回处理可能是报文头参数不正确、或其他的异常导致；
            if(backStr.contains("trans_content")) {
                respObj = (TransContent<TransRespBF0040001>) respObj.str2Obj(backStr, TransRespBF0040001.class);
                if(ReturnCode.SUCCESS.equals(respObj.getTrans_head().getReturn_code())) {
                    throw new  ServiceException("查询失败：返回数据没有加密，但是不是失败状态");
                }
            } else { //密文返回
                
                //第一步：公钥解密
                backStr = RsaCodingUtil.decryptByPubCerFile(backStr, pub_key);
                
                logger.info("backStr decode:{}", backStr);
                
                //第二步BASE64解密
                backStr = new String(Base64.decodeBase64(backStr));
                
                logger.info("base64 decode:{}", desEncoder.encode(backStr));
                
                respObj = (TransContent<TransRespBF0040001>) respObj.str2Obj(backStr, TransRespBF0040001.class);
            }
            
            if(respObj == null || respObj.getTrans_head() == null) {
                throw new  ServiceException("宝付代付解析返回内容失败");
            }
            
            if(respObj.getTrans_reqDatas() != null && respObj.getTrans_reqDatas().size() > 0) {
                
                if(respObj.getTrans_reqDatas().size() != 1) {
                    throw new  ServiceException("返回记录条数不等于1");
                }
                
                TransRespBF0040001 transRespBF0040001 = respObj.getTrans_reqDatas().get(0);
                //回调的参数存入请求数据库，敏感信息进行加密
                String bankAccount = desEncoder.encode(transRespBF0040001.getTo_acc_no());
                transRespBF0040001.setTo_acc_no(bankAccount);
                //用户名
                String userName = desEncoder.encode(transRespBF0040001.getTo_acc_name());
                transRespBF0040001.setTo_acc_name(userName);
            }

            backData.setResultStr(JSON.toJSONString(respObj));
            
            logger.info("订单号：{}， returnCode：{}, returnMsg:{}, JSON:{}", orderId, respObj.getTrans_head().getReturn_code(), respObj.getTrans_head().getReturn_msg(), JSON.toJSONString(respObj));
            
            //5.封装返回结果
            if(ReturnCode.SUCCESS.equals(respObj.getTrans_head().getReturn_code())) { //交易请求成功
                logger.info("请求已经接收， orderId:{}", orderId);
                backData.setWithdrawStatus(WithdrawStatus.APPLYING);
            } else { 
                logger.info("请求失败， orderId:{}", orderId);
                backData.setWithdrawStatus(WithdrawStatus.FAILURE);
            } 
            backData.setMessage(respObj.getTrans_head().getReturn_code() + ":" + respObj.getTrans_head().getReturn_msg());
            logger.info("交易完成, 退出宝付单笔打款...");
            logger.info("result:{}", result);
            return result;
        } catch (Exception e) {
            logger.error("发送请求后发生异常", e);
            backData.setWithdrawStatus(WithdrawStatus.APPLYING);  //异常也定义为申请中
            backData.setMessage(e.getMessage());
            logger.info("异常，退出宝付单笔打款...");
            logger.info("result:{}", result);
            return result;
        }
    }

    @Override
    public PartnerResult<WithdrawCallbackData> withdrawCallback(String backStr) {
        
        logger.info("宝付单款异步回调开始...");
        
        logger.info("返回字符串， {}", backStr);

        if(StringUtils.isBlank(backStr)) {
            logger.error("宝付异步回调失败， 返回参数为空");
            throw new ServiceException("异步回调失败：返回内容为空");
        }
        
        //截取数据
        Map<String, String> params = getParams(backStr);
        
        //判断商户号是是否正确
        if(!config.getMchtNo().equals(params.get("member_id")) || !config.getTerminalId().equals(params.get("terminal_id"))) {
            logger.error("宝付异步回调失败，商户号不对应, member_id:{}, terminal_id:{}", params.get("member_id"), params.get("terminal_id"));
            throw new ServiceException("异步回调失败：商户号不对应");
        }
        
        String contentStr = params.get("data_content");
        
        if(StringUtils.isBlank(contentStr)) {
            logger.error("宝付异步回调失败， 返回业务数据为空");
            throw new ServiceException("异步回调失败：返回业务数据为空");
        }
        
        
        TransContent<TransRespBF0040002> respObj = new TransContent<TransRespBF0040002>(DATA_TYPE);
        
        String pub_key = config.getPublicKeyFile();
        contentStr = RsaCodingUtil.decryptByPubCerFile(contentStr, pub_key);
        logger.info("contentStr decode:{}", contentStr);
        contentStr = new String(new Base64().decode(contentStr));
        logger.info("contentStr decodeBase64:{}", desEncoder.encode(contentStr));
        respObj = (TransContent<TransRespBF0040002>) respObj.str2Obj(contentStr,TransRespBF0040002.class);
        
        if(respObj == null) {
            logger.error("宝付异步回调失败， 解析对象失败");
            throw new ServiceException("异步回调失败：解析返回内容失败");
        }
        
        if(respObj.getTrans_reqDatas() == null || respObj.getTrans_reqDatas().size() != 1) {
            logger.error("宝付异步回调失败，返回订单总数不为1条");
            throw new ServiceException("异步回调失败：返回订单总数不为1条");
        }
        
        TransRespBF0040002 transRespBF0040002 = respObj.getTrans_reqDatas().get(0);
            
        //4.封装参数返回
        WithdrawCallbackData backData = new WithdrawCallbackData();
        //计算手续费(固定每笔2元)
        backData.setFee(feeCalculate(BigDecimal.ZERO));
        backData.setCallbackTime(new Date());
        backData.setRequestUrl(config.getRequestUril());
        
        //回调的参数存入请求数据库，敏感信息进行加密
        String bankAccount = desEncoder.encode(transRespBF0040002.getTo_acc_no());
        transRespBF0040002.setTo_acc_no(bankAccount);
        //用户名
        String userName = desEncoder.encode(transRespBF0040002.getTo_acc_name());
        transRespBF0040002.setTo_acc_name(userName);
        
        logger.info("respObj：{}", JSON.toJSONString(respObj));

        backData.setCallbackStr(JSON.toJSONString(respObj));
        
        String orderId = StringUtils.trimToEmpty(transRespBF0040002.getTrans_no()).replaceAll(config.getMchtNo(), "");
        logger.info("去除宝付商户号的订单号:{}", orderId);
        backData.setOrderId(orderId);
        backData.setMessage(transRespBF0040002.getTrans_remark());
        backData.setChannelOrderId(transRespBF0040002.getTrans_no());
        
        backData.setResponseStr("ok");
        backData.setAmount(new BigDecimal(transRespBF0040002.getTrans_money()));
        
        if(WithdrawState.FAILURE.equals(transRespBF0040002.getState()) || WithdrawState.REFUND.equals(transRespBF0040002.getState())) { //失败或退款
            logger.error("orderId:{}, 支付失败", orderId);
            backData.setWithdrawStatus(WithdrawStatus.FAILURE);
        } else if(WithdrawState.SUCCESS.equals(transRespBF0040002.getState())) {
            logger.error("orderId:{}, 支付成功", orderId);
            backData.setWithdrawStatus(WithdrawStatus.SUCCESS);
        } else { //未知
            logger.error("orderId:{}, 未知的支付状态：{}", orderId, transRespBF0040002.getState());
            throw new ServiceException("异步回调失败, 未知的交易状态:" + transRespBF0040002.getState());
        }
        
        logger.info("回调处理完成...");

        PartnerResult<WithdrawCallbackData> result = new PartnerResult<WithdrawCallbackData>(PartnerResultCode.SUCCESS, backData, "回调处理成功");
        logger.info("result:{}", result);

        return result;
    }

    @Override
    public PartnerResult<WithdrawCallbackData> withdrawSingleQuery(
            String orderId) {
        
        logger.info("宝付单笔打款状态查询开始...");

        logger.info("请求参数：orderId = {}", orderId);
        
        orderId = config.getMchtNo() + orderId;
        
        logger.info("加上宝付前缀的订单号：" + orderId);
        
        //1.封装请求信息
        TransContent<TransReqBF0040002> transContent = new TransContent<TransReqBF0040002>(DATA_TYPE);
        
        List<TransReqBF0040002> trans_reqDatas = new ArrayList<TransReqBF0040002>();
        transContent.setTrans_reqDatas(trans_reqDatas);
        
        TransReqBF0040002 transReqData = new TransReqBF0040002();
        transReqData.setTrans_batchid("");
        transReqData.setTrans_no(orderId);
        trans_reqDatas.add(transReqData);
        
        
        String bean2XmlString = transContent.obj2Str(transContent);
        
        logger.info("orderOd:{}, 请求报文:{}", orderId, bean2XmlString);
        
        String keyStorePath = config.getPrivateKeyFile();
        String keyStorePassword = config.getPwd();
        String pub_key = config.getPublicKeyFile();
        String origData = bean2XmlString;

        /**
         * 加密规则：项目编码UTF-8 
         * 第一步：BASE64 加密
         * 第二步：商户私钥加密
         */
        origData =  new String(Base64.encodeBase64(origData.getBytes()));//Base64.encode(origData);
        String encryptData = RsaCodingUtil.encryptByPriPfxFile(origData,
                        keyStorePath, keyStorePassword);
        
        Map<String, String> params = new HashMap<>();
        params.put("member_id", config.getMchtNo());
        params.put("terminal_id", config.getTerminalId());
        params.put("data_type", DATA_TYPE);
        params.put("data_content", encryptData);
        params.put("version", "4.0.0");
        
        logger.info("请求报文: {}", params);

        //3.发送请求
        logger.info("发送请求...");
        String backStr = HttpUtils.doPost(config.getQueryRequestUril(), params);
        logger.info("返回报文：{}", backStr);
        
        if(StringUtils.isBlank(backStr)) {
            return new PartnerResult<>(PartnerResultCode.FAILURE, null, "查询失败：返回内容为空");
        }
        
        TransContent<TransRespBF0040002> respObj = new TransContent<TransRespBF0040002>(DATA_TYPE);
        
        //4.解析返回结果
        if (backStr.contains("trans_content")) {//报文错误处理
            respObj = (TransContent<TransRespBF0040002>) respObj.str2Obj(backStr,TransRespBF0040002.class);
            if(ReturnCode.SUCCESS.equals(respObj.getTrans_head().getReturn_code())) {
                return new PartnerResult<>(PartnerResultCode.FAILURE, null, "查询失败：返回数据没有加密，但是不是失败状态");
            }
        } else {
            backStr = RsaCodingUtil.decryptByPubCerFile(backStr, pub_key);
            logger.info("backStr decode:{}", backStr);
            backStr = new String(new Base64().decode(backStr));
            logger.info("backStr decodeBase64:{}", desEncoder.encode(backStr));
            respObj = (TransContent<TransRespBF0040002>) respObj.str2Obj(backStr,TransRespBF0040002.class);
        }
        
        if(respObj == null || respObj.getTrans_head() == null) {
            return new PartnerResult<>(PartnerResultCode.FAILURE, null, "查询失败：解析返回内容失败");
        }
        
        logger.info("orderId:{}, return_code:{}, respObj：{}", orderId, respObj.getTrans_head().getReturn_code(), desEncoder.encode(JSON.toJSONString(respObj)));
        
        //5.封装返回结果
        if(!ReturnCode.SUCCESS.equals(respObj.getTrans_head().getReturn_code())) { //查询失败
            return new PartnerResult<>(PartnerResultCode.FAILURE, null, "查询失败：" + respObj.getTrans_head().getReturn_msg());
        }
        
        if(respObj.getTrans_reqDatas() == null || respObj.getTrans_reqDatas().size() != 1) {
            return new PartnerResult<>(PartnerResultCode.FAILURE, null, "查询失败：返回记录数不为1");
        }
        
        TransRespBF0040002 transRespBF0040002 = respObj.getTrans_reqDatas().get(0);
            
        /*PartnerResult<WithdrawSingleQueryData> result = new PartnerResult<>();
         * WithdrawSingleQueryData queryData = new WithdrawSingleQueryData();
        
        result.setObject(queryData);
        result.setCode(PartnerResultCode.SUCCESS);
        result.setMessage("查询成功");

        queryData.setOrderId(orderId);
        queryData.setMessage(transRespBF0040002.getTrans_remark());
        
        logger.info("orderId:{}, state:{}", orderId, transRespBF0040002.getState());
        
        if(WithdrawState.FAILURE.equals(transRespBF0040002.getState()) || WithdrawState.REFUND.equals(transRespBF0040002.getState())) { //失败或退款
            queryData.setWithdrawStatus(WithdrawStatus.FAILURE);
            return result;
        } else if(WithdrawState.HANDLING.equals(transRespBF0040002.getState())) {
            queryData.setWithdrawStatus(WithdrawStatus.APPLYING);
        } else if(WithdrawState.SUCCESS.equals(transRespBF0040002.getState())) {
            queryData.setWithdrawStatus(WithdrawStatus.SUCCESS);
        } else { //未知
            return new PartnerResult<>(PartnerResultCode.FAILURE, null, "查询失败, 未知的交易状态:" + transRespBF0040002.getState());
        }
        
        return result;*/
        
        //4.封装参数返回
        WithdrawCallbackData backData = new WithdrawCallbackData();
        //计算手续费(固定每笔2元)
        backData.setFee(feeCalculate(BigDecimal.ZERO));
        backData.setCallbackTime(new Date());
        backData.setRequestUrl(config.getRequestUril());
        
        //回调的参数存入请求数据库，敏感信息进行加密
        String bankAccount = desEncoder.encode(transRespBF0040002.getTo_acc_no());
        transRespBF0040002.setTo_acc_no(bankAccount);
        //用户名
        String userName = desEncoder.encode(transRespBF0040002.getTo_acc_name());
        transRespBF0040002.setTo_acc_name(userName);
        
        logger.info("respObj：{}", JSON.toJSONString(respObj));

        backData.setCallbackStr(JSON.toJSONString(respObj));
        
        backData.setOrderId(orderId.replaceAll(config.getMchtNo(), ""));
        backData.setMessage(transRespBF0040002.getTrans_remark());
        backData.setChannelOrderId(transRespBF0040002.getTrans_no());
        
        backData.setResponseStr("ok");
        backData.setAmount(new BigDecimal(transRespBF0040002.getTrans_money()));
        
        if(WithdrawState.FAILURE.equals(transRespBF0040002.getState()) || WithdrawState.REFUND.equals(transRespBF0040002.getState())) { //失败或退款
            logger.error("orderId:{}, 支付失败", orderId);
            backData.setWithdrawStatus(WithdrawStatus.FAILURE);
        } else if(WithdrawState.SUCCESS.equals(transRespBF0040002.getState())) {
            logger.error("orderId:{}, 支付成功", orderId);
            backData.setWithdrawStatus(WithdrawStatus.SUCCESS);
        } else if(WithdrawState.HANDLING.equals(transRespBF0040002.getState())) {
            logger.error("orderId:{}, 支付处理中", orderId);
            backData.setWithdrawStatus(WithdrawStatus.APPLYING);
        } else { //未知
            logger.error("orderId:{}, 未知的支付状态：{}", orderId, transRespBF0040002.getState());
            throw new ServiceException("异步回调失败, 未知的交易状态:" + transRespBF0040002.getState());
        }
        
        logger.info("回调处理完成...");

        PartnerResult<WithdrawCallbackData> result = new PartnerResult<WithdrawCallbackData>(PartnerResultCode.SUCCESS, backData, "查询成功");
        logger.info("result:{}", result);

        return result;
    }
    
    @Override
    public WithdrawChannel getChannelId() {
        return WithdrawChannel.BAOFOO_WITHDRAW;
    }
    
    @Override
    public BigDecimal feeCalculate(BigDecimal amount) {
        return new BigDecimal(config.getFixFee());
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

}
