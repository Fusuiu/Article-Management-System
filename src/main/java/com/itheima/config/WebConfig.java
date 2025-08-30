package com.itheima.config;


import com.itheima.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//声明为配置类bean
@Configuration
//继承配置类
//注册拦截器并设置要拦截哪些请求（设置拦截对象）
public class WebConfig implements WebMvcConfigurer {
    //依赖注入拦截器
    @Autowired
    private LoginInterceptor loginInterceptor;
    //注册刚刚注入的拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截除了登录和注册以外的所有请求
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/login","/user/register");
    }
}
