package com.hmall.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "hm.jwt")
public class JwtProperties {
    private Resource location; //密钥文件位置
    private String password;   //打开密钥的密码
    private String alias;      //密钥别名
    private Duration tokenTTL = Duration.ofMinutes(10);
}
