package com.elend.gate.notify;

import org.springframework.stereotype.Component;

/**
 * 1-10         20秒
 * 11-20        10分钟
 * 21-30        20分钟
 * 31-          半小时
 * @author mgt
 *
 */
@Component("simpleNextQueryTimeStategy")
public class SimpleNextQueryTimeStategy implements NextQueryTimeStategy{
    private static final int POINT1 = 10;
    private static final int POINT2 = 20;
    private static final int POINT3 = 30;
    
    @Override
    public int getSeconds(int currentNum) {
        if(currentNum < POINT1) {
            return 20;
        } else if(currentNum >= POINT1 && currentNum < POINT2) {
            return 600;
        } else if(currentNum >= POINT2 && currentNum < POINT3) {
            return 1200;
        } else {
            return 1800;
        }
    }
}
