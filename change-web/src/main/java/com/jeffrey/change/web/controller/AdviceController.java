package com.jeffrey.change.web.controller;

import com.jeffrey.change.web.annotation.DecryptField;
import com.jeffrey.change.web.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * author:jeffrey(jeffreyji@aliyun.com)
 * date: 2018/12/1 22:31
 * version:1.0.0
 * description:restful加解密
 * 主要内容
 */
@Slf4j
@RestController
@RequestMapping("/advice")
public class AdviceController {


    @PostMapping("/getUserVo")
    @DecryptField(includes = {"userName", "passWord"})
    public UserVo getUserVo(@RequestBody UserVo userVo) {
        userVo.setId(ThreadLocalRandom.current().nextLong());
        int temp = 10 / 0;
        return userVo;
    }

    @GetMapping("/listUserVo")
    @DecryptField(includes = {"userName","passWord","phone"},encode = false)
    public List<UserVo> listUserVo(){
        return createUserVos();
    }


    private List<UserVo> createUserVos(){
        List<UserVo> userVos = new ArrayList<>();
        for (int i=0; i<10; i++){
            userVos.add(createUserVo());
        }
        return userVos;
    }

    private UserVo createUserVo(){
        UserVo userVo = new UserVo();
        userVo.setId(ThreadLocalRandom.current().nextLong());
        userVo.setUserName("userName:" + ThreadLocalRandom.current().nextInt());
        userVo.setPassWord("passWord:" + ThreadLocalRandom.current().nextInt());
        userVo.setPhone("18500237777");
        return userVo;
    }
}
