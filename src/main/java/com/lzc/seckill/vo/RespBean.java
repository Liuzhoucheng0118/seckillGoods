package com.lzc.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/4 16:51
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespBean {
    private Integer code;
    private String message;
    private Object obj;

    /**
     * 成功无结果返回
     * @return
     */
    public static RespBean success(){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(),null);
    }

    /**
     * 成功有结果返回
     * @return
     */
    public static RespBean success(Object obj){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(),obj);
    }

    /**
     * 失败无结果返回
     * @return
     */
    public static RespBean error(RespBeanEnum respBeanEnum){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),null);
    }

    /**
     * 失败有结果返回
     * @return
     */
    public static RespBean error(RespBeanEnum respBeanEnum,Object obj){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),obj);
    }
}
