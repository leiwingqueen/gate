/**
 * 
 */
package com.elend.gate.admin.servlet;

import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elend.gate.admin.schedule.ResourceDB;
import com.elend.p2p.spring.SpringContextUtil;

/**
 * @author Administrator
 *
 */
public class ResourceUrlServlet  extends HttpServlet{

    /**
     * 
     */
    private static final long serialVersionUID = 7799889906787051970L;
    private static Logger logger = LoggerFactory.getLogger(ResourceUrlServlet.class);
    @Override
    public void init() throws ServletException {
        DataSource  dataSource = (DataSource)SpringContextUtil.getBean("eucDataSource");
        try {
            new ResourceDB().run(dataSource.getConnection());
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
