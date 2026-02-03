package com.example.boilerplate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Profile({ "local", "dev" })
public class TestController {

    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/test/log")
    public String generateLog() {
        log.info("log1");
        log.warn("log2");
        log.error("log3");
        return "log";
    }

    @GetMapping("/test/redis")
    public String testRedis(@RequestParam String key, @RequestParam String value) {
        log.info("Setting Redis key: {} with value: {}", key, value);
        redisTemplate.opsForValue().set(key, value);

        Object storedValue = redisTemplate.opsForValue().get(key);
        log.info("Retrieved from Redis - Key: {}, Value: {}", key, storedValue);

        return "Redis set success. Key: " + key + ", Value: " + storedValue;
    }
}
