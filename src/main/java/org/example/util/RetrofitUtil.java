package org.example.util;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.example.http_service.UserApiService;
import org.example.model.to.UserTO;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author xuyachang
 * @date 2024/2/29
 */
@RequiredArgsConstructor
@Component
public class RetrofitUtil {

    private final OkHttpClient client;

    public static void main(String[] args) {

    }
}
