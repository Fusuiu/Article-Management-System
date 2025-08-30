package com.itheima.exception;

import com.itheima.pojo.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
//用于定义全局异常处理器，是@ControllerAdvice的增强版（结合了@ResponseBody的功能）
@RestControllerAdvice
public class GlobalExceptionHandler {
    //指定当前方法用于处理哪种类型的异常
    @ExceptionHandler(Exception.class)
    //统一捕获和处理项目中抛出的异常，并返回标准化的错误响应
    public Result handleException(Exception e){
        e.printStackTrace();
        return Result.error(StringUtils.hasLength(e.getMessage())?e.getMessage():"操作失败");
    }

}
