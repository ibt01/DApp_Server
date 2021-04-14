package com.chengxi.cache.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="redis")
public class RedisConfiguration {

    public String host;

    public int port;

    public String password;

    public int database;

    public int timeout;
}