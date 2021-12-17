package com.lzc.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzc.seckill.mapper.SeckillGoodsMapper;
import com.lzc.seckill.mapper.SeckillOrderMapper;
import com.lzc.seckill.pojo.SeckillGoods;
import com.lzc.seckill.pojo.SeckillOrder;
import com.lzc.seckill.service.ISeckillOrderService;
import org.springframework.stereotype.Service;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/17 16:06
 * @Version 1.0
 */
@Service
public class SeckillOrderService extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {
}
