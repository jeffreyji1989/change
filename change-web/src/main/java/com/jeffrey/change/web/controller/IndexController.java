package com.jeffrey.change.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:jeffrey(jeffreyji@aliyun.com)
 * @date: 2018/12/15 9:48
 * @version:1.0.0
 * @description:TODO
 */
@RestController
@RequestMapping("index")
public class IndexController {

    @GetMapping("/")
    public String index(){
        return "hello,world";
    }

}
