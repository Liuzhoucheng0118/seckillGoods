package com.lzc.seckill.ribbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/25 16:12
 * @Version 1.0
 */
@Component
@Slf4j
public class ConfirmService implements RabbitTemplate.ConfirmCallback {//投递确认
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println(correlationData+":");
        if (ack){
            log.info("消息成功投递");
        }else{
            log.info("消息失败，原因："+cause);
        }
    }
}
