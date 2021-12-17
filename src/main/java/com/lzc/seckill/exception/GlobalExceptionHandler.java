package com.lzc.seckill.exception;

import com.lzc.seckill.vo.RespBean;
import com.lzc.seckill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/5 19:48
 * @Version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {//异常处理

    @ExceptionHandler(Exception.class)
    public RespBean ExceptionHandler(Exception e){
        if(e instanceof GlobalException){
            GlobalException ex = (GlobalException) e;
            return RespBean.error(ex.getRespBeanEnum());
        }else if(e instanceof BindException){
            BindException be = (BindException) e;
            RespBean error = RespBean.error(RespBeanEnum.BIND_ERROR);
            error.setMessage("参数异常："+be.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return error;
        }
        return RespBean.error(RespBeanEnum.ERROR);
    }
}
