package org.example.util;

import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author xuyachang
 * @date 2024/2/26
 */
@RequiredArgsConstructor
@Component
public class OKHttpUtil {

    private final OkHttpClient client;

    public String get(String url,Map<String,String> param){

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

    public void getDemo(){
        //创建okhttp客户端
        //在实际项目中，应该复用OkHttpClient，做成配置类交给spring容器管理
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                //连接失败时是否重试
                .retryOnConnectionFailure(false)
                //连接池
                .connectionPool(new ConnectionPool(200, 5, TimeUnit.MINUTES))
                //连接超时
                .connectTimeout(30, TimeUnit.SECONDS)
                //读超时
                .readTimeout(30, TimeUnit.SECONDS)
                //写超时
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        //构建url
        HttpUrl httpUrl = HttpUrl.parse("http://localhost:8080/hello");
        HttpUrl.Builder builder = httpUrl.newBuilder();
        builder.addQueryParameter("param1", "value");
        httpUrl = builder.build();

//        HttpUrl httpUrl = new HttpUrl.Builder()
//                //通信协议
//                .scheme("http")
//                //域名
//                .host("localhost")
//                //路径
//                .addPathSegment("hello")
//                //请求参数
//                .addQueryParameter("param1", "value")
//                //端口号
//                .port(8080)
//                .build();

        //构建请求
        Request request = new Request.Builder()
                //GET请求方式
                .get()
                //HttpUrl或url字符串
                .url(httpUrl)
                .build();

        //创建一个通话
        Call call = okHttpClient.newCall(request);
        //执行请求，结束时关闭连接
        try (Response response = call.execute()) {
            //返回响应
            if (response.isSuccessful() && 200 == response.code()) {
                //将响应体转化成字符串
                System.out.println(response.body().string());
            } else {
                //失败时返回失败信息
                System.out.println(response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    public void postDemo(){

        //创建okhttp客户端
        //在实际项目中，应该复用OkHttpClient，做成配置类交给spring容器管理
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                //连接失败时是否重试
                .retryOnConnectionFailure(false)
                //连接池
                .connectionPool(new ConnectionPool(200, 5, TimeUnit.MINUTES))
                //连接超时
                .connectTimeout(30, TimeUnit.SECONDS)
                //读超时
                .readTimeout(30, TimeUnit.SECONDS)
                //写超时
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        String json = "{\"param\":\"value\"}";
        //构建url
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);

        //构建请求
        Request request = new Request.Builder()
                //POST请求方式
                .post(body)
                //HttpUrl或url字符串
                .url("http://localhost:8080/hello")
                .build();

        //创建一个通话
        Call call = okHttpClient.newCall(request);
        //执行请求，结束时关闭连接
        try (Response response = call.execute()) {
            //返回响应
            if (response.isSuccessful() && 200 == response.code()) {
                //将响应体转化成字符串
                System.out.println(response.body().string());
            } else {
                //失败时返回失败信息
                System.out.println(response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
