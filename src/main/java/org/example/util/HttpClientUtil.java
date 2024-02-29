package org.example.util;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xuyachang
 * @date 2024/2/29
 */
public class HttpClientUtil {

    public void get(){
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            // 创建httppost
            HttpPost httpPost = new HttpPost("http://localhost:8080/xxxxxx");
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("phone", "15797704512"));
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, Charset.defaultCharset());
            httpPost.setEntity(uefEntity);
            CloseableHttpResponse response = null;
            UrlEncodedFormEntity urlEntity = null;
            try {
                response = httpClient.execute(httpPost);
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
                httpClient.close();
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
