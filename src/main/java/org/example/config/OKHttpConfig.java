package org.example.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author xuyachang
 * @date 2024/2/26
 */
@Configuration
public class OKHttpConfig {

    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient().newBuilder()
                .retryOnConnectionFailure(false)
                .connectionPool(pool())
                .connectTimeout(30,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .build();
    }
    @Bean
    public ConnectionPool pool() {
        return new ConnectionPool(200, 5, TimeUnit.MINUTES);
    }
}
