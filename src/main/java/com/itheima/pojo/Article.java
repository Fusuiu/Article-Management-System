package com.itheima.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itheima.anno.State;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
@Data
public class Article {
    @NotNull(groups = Update.class)
    private Integer id;//主键ID
    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String title;//文章标题
    @NotEmpty
    private String content;//文章内容
    @NotEmpty
    @URL
    private String coverImg;//封面图像
    @State
    private String state;//发布状态 已发布|草稿
    @NotNull
    //为什么不用@NotEmpty？因为categoryId是Integer类型（整数），而@NotEmpty适用于有 “长度” 概念的类型（如字符串、集合），整数类型只需校验非null即可。
    private Integer categoryId;//文章分类id
    @JsonIgnore//让springmvc把当前对象转换成json字符串响应的时候，忽略,最终的json字符串中就没有这个属性了
    private Integer createUser;//创建人ID
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
    //继承默认分组，默认分组具有所有未指定分组的校验
    public interface Add extends Default {

    }
    public interface Update extends Default{

    }

}
