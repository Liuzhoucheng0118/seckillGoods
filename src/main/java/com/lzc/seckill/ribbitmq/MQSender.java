package com.lzc.seckill.ribbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/14 22:13
 * @Version 1.0
 */
@Service
@Slf4j
public class MQSender { //生产者
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     *  生成秒杀商品消息
     * @param message
     */
    public void toSeckillGoods(String message){
        rabbitTemplate.convertAndSend("topicExchange","seckill.goods",message);
    }
}
