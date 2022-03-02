package com.awsdemo.springbootawsredisdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("/put")
    public void setRedisObject(@RequestParam String value) {
        redisTemplate.opsForValue().set("name", value);
    }

    @PutMapping("/get")
    public String getRedisObject() {
        return (String) redisTemplate.opsForValue().get("name");
    }
}
