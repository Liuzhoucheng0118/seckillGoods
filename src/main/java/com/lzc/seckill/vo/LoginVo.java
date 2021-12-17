package com.lzc.seckill.vo;

import com.lzc.seckill.validator.IsMobile;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/4 17:18
 * @Version 1.0
 */
@Data
@ToString
public class LoginVo {

    private String moblie;


    private String password;
}
