package com.lzc.seckill.vo;

import com.lzc.seckill.util.ValidatorUtil;
import com.lzc.seckill.validator.IsMobile;
import jdk.nashorn.internal.objects.annotations.Constructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/4 20:29
 * @Version 1.0
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {

    private  boolean required = false;
    @Override
    public void initialize(IsMobile constraintAnnotation) {
            required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (required){
            return ValidatorUtil.isMobile(s);
        }else{
            if (s.isEmpty()){
                return true;
            }else{
                return ValidatorUtil.isMobile(s);
            }
        }
    }
}
