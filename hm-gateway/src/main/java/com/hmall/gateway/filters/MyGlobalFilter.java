package com.hmall.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class MyGlobalFilter implements GlobalFilter , Ordered {

    //对于JWT令牌进行用户登录状态的判定
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取request
        ServerHttpRequest request = exchange.getRequest();
        //获取header
        HttpHeaders headers = request.getHeaders();

        System.out.println(headers);

        //将当前获取到的值保存到上下文当中
        return chain.filter(exchange);
    }

    //过滤器执行顺序，数字越小执行顺序越靠前
    @Override
    public int getOrder() {
        return 0;
    }
}
