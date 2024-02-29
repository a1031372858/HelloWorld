package org.example.http_service;

import okhttp3.ResponseBody;
import org.example.model.to.UserTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * @author xuyachang
 * @date 2024/2/29
 */
public interface UserApiService {

    @POST("/api/user/read/findByPhone")
    Call<ResponseBody> findByPhone(@Body UserTO userTO);


    @GET("/api/user/read/getByPhone")
    Call<ResponseBody> getByPhone(@QueryMap Map<String,String> map);

}
