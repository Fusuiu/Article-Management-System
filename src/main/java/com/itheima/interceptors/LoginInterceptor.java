package com.itheima.interceptors;

import com.itheima.pojo.Result;
import com.itheima.utils.JwtUtil;
import com.itheima.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

//作为bean放入容器
@Component
//创建拦截器，设置拦截器内容
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        //获取token
        String token = request.getHeader("Authorization");
        //验证token
        try {
            //从redis中获取相同的token
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            String redisToken = operations.get(token);
            if (redisToken==null){
                //token已经失效了
                throw new RuntimeException();
            }
            //验证token通过，并得到了业务数据
            Map<String, Object> claims = JwtUtil.parseToken(token);
            //把业务数据存储到ThreadLocalUtil中的ThreadLocal对象中
            ThreadLocalUtil.set(claims);
            //拦截器放行该请求
            return true;
        }catch (Exception e){//把错误处理在这里了没有被全局异常处理器捉住
            //未通过验证
            //用户未登录的http响应状态码为401
            response.setStatus(401);
            //拦截器不放行该请求
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清除ThreadLocal对象中的数据
        ThreadLocalUtil.remove();
    }
}
