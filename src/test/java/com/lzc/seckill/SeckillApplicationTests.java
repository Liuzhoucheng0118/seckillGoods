package com.lzc.seckill;

import com.lzc.seckill.util.MD5Utile;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class SeckillApplicationTests {

    @Test
    void contextLoads() {

        System.out.println(MD5Utile.inputToDBPass("7wzhzgmqmzllzj", "1a2b3c4d"));

    }
}
