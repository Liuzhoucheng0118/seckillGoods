package com.lzc.seckill.config;

import com.lzc.seckill.pojo.User;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/17 14:30
 * @Version 1.0
 */
public class UserContext {  //用户线程私有化自己的user对象

    private static ThreadLocal<User> t = new ThreadLocal<User>();

    public static void setUser(User user){
        t.set(user);
    }

    public static User getUser(){
        return t.get();
    }
}
