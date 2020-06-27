package com.xiao.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    //@Secured("ROLE_ADMIN")
    @RequestMapping("/findInfo")
    public String findInfo(){
        return "oauth2.oauth_source  信息查询成功！";
    }

}
