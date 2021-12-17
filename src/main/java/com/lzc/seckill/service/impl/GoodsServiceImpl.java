package com.lzc.seckill.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzc.seckill.mapper.GoodsMapper;
import com.lzc.seckill.pojo.Goods;
import com.lzc.seckill.service.IGoodsService;
import com.lzc.seckill.vo.GoodsVo;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper,Goods> implements IGoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取全部的商品
     * @return
     */
    @Override
    public List<GoodsVo> findGoodsVo(){
        List<GoodsVo> goods = redisTemplate.opsForHash().multiGet("good", new ArrayList());
        for (GoodsVo good : goods) {
            System.out.println(good.toString());
        }
        if(goods.size()>0){
            return goods;
        }

        goods = goodsMapper.findGoodsVo();
//        遍历存入redis中
        for (GoodsVo vo : goods) {
            redisTemplate.opsForHash().put("good",vo.getId().toString(),vo);
        }
        redisTemplate.expire("good",100,TimeUnit.SECONDS);
        return goods;
    }

    /**
     * 根据商品id获取对应的商品
     * @param id
     * @return
     */
    @Override
    public GoodsVo getGoodById(Integer id) {
        GoodsVo goods = (GoodsVo) redisTemplate.opsForValue().get("goods:" + id);
        if (goods!=null){
            return goods;
        }
        GoodsVo goodsVo = goodsMapper.getGoodById(id);
        return goodsVo;
    }


}
