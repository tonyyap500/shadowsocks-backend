package com.shadowsocks.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pay.gaotong")
@Data
public class GaotongConfig {
    private String merchantNo;
    private String publicKey;
    private String callback;
}
