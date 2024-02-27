package org.example.util;

import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * @author xuyachang
 * @date 2024/2/26
 */
@RequiredArgsConstructor
@Component
public class OKHttpUtil {

    private final OkHttpClient client;

    public String get(String url, Map<String,String> param) {
        HttpUrl.Builder newBuilder = HttpUrl.parse(url).newBuilder();
        if(Objects.nonNull(param)){
            param.forEach(newBuilder::addQueryParameter);
        }
        HttpUrl httpUrl = newBuilder.build();
        Request request = new Request.Builder()
                .get()
                .url(httpUrl)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if(response.isSuccessful()){
                return response.body().string();
            }else {
                return "访问失败";
            }
        } catch (IOException e) {
            return "访问失败";
        }
    }

    public String post(String url,String json){
        RequestBody body = RequestBody.create(json,MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                return response.body().string();
            }else {
                return "访问失败";
            }
        } catch (IOException e) {
            return "访问失败";
        }
    }
}
