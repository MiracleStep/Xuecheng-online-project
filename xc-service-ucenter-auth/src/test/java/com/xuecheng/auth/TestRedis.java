package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //创建jwt令牌
    @Test
    public void testRedis(){
        //定义key
        String key = "7141c106-f496-4499-b449-c2b146678e2f";
        //定义value
        HashMap<String, String> value = new HashMap<>();
        value.put("jwt","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTYyNjAyNjczMSwianRpIjoiNzE0MWMxMDYtZjQ5Ni00NDk5LWI0NDktYzJiMTQ2Njc4ZTJmIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.EKrccv8XiIJqThRLh-kB2bv5-urxIvRgJD1pOHEbqY3mkGn5H70ZFKonCUWAo8WRsnuywNzhix5bTG0URdmzWwZ-5GBuUnOdC4Yh_jMddx2_62EBEYX06mXvYr-vhGLtaicrnFtIayKOUTZZ9yQ99pnbGVEDw9frxCjZZDdJDjL61Smk5Lpp37YPZtgnqJ4NsLTdEaQoEkhuf__sXAEfFSv2Mtd-OEiC79gdNw-7dhVOgj_Exs7eGzxQAPjebyikI9PLC23YslF03fV2obYiWJtJKDvCxQ-zLJrVt154AZAiEnYSbAPWu8zx1rUAProsIUll2PextTAYwSwIDKtEZA");
        value.put("refresh_token","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJhdGkiOiI3MTQxYzEwNi1mNDk2LTQ0OTktYjQ0OS1jMmIxNDY2NzhlMmYiLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTYyNjAyNjczMSwianRpIjoiNWQzYzgxYTMtMWIwMi00YmQyLWFmYzktZGUyZjcxMjc5MzYxIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.Nfwa0H-9Do-96T8NCH_p-GEuBT_xONtJZ8LAZMxb4b4qKu0AtKYeXaKgC2IcuuzyBwIHSofh-mI--njWmhY_JlGTrc_uYl68czvPxBbVqVUw6axyPbgqvvAs07Z40jFdwCBgWSAlRPXJA1dxaYZzfDrUbecxy0rmUuivh5QIKFPGcZzTQAHzoibglYmtrJDvRdmd8ZT-qXLBiFVrsvufvM1UfULJcP0cSyPNBRLk3kSdv36lC8Y4S--s11rpRD39ltTunA-wLRpGhik1FQkQzABzIndKb1wWnkm34cqyMdrmOVVU7U4ddvELEOgJhkIv9EA65KhzD9nu_lukbgCQ-w");
        String jsonString = JSON.toJSONString(value);
        //存储数据
        System.out.println(jsonString);
        //获取数据
        stringRedisTemplate.boundValueOps(key).set(jsonString,30, TimeUnit.SECONDS);
        String string = stringRedisTemplate.opsForValue().get(key);
        System.out.println(string);
    }
}
