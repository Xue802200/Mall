package com.hmall.common.interceptors;

import cn.hutool.core.util.StrUtil;
import com.hmall.common.utils.UserContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInfoInterceptor implements HandlerInterceptor {

    //拦截所有的请求，获取请求头中的userId
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取userId
        String userId = request.getHeader("user-info");

        //2.判断是否为null
        if(StrUtil.isNotBlank(userId)) {
            //不为null则添加数据
            UserContext.setUser(Long.valueOf(userId));
        }

        //3.放行
        return true;
    }

    //清理userId
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
       UserContext.removeUser();
    }
}
