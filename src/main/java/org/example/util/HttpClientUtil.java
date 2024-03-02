package org.example.util;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xuyachang
 * @date 2024/2/29
 */
public class HttpClientUtil {

    private static final CloseableHttpClient client;

    static {
        HttpClientBuilder clientBuilder = HttpClients.custom();
        PoolingHttpClientConnectionManager clientConnectionManager = new PoolingHttpClientConnectionManager();
        clientConnectionManager.setMaxTotal(50);
        clientConnectionManager.setDefaultMaxPerRoute(50);

        clientBuilder.setConnectionManager(clientConnectionManager);

        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(30, TimeUnit.SECONDS)
                .setResponseTimeout(30,TimeUnit.SECONDS)
                .build();
        clientBuilder.setDefaultRequestConfig(config);
        client = clientBuilder.build();
    }

    private HttpClientUtil(){}


    public static CloseableHttpClient getClient(){
        return client;
    }

    public static String get(){
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet("http://localhost:8082/api/user/read/getByPhone");
        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            EntityUtils.consume(entity);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public static void postFrom(){
        try {
            // 创建httppost
            HttpPost httpPost = new HttpPost("http://localhost:8080/xxxxxx");
            RequestConfig config = RequestConfig.custom()
                    .setConnectionRequestTimeout(30, TimeUnit.SECONDS)
                    .setResponseTimeout(30,TimeUnit.SECONDS)
                    .build();
            httpPost.setConfig(config);
            httpPost.setHeader("content-type","");
//            httpPost.addHeader();
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("phone", "15797704512"));
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, Charset.defaultCharset());
            httpPost.setEntity(uefEntity);
            CloseableHttpResponse response = null;
            UrlEncodedFormEntity urlEntity = null;
            try {
                response = client.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }finally {
                if (response != null) {
                    response.close();
                }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void postJson(){
        try {
            // 创建httppost
            HttpPost httpPost = new HttpPost("http://localhost:8082/api/user/read/getByPhone");
            RequestConfig config = RequestConfig.custom()
                    .setConnectionRequestTimeout(30, TimeUnit.SECONDS)
                    .setResponseTimeout(30,TimeUnit.SECONDS)
                    .build();
            httpPost.setConfig(config);
            httpPost.setHeader("content-type","application/json; charset=utf-8");

            StringEntity jsonEntity = new StringEntity("json",StandardCharsets.UTF_8);
            CloseableHttpResponse response = null;
            UrlEncodedFormEntity urlEntity = null;
            try {
                response = client.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }finally {
                if (response != null) {
                    response.close();
                }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
