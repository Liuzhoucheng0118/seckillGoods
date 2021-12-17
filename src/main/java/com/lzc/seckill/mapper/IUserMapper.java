package com.lzc.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzc.seckill.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuzhoucheng
 * @since 2021-12-04
 */

public interface IUserMapper extends BaseMapper<User> {
    public User getUser(Integer id);
}
