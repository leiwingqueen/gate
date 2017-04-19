package com.elend.gate.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * 
 * @author liyongquan
 *
 */
public class LogHelper {
    /**
     * 初始化日志配置
     * @param file
     */
    public static void initLog4j(String file) {
        try {
            URL log4jFile = ResourceUtils.getURL(file);
            URL logURL = new ClassPathResource(file).getURL();
            ILoggerFactory loggerFactory =  LoggerFactory.getILoggerFactory();
            LoggerContext loggerContext = (LoggerContext) loggerFactory;
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(loggerContext);
            configurator.doConfigure(logURL);
            System.out.println("Change log4j configuration to: " + log4jFile);
        } catch (JoranException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }
}
