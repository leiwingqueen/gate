package com.elend.gate.sdk;

import java.math.BigDecimal;

import org.junit.Test;

import com.elend.gate.sdk.vo.PartnerChargeResponse;
import com.elend.gate.util.FormUtil;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.util.HttpUtils;
import com.elend.p2p.util.OrderIdHelper;

public class GateHelperTest {

    @Test
    public void testChargeRequest() {
        String priKey="69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
        RequestFormData chargeRequest = GateHelper.chargeRequest("P2P", OrderIdHelper._newOrderId("ll"), new BigDecimal("50000"), "308", "http://123456.com", "http://123456.com", 
                                 ChannelIdConstant.BAOFOO_GATE, priKey, "http://test.gate.gzdai.com/pay.jspx", "127.0.0.1", 100529, PayType.ENTERPRISE);
        String form=FormUtil.build(chargeRequest);
        System.out.println(form);
        
    }

    @Test
    public void testGetResponse() {
        String priKey="69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
        PartnerChargeResponse response=new PartnerChargeResponse();
        response.setAmount("100.00");
        response.setPartnerOrderId("150629160926040148260001");
        response.setPartnerId("P2P");
        response.setChannelOrderId("715214241215422I");
        response.setMessage("操作成功");
        response.setHmac("4aedc4ffc621f1cf66caf80c645c6f74");
        response.setChannelNoticeTime("20150629161148");
        response.setResultCode("000");
        response.setOrderId("15062916092604056040");
        response.setGateNoticeTime("20150629161227");
        response.setChannelPayTime("20150629161147");
        response.setIsNotify("false");
        //response.setBankId("");
        response.setChannelId("YEEPAY");
        GateHelper.getResponse(response, priKey);
    }
    
    @Test
    public void testWithdraw(){
        String partnerId = "P2P";
        String partnerOrderId = OrderIdHelper.newOrderId();
        String requestUrl = "http://test.gate.gzdai.com/withdraw.do";
        String notifyUrl = "http://test.api.pay.gzdai.com/gate/withdrawCallback.do";
        String priKey = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
        String ip = "127.0.0.1";
        String bankId = "307";
        String bankAccount = "6230580000025493748";
        String userName = "李泳权";
        String cityId = "5810";
        String provinceId = "20";
        String branchName = "广州大道支行";
        String identityCard = "442000198703066410";
        //String identityCard = "";
        int accountType = 2;
        BigDecimal amount = new BigDecimal("1");
        RequestFormData form = GateHelper.withdrawRequest(partnerId, 
                                                          partnerOrderId, 
                                                          amount,
                                                          notifyUrl,
                                                          WithdrawChannel.BAOFOO_WITHDRAW, 
                                                          priKey, 
                                                          requestUrl, 
                                                          ip, 
                                                          bankId, 
                                                          bankAccount, 
                                                          userName, 
                                                          cityId, 
                                                          provinceId, 
                                                          branchName, 
                                                          accountType,identityCard);
        String s = HttpUtils.doPost(form.getRequestUrl(), form.getParams());
        System.out.println("提现返回字符串："+ s);
    }
    
}
