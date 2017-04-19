package com.elend.gate.reconcile.service;

import java.util.Date;

/**
 * 对账
 * @author mgt
 *
 */
public interface OrderCheckService {
    void check(Date begin, Date end);
}
