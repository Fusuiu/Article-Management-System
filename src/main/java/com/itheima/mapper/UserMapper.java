package com.itheima.mapper;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

//使用注解，标记为数据库接口层让mybatis自动扫描,并作为bean加入容器，同时可以管理sql的xml映射文件
@Mapper
public interface UserMapper {
    //编写sql注解，#{}占位符中的是接口中的我们传入的字段username，where后的username是数据库中的字段
    @Select("select * from user where username=#{username}")
    User findByUserName(String username);
    //这里的形参是password，因为是别人调用它，不管传入的加密的还是不加密的在这里都是password
    //使用now()方法来添加时间字段
    @Insert("insert into user(username,password,create_time,update_time)"+
            "values(#{username},#{password},now(),now())"     )
    void add(String username, String password);

    @Update("update user set nickname=#{nickname},email=#{email},update_time=#{updateTime} where id =#{id}")
    void update(User user);

    @Update("update user set user_pic=#{avatarUrl},update_time=now() where id =#{id}")
    void updateAvatar(String avatarUrl,Integer id);

    @Update("update user set password=#{md5String},update_time=now() where id =#{id}")
    void updatePwd(String md5String, Integer id);
}
