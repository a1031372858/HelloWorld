package org.example.controller;


import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.converter.UserConverter;
import org.example.model.po.UserPO;
import org.example.model.to.UserTO;
import org.example.service.UserService;
import org.example.util.OKHttpUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("hello")
public class HelloController {

    private final UserService userService;

    private final OKHttpUtil okHttpUtil;


    @GetMapping("index")
    public String helloWorld(){
        return "Hello World";
    }

    @GetMapping("user/get")
    public UserTO userInfo(){
        return userService.userInfo();
    }

    @GetMapping("response/get")
    public String response(){
        HashMap<String, String> param = new HashMap<>();
        param.put("phone","15797704512");
        return okHttpUtil.get("http://localhost:8082/api/user/read/getByPhone",param);
    }

    @GetMapping("response/post")
    public String responsePost(){
        HashMap<String, String> param = new HashMap<>();
        param.put("phone","15797704512");
        String json = JSON.toJSONString(param);
        return okHttpUtil.postJson("http://localhost:8082/api/user/read/findByPhone",json);
    }
}
