package com.jeffrey.change.web.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author:jeffrey(jeffreyji@aliyun.com)
 * @date: 2018/12/16 22:42
 * @version:1.0.0
 * @description:TODO
 */

@Setter
@Getter
public class BaseResultVo {
    private String code;
    private String message;

    /**
     * 是否成功
     * @return
     */
    public boolean isSuccess(){
        if (code.equals("1")){
            return true;
        }
        return false;
    }
}
