package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.ArticleMapper;
import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;
import com.itheima.service.ArticleService;
import com.itheima.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;
    @Override
    public void add(Article article) {
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        article.setCreateUser(id);
        articleMapper.add(article);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        //创建PageBean对象
        PageBean<Article> pb =new PageBean<>();

        //开启分页查询 使用pageHelper插件(需要先导入坐标依赖) 实现了把参数自动拼接到SQL语句后（相当于limit） 实现分页查询
        /*
        *   调用 PageHelper.startPage(2, 10); 后，PageHelper 会通过 MyBatis 的拦截器机制，对接下来执行的 select 语句进行拦截。
            自动在 SQL 末尾添加分页条件（例如 MySQL 中会变成 SELECT * FROM table LIMIT 10 OFFSET 10，即查询第 2 页，每页 10 条数据）。
            查询结果会被封装成 Page 对象（继承自 ArrayList），该对象不仅包含当前页的数据，还包含分页相关的元信息（总条数、总页数、当前页码等）。
        * */
        //PageHelper 会自动帮我们执行一条额外的 “总条数查询 SQL”。
        PageHelper.startPage(pageNum,pageSize);

        //调用mapper
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        List<Article> as =  articleMapper.list(userId,categoryId,state);
        //Page中提供了方法，可以获取PageHelper分页查询后 得到的总数据条数和当前页数据
        //因为PageHelper的工作原理是调用mapper层方法时使用sql语句时进行拦截再添加分页条件，而不是直接在PageHelper添加并使用SQL语句，所以我们要对as进行强转。
        //对as强转从而拿到被PageHelper封装成 Page 对象的查询结果
        Page<Article> p = (Page<Article>) as;

        //返回值类型是PageBean<Article>，（本质是因为接口文档中的data响应类型要含有“items”和“total”，我们使用了PageBean进行封装）
        // 所以把数据填充到一开始创建的PageBean对象中作为返回值
        //获取查询结果p中的所有记录和总条数填入到PageBean对象中
        pb.setItems(p.getResult());
        pb.setTotal(p.getTotal());

        return pb;
    }

    @Override
    public Article detail(Integer id) {
        // 可在此添加参数校验，如 id 非空判断
        if (id == null) {
            throw new IllegalArgumentException("主键 ID 不能为空");
        }
        Article article = articleMapper.detail(id);
        return article;
    }

    @Override
    public void update(Article article) {
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.update(article);
    }

    @Override
    public void delete(Integer id) {
     articleMapper.delete(id);
    }
}
