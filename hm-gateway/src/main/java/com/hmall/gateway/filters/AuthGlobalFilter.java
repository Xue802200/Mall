package com.hmall.gateway.filters;

import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AuthGlobalFilter implements GlobalFilter {

    private final AuthProperties authProperties;
    private final JwtTool jwtTool;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 进行用户是否登录的判断
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取到request对象
        ServerHttpRequest request = exchange.getRequest();

        //2.判断当前请求是否需要被拦截
        String path = request.getURI().getPath();
        if(isExclude(path)){
            //放行
            return chain.filter(exchange);
        }

        //3.从request中获取token
        String token = null;
        List<String> authorization = request.getHeaders().get("authorization");
        if(authorization != null && !authorization.isEmpty()){
            token = authorization.get(0);
        }

        //3.解析token,获取userId
        Long userId = null;
        try{
            userId = jwtTool.parseToken(token);
        }catch (Exception e){
            //自定义响应状态码
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);

            //过滤器到这全部执行完毕，剩下的不执行
            return response.setComplete();
        }

        //4. 将userid保存到上下文当中
        String userInfo = userId.toString();
        ServerWebExchange build = exchange.mutate()
                .request(builder -> builder.header("user-info", userInfo))
                .build();

        //5.放行
        return chain.filter(build);
    }

    /**
     * 判断当前路径是否被拦截
     * 未被拦截返回true
     */
    private boolean isExclude(String path) {
        //1.获取未被拦截的请求
        List<String> excludePaths = authProperties.getExcludePaths();

        //2.判断是否被拦截
        for(String excludePath : excludePaths){
            if(antPathMatcher.match(excludePath, path)){
                //未被拦截
                return true;
            }
        }

        //被拦截了
        return false;
    }
}
