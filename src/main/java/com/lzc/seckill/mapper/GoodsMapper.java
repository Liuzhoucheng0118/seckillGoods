package com.lzc.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzc.seckill.pojo.Goods;
import com.lzc.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuzhoucheng
 * @since 2021-12-07
 */
public interface GoodsMapper extends BaseMapper<Goods> {
    List<GoodsVo> findGoodsVo();

    GoodsVo getGoodById(Integer id);
}
