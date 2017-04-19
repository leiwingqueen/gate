package com.elend.gate.order.vo;

import com.elend.gate.order.model.PPayAgentApplyRequestPO;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PPayAgentApplyRequestVO extends PPayAgentApplyRequestPO {

    private static final long serialVersionUID = 1L;

    private DesPropertiesEncoder encoder = new DesPropertiesEncoder();

    public PPayAgentApplyRequestVO(PPayAgentApplyRequestPO po) {
        this.id = po.getId();
        this.orderId = po.getOrderId();
        this.partnerId = po.getPartnerId();
        this.partnerOrderId = po.getPartnerOrderId();
        this.channel = po.getChannel();
        this.channelOrderId = po.getChannelOrderId();
        this.bankId = po.getBankId();
        if (StringUtils.isNotBlank(po.getCardNo())) {
            this.cardNo = encoder.decode(po.getCardNo());
        }
        if (StringUtils.isNotBlank(po.getRealName())) {
            this.realName = encoder.decode(po.getRealName());
        }
        if (StringUtils.isNotBlank(po.getIdNo())) {
            this.idNo = encoder.decode(po.getIdNo());
        }
        if (StringUtils.isNotBlank(po.getCellphone())) {
            this.cellphone = encoder.decode(po.getCellphone());
        }
        this.amount = po.getAmount();
        this.merOrderId = po.getMerOrderId();
        this.custId = po.getCustId();
        this.phoneToken = po.getPhoneToken();
        this.requestUrl = po.getRequestUrl();
        this.param = po.getParam();
        this.result = po.getResult();
        this.ip = po.getIp();
        this.status = po.getStatus();
        this.createTime = po.getCreateTime();
        this.updateTime = po.getUpdateTime();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
