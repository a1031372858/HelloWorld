package org.example.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.converter.UserConverter;
import org.example.model.po.UserPO;
import org.example.model.to.UserTO;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface UserService {

     UserTO userInfo();
}
