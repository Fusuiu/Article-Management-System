package com.itheima.validation;

import com.itheima.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

//实现接口后，在参数中指定要对哪一个注解进行判断，判断对象的类型是什么
public class StateValidation implements ConstraintValidator<State,String> {
    /**
     *
     * @param s 将来要校验的数据
     * @param constraintValidatorContext
     * @return 如果返回false校验不通过，如果返回true校验通过。
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null){
            return  false;
        }
        if(s.equals("已发布") || s.equals("草稿")){
            return true;
        }
        return false;
    }
}
