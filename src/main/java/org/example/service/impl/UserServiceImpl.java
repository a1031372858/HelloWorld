package org.example.service.impl;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.example.converter.UserConverter;
import org.example.http_service.UserApiService;
import org.example.model.po.UserPO;
import org.example.model.to.UserTO;
import org.example.service.UserService;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final OkHttpClient client;
    private final UserConverter userConverter;

    public UserTO userInfo(){
        UserPO userPO = new UserPO();
        userPO.setId(1L);
        userPO.setBirthday(new Date());
        userPO.setName("张三");
        userPO.setMobile("13000000000");
        UserTO userTO = userConverter.po2to(userPO);
        log.info(userTO.toString());
        return userTO;
    }

    @Override
    public String responseRetrofitPost() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://localhost:8082/")
                .client(client)
                .build();

        UserApiService userApiService = retrofit.create(UserApiService.class);
        UserTO userParam = new UserTO();
        userParam.setPhone("15797704512");
        Call<ResponseBody> call = userApiService.findByPhone(userParam);
        Response<ResponseBody> userTOResponse = null;
        try {
            userTOResponse = call.execute();
            if(userTOResponse.isSuccessful() && userTOResponse.code()==200){
                ResponseBody responseBody = userTOResponse.body();
                return responseBody.string();
            }else{
                return userTOResponse.message();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String responseRetrofitGet() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://localhost:8082/")
                .client(client)
                .build();

        UserApiService userApiService = retrofit.create(UserApiService.class);
        Map<String,String> userParam = new HashMap<>();
        userParam.put("phone","15797704512");
        Call<ResponseBody> call = userApiService.getByPhone(userParam);
        Response<ResponseBody> userTOResponse = null;
        try {
            userTOResponse = call.execute();
            if(userTOResponse.isSuccessful() && userTOResponse.code()==200){
                ResponseBody responseBody = userTOResponse.body();
                return responseBody.string();
            }else{
                return userTOResponse.message();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
