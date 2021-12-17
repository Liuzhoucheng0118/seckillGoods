package com.lzc.seckill.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/4 17:47
 * @Version 1.0
 */
public class ValidatorUtil {

    private final static Pattern mobile_pattern = Pattern.compile("[1]([3-9])[0-9]{9}$");

    /**
     * 手机密码格式验证
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile){
        if(mobile.isEmpty()){
            return false;
        }
        Matcher matcher = mobile_pattern.matcher(mobile);
        return matcher.matches();
    }
}
