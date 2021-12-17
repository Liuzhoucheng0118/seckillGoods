package com.lzc.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/3 15:36
 * @Version 1.0
 */
@Component
public class MD5Utile {

    private static final String salt="1a2b3c4d";

    public static String getMD5(String str){
        return DigestUtils.md5Hex(str);
    }

    /**
     * 前端加密
     * @param inputPass
     * @return
     */
    public static String inputPassToFromPass(String inputPass){
        String str = salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return getMD5(str);
    }

    /**
     * 后端加密
     * @param inputPass
     * @param salt
     * @return
     */
    public static String formPassToDBPass(String inputPass,String salt){
        String str = salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(5);
        return getMD5(str);
    }

    /**
     * 两次加密到数据库
     * @param inputPass
     * @param salt
     * @return
     */
    public static String inputToDBPass(String inputPass,String salt){
        String oPass = inputPassToFromPass(inputPass);
        String tPass = formPassToDBPass(oPass,salt);
        return tPass;
    }

}
