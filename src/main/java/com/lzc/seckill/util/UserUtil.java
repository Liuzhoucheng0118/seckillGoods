package com.lzc.seckill.util;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/11 21:58
 * @Version 1.0
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.qos.logback.core.db.dialect.DBUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzc.seckill.pojo.User;
import com.lzc.seckill.vo.RespBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class UserUtil {
    /**
     * 生成用户，插入数据库和加载到redis中，需要修改登录controller中的返回
     * @param count
     * @throws Exception
     */

    private static void createUser(int count) throws Exception{
        List<User> users = new ArrayList<User>(count);
        //生成用户
        for(int i=0;i<count;i++) {
            User user = new User();
            user.setId(130000+i);
            user.setLoginCount(1);
            user.setNickname("user"+i);
            user.setRegisterTime(new Date());
            user.setSlat("1a2b3c4d");
            user.setPassword(MD5Utile.inputToDBPass("123456", user.getSlat()));
            users.add(user);
        }
        System.out.println("create user");
//		//插入数据库
        Connection conn = getConn();
        String sql = "insert into t_user(login_count, nickname, register_Time, slat, password, id)values(?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(int i=0;i<users.size();i++) {
            User user = users.get(i);
            pstmt.setInt(1, user.getLoginCount());
            pstmt.setString(2, user.getNickname());
            pstmt.setTimestamp(3, new Timestamp(user.getRegisterTime().getTime()));
            pstmt.setString(4, user.getSlat());
            pstmt.setString(5, user.getPassword());
            pstmt.setLong(6, user.getId());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.close();
        conn.close();
        System.out.println("insert to db");
        //登录，生成token
        String urlString = "http://localhost:8080/login/login";
        File file = new File("C:\\tokens.txt");
        if(file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for(int i=0;i<users.size();i++) {
            User user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection)url.openConnection();
            co.setRequestMethod("GET");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "moblie="+user.getId()+"&password=123456";
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0 ,len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            JSONObject jo = JSON.parseObject(response);
            String token = jo.getString("data");
            System.out.println("create token : " + user.getId());

            String row = user.getId()+","+token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        raf.close();

        System.out.println("over");
    }

    public static Connection getConn() throws  Exception{
        String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123123";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url,username,password);
    }

    public static void main(String[] args)throws Exception {
        createUser(5000);
    }
}
