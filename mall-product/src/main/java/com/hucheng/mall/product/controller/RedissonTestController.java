package com.hucheng.mall.product.controller;

import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * @author MrHu
 */
@RestController
public class RedissonTestController {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/hello")
    public String hello(){
        //获取锁
        RLock lock = redissonClient.getLock("my-lock");
        //加锁
        lock.lock();
        try {
            System.out.println("加锁成功，执行业务....."+Thread.currentThread().getId());
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //解锁
            System.out.println("解锁..."+Thread.currentThread().getId());
            lock.unlock();
        }
        return "hello";
    }

    @GetMapping("/read")
    public String read(){
        //获取锁
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("rw-lock");
        String s ="";
        //加锁
        RLock rLock = readWriteLock.readLock();
        rLock.lock();
        try {
            System.out.println("读锁加锁成功，执行业务....."+Thread.currentThread().getId());
            s  = redisTemplate.opsForValue().get("k1");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //解锁
            System.out.println("解锁..."+Thread.currentThread().getId());
            rLock.unlock();
        }
        return s;
    }

    @GetMapping("/write")
    public String write(){
        //获取锁
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("rw-lock");
        String s ="";
        //加锁
        RLock rLock = readWriteLock.writeLock();
        try {
            rLock.lock();
            System.out.println("读锁加锁成功，执行业务....."+Thread.currentThread().getId());
            s= UUID.randomUUID().toString();
            Thread.sleep(10000);
            redisTemplate.opsForValue().set("k1",s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //解锁
            System.out.println("解锁..."+Thread.currentThread().getId());
            rLock.unlock();
        }
        return s;
    }
}
