package com.jeffrey.change.web.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author:jeffrey(jeffreyji@aliyun.com)
 * @date: 2018/12/1 22:32
 * @version:1.0.0
 * @description:TODO
 */
@ToString
@Setter
@Getter
public class UserVo {
    private Long id;
    private String userName;
    private String passWord;
    private String phone;
}
