package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.JwtUtil;
import com.itheima.utils.Md5Util;
import com.itheima.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//没有完全使用Restful，因为使用了完整的方法名作为请求路径
@RestController
//根据接口文档编写路径/user
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //根据接口文档编写路径/register
    @PostMapping("/register")
    //根据接口文档编写请求参数
    //使用@Pattern注解，用正则表达式添加规则
    public Result register(@Pattern(regexp = "^\\S{5,16}$")String username , @Pattern(regexp = "^\\S{5,16}$") String password){
        //查询用户
        User u =userService.findByUserName(username);
        //判断用户是否已经被注册
        if(u==null){
            //未被注册
            //返回操作成功信息
            userService.register(username,password);
            return Result.success();
        }else{
            //已被注册
            //返回提示信息
            return Result.error("用户名已被占用");
        }
    }
    @PostMapping("/login")
    //返回的Result.data的类型是字符串类型，所以标注了<String>
    //Spring MVC 中，对于 简单类型参数（如 String、int 等），如果参数名与请求中的参数名一致，默认会自动绑定，无需显式添加 @RequestParam 注解
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$")String username , @Pattern(regexp = "^\\S{5,16}$") String password){
        //根据用户名查询用户
        User loginUser = userService.findByUserName(username);
        //判断用户是否存在
        if(loginUser==null){
            return Result.error("用户名错误");
        }
        //判断密码是否正确,注册时存入的密码是加密过后的，所以我们要把获得的密码加密，在和数据库的密码进行比较
        if(Md5Util.getMD5String(password).equals(loginUser.getPassword())){
            //登陆成功
            //生成JWT令牌
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",loginUser.getId());
            claims.put("username",loginUser.getUsername());
            String token = JwtUtil.genToken(claims);
            //把token存储到redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,1, TimeUnit.HOURS);
            return Result.success(token);
        }
        return Result.error("密码错误");
    }
    @GetMapping("/userInfo")
    //返回的Result.data的是user对象，类型是User类型，所以标注了<User>
    public Result<User> userInfo(/*@RequestHeader(name = "Authorization") String token*/){
        //根据用户名查询用户
        /*//先使用token验证方法返回map集合再获取用户名
        Map<String,Object> map = JwtUtil.parseToken(token);
        //map.get("username")获取键值对中键名为“username”的 “值（Value）”,获取时为object类型强制转化为当时存储的类型：字符串
        String username = (String) map.get("username");*/

        //从ThreadLocalUtil中的ThreadLocal对象中获取集合
        Map<String,Object>  map= ThreadLocalUtil.get();
        //map.get("username")获取键值对中键名为“username”的 “值（Value）”,获取时为object类型强制转化为当时存储的类型：字符串
        String username = (String)map.get("username");
        User user =userService.findByUserName(username);
        return Result.success(user);
    }
    @PutMapping("/update")
    //请求了多个参数，在方法体中拿到后使用user对象封装获取
    public Result update(@RequestBody @Validated User user){
        userService.update(user);
        return Result.success();
    }
    //只修改一个头像字段
    @PatchMapping("/updateAvatar")
    //请求参数是格式是queryString的，参数在请求路径中，使用@RequestParam注解从请求中接受参数String avatarUrl
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    //从请求中拿出json数据，封装为map集合，因为json请求参数的键名名和user实体类中的属性名不一致
    public Result updatePwd(@RequestBody Map<String,String> params,@RequestHeader("Authorization") String token){
        //获取集合参数进行参数校验
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        //校验参数是否存在,长度是不是存在
        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            //有任意一个参数不存在就响应错误
            return Result.error("缺少必要参数");
        }

        //校验输入的原密码是否和数据库中的密码一致
        //获取用来查询原密码的username
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginUser = userService.findByUserName(username);
        //因为查询出来的密码是加密过的，所以我们把用户输入的密码也加密，再进行比对
        //比较字符串内容必须用 equals()，== 用于比较对象是否为同一个实例,字符串是引用类型哦
        if(!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码填写不正确");
        }

        //校验新密码和重复输入的新密码是否一致
        if(!rePwd.equals(newPwd)){
            return Result.error("两次填写的新密码不一致");
        }

        //调用方法实现密码更新
        userService.updatePwd(newPwd);
        //删除redis中对应的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }

}
