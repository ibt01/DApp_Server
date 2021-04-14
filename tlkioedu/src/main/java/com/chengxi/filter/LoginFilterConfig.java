package com.chengxi.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class LoginFilterConfig {
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(someFilter());
        //registration.addUrlPatterns("/v1.0/delphy/*");
        registration.setOrder(0);
        return registration;
    }

    public Filter someFilter() {
        return new LoginFilter();
    }
}
