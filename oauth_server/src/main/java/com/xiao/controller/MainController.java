package com.xiao.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @RequestMapping("/findInfo")
    public String findInfo(){
        return "oauth2.oauth_server  信息查询成功！";
    }

}
