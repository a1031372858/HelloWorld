package org.example.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.to.UserTO;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author xuyachang
 * @date 2024/8/18
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("redis/string")
public class RedissonController {

    private final RedissonClient redissonClient;


    /**
     * 测试数据
     * @return
     */
    @GetMapping("/saveUser")
    public String saveUser(){
        UserTO userTO = new UserTO();
        userTO.setId(1L);
        userTO.setBirthday(new Date());
        userTO.setName("张三");
        userTO.setMobile("15798809898");
//        String json = JSON.toJSONString(userTO);
        redissonClient.getBucket("test:user:1").set(userTO);
        return "成功";
    }

    /**
     * 根据key获取值
     * @param key
     * @return
     */
    @GetMapping("/{key}")
    public String stringFind(@PathVariable String key){
        Object result = redissonClient.getBucket(key).get();
        return result.toString();
    }

    /**
     * 根据key获取值
     * @param key
     * @return
     */
    @GetMapping("delete/{key}")
    public String delete(@PathVariable String key){
        Boolean result = redissonClient.getBucket(key).delete();
        return result.toString();
    }


    /**
     * 根据key value设置值
     * @param key
     * @param value
     * @return
     */
    @GetMapping("/set")
    public String stringSet(String key,String value){
        redissonClient.getBucket(key).set(value);
        return "成功";
    }


    /**
     * 获取旧值并设置新值
     * @param key
     * @param value
     * @return
     */
    @GetMapping("/getAndSet")
    public String stringGetAndSet(String key,String value){
        Object andSet = redissonClient.getBucket(key).getAndSet(value);
        return andSet.toString();
    }

    /**
     * 设置多个值
     * @return
     */
    @GetMapping("/multiSet")
    public String stringMultiSet(String key1,String value1,String key2,String value2){
        Map<String,String> map = new HashMap<>();
        map.put(key1,value1);
        map.put(key2,value2);
        redissonClient.getBuckets().set(map);
        return "成功";
    }

    /**
     * 获取多个值
     * @param key1
     * @param key2
     * @return
     */
    @GetMapping("/multiGet")
    public String stringMultiGet(String key1,String key2){
        List<String> map = new ArrayList<>();
        map.add(key1);
        map.add(key2);
        Map<String, Object> stringObjectMap = redissonClient.getBuckets().get(key1, key2);
        return stringObjectMap.get(key1).toString();
    }

    /**
     * 获取字符串长度
     * @param key
     * @return
     */
    @GetMapping("/getLen")
    public String stringGetLength(String key){
        Long size = redissonClient.getBucket(key).size();
        return size.toString();
    }

    /**
     * 整型数据+1,返回更新后的值
     * @param key
     * @return
     */
    @GetMapping("/increment")
    public String stringIncrement(String key){
        Long increment = redissonClient.getAtomicLong(key).incrementAndGet();
        return increment.toString();
    }


    /**
     * 整型数据-1,返回更新后的值
     * @param key
     * @return
     */
    @GetMapping("/decrement")
    public String stringDecrement(String key){
        Long decrement =redissonClient.getAtomicLong(key).decrementAndGet();
        return decrement.toString();
    }


    /**
     * 整型数据做加法
     * @param key
     * @param value
     * @return
     */
    @GetMapping("/incrementByLong")
    public String stringIncrementBy(String key,Long value){
        Long increment = redissonClient.getAtomicLong(key).addAndGet(value);
        return increment.toString();
    }

    /**
     * 整型数据做减法
     * @param key
     * @param value
     * @return
     */
    @GetMapping("/decrementByLong")
    public String stringDecrementBy(String key,Long value){
        Long decrement = redissonClient.getAtomicLong(key).addAndGet(-value);
        return decrement.toString();
    }


    /**
     * 浮点型数据做加法(没有浮点型做减法)
     * @param key
     * @param value
     * @return
     */
    @GetMapping("/incrementByDouble")
    public String stringIncrementBy(String key,Double value){
        Double increment = redissonClient.getAtomicDouble(key).addAndGet(value);
        return increment.toString();
    }

    @GetMapping("/getLock/{key}")
    public String getLock(@PathVariable String key){
        RLock lock = redissonClient.getLock(key);
        lock.lock(30, TimeUnit.SECONDS);
        return "锁成功";
    }

    /**
     * 布隆过滤器增加数据
     * @param value
     * @return
     */
    @GetMapping("/bl/add/{value}")
    public String blSet(@PathVariable String value){
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter("TEST:BLOOM:1");
        if(!bloomFilter.isExists()){
            bloomFilter.tryInit(100000,0.001);
        }
        boolean addResult = bloomFilter.add(value);
        return String.valueOf(addResult);
    }

    /**
     * 布隆过滤器校验数据
     * @param value
     * @return
     */
    @GetMapping("/bl/contains/{value}")
    public String blContains(@PathVariable String value){
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter("TEST:BLOOM:1");
        boolean containsResult = bloomFilter.contains(value);
        return String.valueOf(containsResult);
    }

}
