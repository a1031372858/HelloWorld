package org.example.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.converter.UserConverter;
import org.example.model.po.UserPO;
import org.example.model.to.UserTO;
import org.example.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

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
}
