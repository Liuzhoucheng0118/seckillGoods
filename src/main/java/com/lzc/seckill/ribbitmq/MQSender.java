package com.lzc.seckill.ribbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
    @Autowired
    private ConfirmService confirmService;
    @Autowired
    private RetrueCallbackService retrueCallbackService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *  生成秒杀商品消息
     * @param message
     */
    public void toSeckillGoods(String message){
        rabbitTemplate.setMandatory(true);//失败重新回到队列
        rabbitTemplate.setConfirmCallback(confirmService);//消息传递回调
        rabbitTemplate.setReturnCallback(retrueCallbackService);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("topicExchange","seckill.goods",message,correlationData);
    }
}
