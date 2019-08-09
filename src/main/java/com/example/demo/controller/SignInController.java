package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SignInController extends AbstractBaseController {

    @Override
    String module() {
        return "signin";
    }

    @RequestMapping(value = "/signin", method = {RequestMethod.GET, RequestMethod.POST})
    public String signin() {
        return "security/signin";
    }

}
