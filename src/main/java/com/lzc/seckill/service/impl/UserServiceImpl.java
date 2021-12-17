package com.lzc.seckill.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzc.seckill.exception.GlobalException;
import com.lzc.seckill.mapper.IUserMapper;

import com.lzc.seckill.pojo.User;
import com.lzc.seckill.service.IUserService;
import com.lzc.seckill.util.CookieUtil;
import com.lzc.seckill.util.MD5Utile;
import com.lzc.seckill.vo.LoginVo;
import com.lzc.seckill.vo.RespBean;
import com.lzc.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuzhoucheng
 * @since 2021-12-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<IUserMapper, User> implements IUserService {

    @Resource
    private IUserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMoblie();
        String password = loginVo.getPassword();
        User user = userMapper.getUser(Integer.valueOf(mobile));
        if(user==null){
            throw new GlobalException(RespBeanEnum.LoGIN_ERROR);
        }

        if(!MD5Utile.formPassToDBPass(password,user.getSlat()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LoGIN_ERROR);
        }
        String userToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("user:"+userToken,user);
        CookieUtil.setCookie(request,response,"userToken",userToken);
        return userToken;
    }

    @Override
    public User getUserByToken(HttpServletRequest request, HttpServletResponse response, String token) {
        if ("".equals(token)) {
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:" + token);
//        if(user!=null){
//            CookieUtil.setCookie(request,response,"userToken",UUID.randomUUID().toString());
//        }
        return user;
    }
}
