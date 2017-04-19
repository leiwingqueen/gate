package com.elend.gate.conf.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elend.gate.conf.service.SBankPayLimitService;
import com.elend.gate.conf.vo.SBankPayLimitVO;
import com.elend.p2p.Result;
import com.elend.p2p.spring.SpringContextUtil;

/**
 * 银行限额数据
 * @author mgt
 *
 */
public class BankPayLimitData {

    private static Logger logger = LoggerFactory.getLogger(BankPayLimitData.class);
    
    /**
     * 缓存数据map
     * key: bankId_channel
     */
    public static Map<String, List<SBankPayLimitVO>> DATA_MAP = new LinkedHashMap<>();
    
    private static Boolean STOP_RELOAD = false;
    
    public static void stopReloadCity() {
        STOP_RELOAD = true;
    }
    
    public static void init() {
        
        if (!STOP_RELOAD) {
            SBankPayLimitService service = SpringContextUtil.getBean(SBankPayLimitService.class);
            
            //重载省份
            Result<List<SBankPayLimitVO>> result =  service.listAll();
            if(result.isSuccess()) {
                Map<String, List<SBankPayLimitVO>> bufMap = new LinkedHashMap<>();
                for(SBankPayLimitVO vo : result.getObject()) {
                    List<SBankPayLimitVO> list = bufMap.get(vo.getBankId() + "_" + vo.getChannel());
                    if(list == null) {
                        list = new ArrayList<>(3);
                    }
                    list.add(vo);
                    bufMap.put(vo.getBankId() + "_" + vo.getChannel(), list);
                }
                DATA_MAP = bufMap;
            }
            
            logger.info("reload bank_pay_limit data success");
        }
        
    }
    
    public static List<SBankPayLimitVO> getLimit(String bankId, String channel) {
        return DATA_MAP.get(bankId + "_" + channel);
    }
}
