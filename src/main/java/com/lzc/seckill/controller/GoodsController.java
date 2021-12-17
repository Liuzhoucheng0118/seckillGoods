package com.lzc.seckill.controller;

import com.lzc.seckill.pojo.User;
import com.lzc.seckill.service.IGoodsService;
import com.lzc.seckill.service.IUserService;
import com.lzc.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/5 20:15
 * @Version 1.0
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService userService;


    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 获取全部商品
     * 优化前 958
     * @param model
     * @param user
     * @return
     */

    @RequestMapping(value = "/goods",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toGoods(Model model, User user, HttpServletRequest request,HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goods");//从缓存中看是否有商品列表
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        List<GoodsVo> goodsList = goodsService.findGoodsVo();
        model.addAttribute("goodsList",goodsList);
        //获得容器并交给Thmeleaf解析;
        WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods", webContext);
        if(!StringUtils.isEmpty(html));{
            valueOperations.set("goods",html,100, TimeUnit.SECONDS);//存入缓存中
        }
        return html;
    }

    /**
     * 根据商品id获得商品详情,并进行缓存渲染后的页面
     * @param model
     * @param user
     * @param id 商品id
     * @return
     */

    @RequestMapping(value = "goods/{id}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String getGoodById(Model model, User user, @PathVariable Integer id, HttpServletRequest request,HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodId:" + id);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        GoodsVo goods = goodsService.getGoodById(id);
        Date startDate = goods.getStartDate();
        Date endDate = goods.getEndDate();
        Date nowDate = new Date();
        int secKillStatus = 0;  //秒杀状态
        int remainSeconds = 0;

        if(nowDate.before(startDate)){
            remainSeconds = ((int) (startDate.getTime()-nowDate.getTime())/1000);  //还没开始
        }else if(nowDate.after(endDate)){
            secKillStatus = 2;  //已经结束
            remainSeconds = -1;
        }else{
            secKillStatus = 1;  //秒杀中
        }
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("secKillStatus",secKillStatus);
        model.addAttribute("goods",goods);

        WebContext context = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("detail", context);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodId:"+id,html,100, TimeUnit.SECONDS);
        }
        return html;
    }
}
