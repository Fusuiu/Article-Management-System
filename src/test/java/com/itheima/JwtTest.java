package com.itheima;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    @Test
    public void testGen(){
        //创建装用户信息和MAP
        Map<String,Object> claims = new HashMap<>();
        claims.put("id",1);
        claims.put("username","张三");
        //生成JWT的代码
        String token = JWT.create()
                .withClaim("user",claims)//添加载荷
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*12))//添加过期时间
                //配置头部签名算法（指定算法）：Algorithm.HMAC256，
                //配置签名的密钥：itheima
                .sign(Algorithm.HMAC256("itheima"));
        System.out.println(token);
    }
    @Test
    public void testParse(){
        //模拟用户传过来的字符串
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6IuW8oOS4iSJ9LCJleHAiOjE3NTQ2MDMyMzd9" +
                ".v7QjvqJuRCfiwAYfHrf7mOY5vNUbJeX_UVYAUhs61hg";
        //验证1：使用密钥解码，创建验证器
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("itheima")).build();
        //验证2：验证token（头部和载荷部分的数据），生成一个解析后的JWT对象
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        //获取JWT对象中的所有claims用户信息
        Map<String, Claim> claims = decodedJWT.getClaims();
        //看能不能成功输出名为user的claims
        System.out.println(claims.get("user"));

        //如果用户提供的JWT字符串令牌的头部和载荷部分的数据被篡改，那么验证失败
        //如果密钥不对，验证失败
        //token过期，验证失败

    }
}
