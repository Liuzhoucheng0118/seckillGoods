package com.lzc.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/16 15:38
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessage {
    private User user;
    private Integer goodsId;
}
