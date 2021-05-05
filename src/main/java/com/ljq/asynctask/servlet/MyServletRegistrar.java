package com.ljq.asynctask.servlet;

import com.ljq.asynctask.constant.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * @author linjunqiang
 * @Date 2021/5/5
 * 注册自定义Servlet
 */
public class MyServletRegistrar implements ServletContextInitializer {
    private static final Logger log = LoggerFactory.getLogger(MyServletRegistrar.class);


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        this.register(servletContext);
    }

    private void register(ServletContext servletContext) {
        String urlMapping = CommonConstant.URI + "/*";
        ServletRegistration.Dynamic registration = servletContext.addServlet(CommonConstant.URI, new MyServlet());
        registration.addMapping(urlMapping);
        log.info("registered to {} ", CommonConstant.URI);
    }
}
