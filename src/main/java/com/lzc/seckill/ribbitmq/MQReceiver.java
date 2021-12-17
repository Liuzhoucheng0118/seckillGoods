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
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
     * @param message
     */
    @RabbitListener(queues = "seckillQueue")
    public void toSeckill(String message){
        log.info("下单信息："+message);
//        反序列化
        OrderMessage orderMessage = JSONObject.parseObject(message, OrderMessage.class);
        User user = orderMessage.getUser();
        Integer goodsId = orderMessage.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodById(orderMessage.getGoodsId());
        Order order = (Order) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
//        再次判断是否重复下单
        if(order!=null){
            return ;
        }
//        下单
        orderService.creatOrder(orderMessage.getUser(), goodsVo);
    }
}
