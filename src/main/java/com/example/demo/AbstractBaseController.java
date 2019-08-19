package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class AbstractBaseController {

    @Value("${info.app.name:appName}")
    private String appName;

    @ModelAttribute("appName")
    protected String appName() {
        return this.appName;
    }

    @ModelAttribute("module")
    protected abstract String module();

}
