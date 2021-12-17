package com.lzc.seckill.config;

import com.alibaba.fastjson.JSONObject;
import com.lzc.seckill.pojo.User;
import com.lzc.seckill.service.IUserService;
import com.lzc.seckill.util.CookieUtil;
import com.lzc.seckill.vo.RespBean;
import com.lzc.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/17 14:22
 * @Version 1.0
 */
@Component
public class AccessInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 拦截器，判断用户访问次数是否超过设定的值
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = getUser(request,response);
        UserContext.setUser(user);//向threadlocal中加入user对象
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if(accessLimit==null){
                return true;
            }
//            获取注解值
            int second = accessLimit.second();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if(needLogin==true){
                if(user==null){
                    render(response, RespBean.error(RespBeanEnum.SESSION_ISEMPTY));
                    return false;
                }
                key+=":"+user.getId();
                ValueOperations valueOperations = redisTemplate.opsForValue();
                Integer count = (Integer) valueOperations.get(key);
                if(count==null){
                    valueOperations.set(key,1,second, TimeUnit.SECONDS);
                }else if(count<maxCount){
                    valueOperations.increment(key);
                }else{
                    render(response, RespBean.error(RespBeanEnum.ACCESS_LIMIT));
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 响应返回信息
     * @param response
     * @param error
     * @throws IOException
     */
    private void render(HttpServletResponse response, RespBean error) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSONObject.toJSONString(error));
        writer.flush();
        writer.close();
    }

    /**
     * 获取用户
     * @param request
     * @param response
     * @return
     */
    private User getUser(HttpServletRequest request, HttpServletResponse response) {

        String userToken = CookieUtil.getCookieValue(request, "userToken");
        if ("".equals(userToken))
            return null;
        return userService.getUserByToken(request, response, userToken);
    }
}
