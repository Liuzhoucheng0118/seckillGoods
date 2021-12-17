package com.lzc.seckill.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzc.seckill.service.IGoodsService;
import com.lzc.seckill.service.IUserService;
import com.lzc.seckill.util.MD5Utile;
import com.lzc.seckill.vo.LoginVo;
import com.lzc.seckill.vo.RespBean;
import com.lzc.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/4 17:03
 * @Version 1.0
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private IUserService userService;

    /**
     * 跳转到登录页面
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "/login";
    }

    /**
     *  用户登录验证
     * @param loginVo   用户信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/login")
    public String toLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String newPass = MD5Utile.inputPassToFromPass(loginVo.getPassword());
        loginVo.setPassword(newPass);
        String userToken = userService.doLogin(loginVo, request, response);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("data",userToken);
        return "redirect:/goods/goods";
    }


}
