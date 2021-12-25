package com.lzc.seckill.ribbitmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzc.seckill.pojo.Order;
import com.lzc.seckill.pojo.OrderMessage;
import com.lzc.seckill.pojo.User;
import com.lzc.seckill.service.IGoodsService;
import com.lzc.seckill.service.IOrderService;
import com.lzc.seckill.vo.GoodsVo;
import com.lzc.seckill.vo.RespBean;
import com.lzc.seckill.vo.RespBeanEnum;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/14 22:18
 * @Version 1.0
 */
@Service
@Slf4j
public class MQReceiver {   //消费者

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 秒杀商品消费接口
     *
     * @param message
     */
    @RabbitListener(queues = "seckillQueue")
    @RabbitHandler
    public void toSeckill(String message, Message mesg, Channel channel) throws IOException {
        log.info("秒杀商品成功接收到一条消费信息：" + message);

        OrderMessage orderMessage = JSON.parseObject(String.valueOf(JSON.parse(message)),OrderMessage.class);
        User user = orderMessage.getUser();
        Integer goodsId = orderMessage.getGoodsId();
//        核心业务
        try {
            GoodsVo goodsVo = goodsService.getGoodById(orderMessage.getGoodsId());
            Order order = (Order) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
//        再次判断是否重复下单
            if (order != null) {
                return;
            }
//        下单
            orderService.creatOrder(orderMessage.getUser(), goodsVo);
//        消息确认
            channel.basicAck(mesg.getMessageProperties().getDeliveryTag(), false);
            log.info("成功消费了一条信息，DeliveryTag={}", mesg.getMessageProperties().getConsumerTag());
        } catch (Exception e) {
            if (mesg.getMessageProperties().getRedelivered()) {
                log.error("消息已重复处理失败,拒绝再次接收...");
                channel.basicReject(mesg.getMessageProperties().getDeliveryTag(), false); // 拒绝消息
            } else {
                Long increment = redisTemplate.opsForValue().increment("message:"+user.getId()+goodsId);
                if (increment <= 3) {
                    //重新投回到队列
                    channel.basicNack(mesg.getMessageProperties().getDeliveryTag(), false, true);
                } else {
                    //消息重复次数太多，可能出现错误
                    log.info("Message=> user={},goodId={}", orderMessage.getUser(), orderMessage.getGoodsId());
                }
            }
        }

    }
}
