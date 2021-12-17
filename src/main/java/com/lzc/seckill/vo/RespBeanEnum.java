package com.lzc.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * 公共对象返回枚举
 * @Author liuzhoucheng
 * @Date 2021/12/4 16:48
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
@ToString
public enum RespBeanEnum {
    SUCCESS(200,"成功"),
    ERROR(500,"服务器异常"),
    LoGIN_ERROR(5201,"密码或手机号异常"),
    MOBILE_ERROR(5202,"手机格式错误"),
    BIND_ERROR(5203,"绑定异常"),
    EMTY_STOCK(50020,"商品库存为空"),
    REPEATE_ERROR(50010,"该商品每人限购一份"),
    SESSION_ISEMPTY(55555,"用户未登录"),
    ACCESS_LIMIT(40000,"请勿重复访问")
    ;


    private final Integer code;
    private final String message;
}
