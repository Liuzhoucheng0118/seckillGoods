package com.lzc.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/16 15:29
 * @Version 1.0
 */
@Configuration
public class TopicRabbitMQConfig {//秒杀mq对应的队列和交换机设置

    private static final String seckillQueue = "seckillQueue";
    private static final String topicExchange = "topicExchange";


    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(topicExchange);
    }
    @Bean
    public Queue seckillQueue(){
        return new Queue(seckillQueue);
    }

    @Bean
    public Binding seckillBinding(){
        return BindingBuilder.bind(seckillQueue()).to(topicExchange()).with("seckill.#");
    }
}
