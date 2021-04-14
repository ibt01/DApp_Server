package com.chengxi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Slf4j
@EnableTransactionManagement
@SpringBootApplication
public class PersonalApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(PersonalApplication.class, args);
    }
//tcp/ip
    @Bean
    protected ServletContextListener listener() {
        return new ServletContextListener() {
            @Override
            public void contextInitialized(ServletContextEvent sce) {
            }

            @Override
            public void contextDestroyed(ServletContextEvent sce) {
            }

        };
    }
}
