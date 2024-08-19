package org.example.config;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xuyachang
 * @date 2024/8/18
 */
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        //EPOLL是Linux上性能优于NIO的网络I/O处理方式(需要依赖包)
        config.setTransportMode(TransportMode.NIO);
        //设置序列化编码
        config.setCodec(new JsonJacksonCodec());
        //设置单机模式下的redis配置
        String address = "redis://" + host + ":" + port;
        config.useSingleServer()
                .setPassword(password)
                .setAddress(address);
        return Redisson.create(config);
    }
}
