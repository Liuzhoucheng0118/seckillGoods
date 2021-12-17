package com.lzc.seckill.exception;

import com.lzc.seckill.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/5 19:44
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GlobalException extends RuntimeException {
    private RespBeanEnum respBeanEnum;
}
