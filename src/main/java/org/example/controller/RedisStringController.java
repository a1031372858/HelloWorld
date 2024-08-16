package org.example.controller;

import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuyachang
 * @date 2024/8/16
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("redis/string")
public class RedisStringController {
    private final RedisTemplate<String,String> redisTemplate;

    /**
     * 根据key获取值
     * @param key
     * @return
     */
    @GetMapping("/{key}")
    public String stringFind(@PathVariable String key){
        String result = redisTemplate.opsForValue().get(key);
        return result;
    }


    /**
     * 根据key value设置值
     * @param key
     * @param value
     * @return
     */
    @GetMapping("/set")
    public String stringSet(String key,String value){
        redisTemplate.opsForValue().set(key,value);
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
        String andSet = redisTemplate.opsForValue().getAndSet(key, value);
        return andSet;
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
        redisTemplate.opsForValue().multiSet(map);
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
        List<String> values = redisTemplate.opsForValue().multiGet(map);
        if(CollectionUtil.isNotEmpty(values)){
            return String.join(",",values);
        }
        return "空";
    }

    /**
     * 获取字符串长度
     * @param key
     * @return
     */
    @GetMapping("/getLen")
    public String stringGetLength(String key){
        Long size = redisTemplate.opsForValue().size(key);
        return size.toString();
    }
    /**
     * 获取字符串字串
     * @param key
     * @return
     */

    @GetMapping("/getRange")
    public String stringGetRange(String key,Long start,Long end){
        String str = redisTemplate.opsForValue().get(key,start,end);
        return str;
    }

    /**
     * 设置字符串字串
     * @param key
     * @param value
     * @param offset
     * @return
     */
    @GetMapping("/setRange")
    public String stringSetRange(String key,String value,Long offset){
        redisTemplate.opsForValue().set(key,value,offset);
        return "成功";
    }


    /**
     * 追加新内容到末尾
     * @param key
     * @param value
     * @return
     */
    @GetMapping("/append")
    public String stringAppend(String key,String value){
        redisTemplate.opsForValue().append(key,value);
        return "成功";
    }

    /**
     * 整型数据+1,返回更新后的值
     * @param key
     * @return
     */
    @GetMapping("/increment")
    public String stringIncrement(String key){
        Long increment = redisTemplate.opsForValue().increment(key);
        return increment.toString();
    }


    /**
     * 整型数据-1,返回更新后的值
     * @param key
     * @return
     */
    @GetMapping("/decrement")
    public String stringDecrement(String key){
        Long decrement = redisTemplate.opsForValue().decrement(key);
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
        Long increment = redisTemplate.opsForValue().increment(key,value);
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
        Long decrement = redisTemplate.opsForValue().decrement(key,value);
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
        Double increment = redisTemplate.opsForValue().increment(key,value);
        return increment.toString();
    }

}
