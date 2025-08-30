package com.itheima.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Category {
    //指定了分组，这个校验只有Update.class组有
    @NotNull(groups = Update.class)
    private Integer id;//主键ID
    @NotEmpty//默认分组，所有的分组（Update.class和Add.class）都有这个校验
    private String categoryName;//分类名称
    @NotEmpty//默认分组，所有的分组（Update.class和Add.class）都有这个校验
    private String categoryAlias;//分类别名
    private Integer createUser;//创建人ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间

    //继承默认分组，默认分组具有所有未指定分组的校验
    public interface Add extends Default {

    }
    public interface Update extends Default{

    }
}
