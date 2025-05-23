package com.hmall.api.config;

import com.hmall.common.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {
    //日志级别的配置
    @Bean
    public Logger.Level feignLogLevel(){
        return Logger.Level.FULL;
    }

    //为请求配置请求头
    @Bean
    public RequestInterceptor requestInterceptor(){
        return (requestTemplate) -> {
            Long user = UserContext.getUser();
            if(user != null){
                requestTemplate.header("user-info",user.toString());
            }
        };
    }
}
