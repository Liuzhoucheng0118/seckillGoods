package com.lzc.seckill.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzc.seckill.mapper.OrderMapper;
import com.lzc.seckill.pojo.Order;
import com.lzc.seckill.pojo.SeckillGoods;
import com.lzc.seckill.pojo.SeckillOrder;
import com.lzc.seckill.pojo.User;
import com.lzc.seckill.service.IOrderService;
import com.lzc.seckill.service.ISeckillGoodsService;
import com.lzc.seckill.service.ISeckillOrderService;
import com.lzc.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuzhoucheng
 * @since 2021-12-07
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Resource
    private OrderMapper orderMapper;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private ISeckillGoodsService seckillGoodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public Order creatOrder(User user, GoodsVo goods) {
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
//        库存不足
        if(seckillGoods.getStockCount()<1){
            redisTemplate.opsForValue().set("seckillGoodsEmpty:"+goods.getId(),0);
            return null;
        }
//        下单库存减一
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
//        更新到数据库中，并且返回结果，是否可以继续消费
        boolean result = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = " + "stock_count-1")
                .eq("goods_id", goods.getId()).gt("stock_count", 0));
        if (!result) {
//          下单失败
            return null;
        }
//        生成订单
        Order order = new Order();
        order.setGoodsId(goods.getId());
        order.setGoodsName(goods.getGoodsName());
        order.setDeliveryAddrId(1);
        order.setGoodsCount(1);
        order.setUserId(user.getId());
        order.setGoodsPrice(goods.getSeckillPrice());
        order.setStatus(0);
        int id = orderMapper.insert(order);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(seckillGoods.getId());
        seckillOrder.setOrderId(id);
        seckillOrder.setUserId(user.getId());
        seckillOrderService.save(seckillOrder);

//        下单成功后加载到redis中，防止重复消费
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goods.getId(),seckillOrder,1, TimeUnit.HOURS);
        return order;
    }
}
