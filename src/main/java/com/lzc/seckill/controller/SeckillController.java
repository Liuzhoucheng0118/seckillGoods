package com.lzc.seckill.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzc.seckill.config.AccessLimit;
import com.lzc.seckill.pojo.Order;
import com.lzc.seckill.pojo.OrderMessage;
import com.lzc.seckill.pojo.User;
import com.lzc.seckill.ribbitmq.MQSender;
import com.lzc.seckill.service.IGoodsService;
import com.lzc.seckill.service.IOrderService;
import com.lzc.seckill.vo.GoodsVo;
import com.lzc.seckill.vo.RespBean;
import com.lzc.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/9 20:45
 * @Version 1.0
 */
@Controller
@Slf4j
public class SeckillController implements InitializingBean {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqSender;

    private static  HashMap<Integer,Boolean> goodsMap = new HashMap<>();


    /**
     *  商品秒杀接口
     * qds 485
     * 238
     * @param user  当前用户
     * @param model
     * @param goodId    商品id
     * @return
     */
    @RequestMapping(value = "/seckill")
    @AccessLimit(second=5,maxCount=5,needLogin=true)
    @ResponseBody
    public String seckillGoods(User user, Model model, Integer goodId){
//        是否登录
        if (user == null){
            return "login";
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
//        是否重复下单
        Order order = (Order) valueOperations.get("order:" + user.getId() + ":" + goodId);
        if(order!=null){
            model.addAttribute("msg",RespBean.error(RespBeanEnum.REPEATE_ERROR));
            return "fail";
        }
//        是否还有库存，用map进行状态标记，避免大量访问redis
        if(goodsMap.get(goodId)){
            return "fail";
        }
//        库存减1
        Long decrement = valueOperations.decrement("secKillGoods:" + goodId);
//        库存不足
        if(decrement<0){
            goodsMap.put(goodId,true);
            valueOperations.increment("secKillGoods:"+goodId);
            model.addAttribute("msg", RespBean.error(RespBeanEnum.EMTY_STOCK));
            return "seckillFail";
        }
//        序列化
        OrderMessage orderMessage = new OrderMessage(user,goodId);
        String message = JSON.toJSONString(orderMessage);
//        向队列中加入下单信息
        mqSender.toSeckillGoods(message);
//        消息失败的重传次数
        redisTemplate.opsForValue().set("message:"+user.getId()+goodId,10,10, TimeUnit.SECONDS);
        return "success";
    }

    /**
     * 秒杀控制器一旦初始化就加入秒杀商品的库存
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(goodsVoList)){
            return;
        }
        goodsVoList.forEach(goodsVo -> {
            goodsMap.put(goodsVo.getId(),false);
            redisTemplate.opsForValue().set("secKillGoods:"+goodsVo.getId(),goodsVo.getStockCount());
        });
    }
}
