package com.jeffrey.change.web.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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

    private static final String ERR_MESSAGE = "手机号不能为空";

    private Long id;
    @NotBlank(message = "用户名不能为空")
    private String userName;
    private String passWord;
    @NotBlank(message = UserVo.ERR_MESSAGE)
    private String phone;
}
