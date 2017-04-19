package com.elend.gate.conf.data;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elend.gate.conf.facade.PlatformConfigInfoFacade;
import com.elend.gate.conf.facade.vo.SCity;
import com.elend.gate.conf.facade.vo.SProvince;
import com.elend.p2p.Result;
import com.elend.p2p.spring.SpringContextUtil;

/**
 * 定时reload基础数据
 * @author mgt
 *
 */
public class CityData {

    private static Logger logger = LoggerFactory.getLogger(CityData.class);
    
    /**
     * 所有省份的map  
     * key 省份代码
     * val SProvince
     * */
    public static Map<String, SProvince> PROVINCE_MAP = new HashMap<>();
    
    /**
     * 所有城市的map
     * key 城市代码
     * val SCity
     */
    public static Map<String, SCity> CITY_MAP = new HashMap<String, SCity>();
    
    private static Boolean DATAINIT = true;
    private static Boolean STOP_RELOAD = false;
    
    public static void stopReloadCity() {
        STOP_RELOAD = true;
    }
    
    public static void init() {
        
        /*
         * linshumao 城市信息只初始化一次
         * 除每次启动时reload之外,每天3点reload两次
         */
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int second = calendar.get(Calendar.SECOND);
        if(hour == 3 && second >= 0 && second < 100){
            DATAINIT = true;
        }

        
        if (!STOP_RELOAD && DATAINIT) {
            PlatformConfigInfoFacade facade = (PlatformConfigInfoFacade)SpringContextUtil.getBean("platformConfigInfoFacade");
            
            //重载省份
            Result<List<SProvince>> proResult =  facade.provinceList();
            if(proResult.isSuccess()) {
                Map<String, SProvince> provinceMap = new HashMap<String, SProvince>();
                for(SProvince p : proResult.getObject()) {
                    provinceMap.put(p.getProvinceId() + "", p);
                }
                PROVINCE_MAP = provinceMap;
            }
            
            //重载城市
            if (proResult.isSuccess()) {
                Map<String, SCity> cityMap = new HashMap<String, SCity>();
                for (SProvince sProvince : proResult.getObject()) {
                    Result<List<SCity>> cityResult =  facade.cityList(sProvince.getProvinceId());
                    if(cityResult.isSuccess()) {
                        for(SCity c : cityResult.getObject()) {
                            cityMap.put(c.getCityId() + "", c);
                        }
                    }
                }
                CITY_MAP = cityMap;
            }
            
            logger.info("reload city data success");
        }
        
        if (DATAINIT) {
            DATAINIT = false;
        }
        
    }
}
