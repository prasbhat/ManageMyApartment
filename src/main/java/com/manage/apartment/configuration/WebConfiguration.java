package com.manage.apartment.configuration;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WebConfiguration {
    @Bean
    public ServletRegistrationBean h2ServletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        registrationBean.setName("webServlet");
        return registrationBean;
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
}

