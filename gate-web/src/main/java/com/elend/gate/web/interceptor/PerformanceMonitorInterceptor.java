package com.elend.gate.web.interceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elend.p2p.monitor.performance.PerformanceUtil;

/**
 * 系统请求性能监控
 * @author lxy
 *
 */
public class PerformanceMonitorInterceptor implements MethodInterceptor {

        private final static Logger log = LoggerFactory.getLogger(PerformanceMonitorInterceptor.class);

	private static boolean isEnableMonitor = false;

	public PerformanceMonitorInterceptor() {
		isEnableMonitor = true;
	}

	public static boolean isEnableMonitor() {
		return isEnableMonitor;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
	               StopWatch clock = new StopWatch();
	                 clock.start(); // 计时开始  
		Object result = invocation.proceed();
		clock.stop(); // 计时结束  
		Object target = invocation.getThis();

		String className = PerformanceUtil.getLongClassName(target);
		String methodName = PerformanceUtil.getLongMethodName(className, method.getName());
		if (clock.getTime()> 500) {
		    log.info("invok:"+methodName+",time="+clock.getTime()+"ms");
		}

		// if (className.endsWith("Controller")) {
		// // 线程可能会重用，需要清除之前的数据
		// PerformanceStackTraceConfig.removeThreadName();
		// }

		return result;
	}

}
