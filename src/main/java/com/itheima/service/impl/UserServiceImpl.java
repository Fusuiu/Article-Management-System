package com.itheima.service.impl;

import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.Md5Util;
import com.itheima.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

//将此类作为业务bean放入容器中
@Service
public class UserServiceImpl implements UserService {
    //依赖注入
    @Autowired
    private UserMapper userMapper;
    //包装mapper查询方法：调用方法userMapper.findByUserName查询用户，返回用户对象
    @Override
    public User findByUserName(String username) {
        User u = userMapper.findByUserName(username);
        return u;
    }
    //包装mapper添加方法：调用方法userMapper.add添加用户
    @Override
    public void register(String username, String password) {
        //调用加密工具类的加密方法来加密密码
        String md5String = Md5Util.getMD5String(password);
        //添加用户，注意要使用加密过的密码
        userMapper.add(username,md5String);
    }

    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.updateAvatar(avatarUrl,id);
    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        //和注册一样需要加密密码，再更新存入数据库
        userMapper.updatePwd(Md5Util.getMD5String(newPwd),id);
    }
}
