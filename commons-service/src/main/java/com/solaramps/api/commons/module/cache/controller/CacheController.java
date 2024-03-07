package com.solaramps.api.commons.module.cache.controller;

import com.solaramps.api.commons.module.cache.service.RedisService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/commons/cache")
public class CacheController {

    final RedisService redisService;

    CacheController(RedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/key/{key}")
    Object runConsumer(@PathVariable String key) {
        return redisService.getFromRedis(key);
    }
}
