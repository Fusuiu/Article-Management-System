package com.itheima.service;

import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;
import org.apache.ibatis.annotations.Select;

public interface ArticleService {
    void add(Article article);

    //条件分页列表查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);


    Article detail(Integer id);

    void update(Article article);

    void delete(Integer id);
}
