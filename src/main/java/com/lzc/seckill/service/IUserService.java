package com.lzc.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzc.seckill.pojo.User;
import com.lzc.seckill.vo.LoginVo;
import com.lzc.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuzhoucheng
 * @since 2021-12-04
 */
public interface IUserService extends IService<User> {

    String doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    User getUserByToken(HttpServletRequest request,HttpServletResponse response,String token);
}
