package com.chengxi.tool.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "filemanager")
@Component
public class GlobalFileManagerConfig {
    @Value("${filemanager.fileRootPath}")
    private String fileRootPath;

    @Value("${filemanager.urlRootPath}")
    private String urlRootPath;
}
