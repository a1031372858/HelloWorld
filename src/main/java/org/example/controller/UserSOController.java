package org.example.controller;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.example.model.so.UserSO;
import org.example.repository.UserSORepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author xuyachang
 * @date 2024/9/26
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("userSO")
public class UserSOController {

    private final UserSORepository userSORepository;

    @GetMapping("get/{id}")
    public String findById(@PathVariable Long id){
        Optional<UserSO> userSOOptional = userSORepository.findById(id);
        return userSOOptional.map(JSON::toJSONString).orElse(null);
    }

    @GetMapping("create/{id}")
    public String create(@PathVariable Long id){
        UserSO userSO = new UserSO();
        userSO.setId(id);
        userSO.setName("张三");
        userSO.setBirthday(new Date());
        userSO.setMobile("110"+id);
        UserSO save = userSORepository.save(userSO);
        return JSON.toJSONString(save);
    }

    @GetMapping("page")
    public String page(){
        List<UserSO> result = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(0,5);
        Page<UserSO> userSOs = userSORepository.findAll(pageRequest);
        for (UserSO userSO : userSOs) {
            result.add(userSO);
        }
        return JSON.toJSONString(result);
    }

    @GetMapping("delete/{id}")
    public String deleteById(@PathVariable Long id){
        userSORepository.deleteById(id);
        return "True";
    }

    @PostMapping("update")
    public String update(@RequestBody UserSO userSO){
        UserSO save = userSORepository.save(userSO);
        return JSON.toJSONString(save);
    }
}
