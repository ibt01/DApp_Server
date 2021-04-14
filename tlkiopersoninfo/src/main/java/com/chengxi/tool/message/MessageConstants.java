package com.chengxi.tool.message;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="message")
public class MessageConstants {
    private String accessKeyId;
    private String accessKeySecret;
    private String connectTimeout;
    private String readTimeout;
    private String signName;
    private String templateCode;
    private Integer size=4;
}
