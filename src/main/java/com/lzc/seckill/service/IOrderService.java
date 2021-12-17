package com.lzc.seckill.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lzc.seckill.pojo.Order;
import com.lzc.seckill.pojo.User;
import com.lzc.seckill.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuzhoucheng
 * @since 2021-12-07
 */
public interface IOrderService extends IService<Order> {

    Order creatOrder( User user,GoodsVo goods);
}
