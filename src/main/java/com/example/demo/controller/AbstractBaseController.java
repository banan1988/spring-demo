package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class AbstractBaseController {

    @Value("${info.app.name:appName}")
    private String appName;

    @ModelAttribute("appName")
    String appName() {
        return this.appName;
    }

    @ModelAttribute("module")
    abstract String module();

}
