package com.lzc.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzc.seckill.pojo.Goods;
import com.lzc.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuzhoucheng
 * @since 2021-12-07
 */
public interface IGoodsService extends IService<Goods> {
    List<GoodsVo> findGoodsVo();

    GoodsVo getGoodById(Integer id);
}
