package org.example.util;

import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.File;
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

    public String get(String url,Map<String,String> param){
        //构建url
//        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
//                .scheme("http")
//                .host("localhost")
//                .addPathSegment("api/user/read/getByPhone")
//                .port(8082);

        HttpUrl builderUrl = HttpUrl.parse(url);
        if(Objects.isNull(builderUrl)){
            throw new NullPointerException("url不合法");
        }
        HttpUrl.Builder urlBuilder = builderUrl.newBuilder();
        if(Objects.nonNull(param)){
            param.forEach(urlBuilder::addQueryParameter);
        }
        HttpUrl httpUrl = urlBuilder.build();
        //构建请求
        Request request = new Request.Builder()
                .get()
                .url(httpUrl)
                .build();

        //执行请求
        return executeRequest(request);
    }

    public String postJson(String url,String json){
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);

        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();

        return executeRequest(request);
    }

    public String postFrom(String url,Map<String,String> param){
        FormBody.Builder formBuilder = new FormBody.Builder();
        param.forEach(formBuilder::add);
        RequestBody formBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        //执行请求
        return executeRequest(request);
    }

    public String postFile(String url,String fileUrl){
        File file = new File(fileUrl);
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), file))
                .build();

        return executeRequest(request);
    }

    private String executeRequest(Request request){
        //执行请求
        try (Response response = client.newCall(request).execute()){
            //返回响应
            return handleResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private String handleResponse(Response response) throws IOException {
        if(response.isSuccessful() && 200 == response.code()){
            return response.body().string();
        }else{
            return response.message();
        }
    }
}
