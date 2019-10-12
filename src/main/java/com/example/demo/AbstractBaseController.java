package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class AbstractBaseController {

    @Autowired
    private BuildProperties buildProperties;

    @ModelAttribute("appName")
    protected String appName() {
        return this.buildProperties.getName();
    }

    @ModelAttribute("appVersion")
    protected String appVersion() {
        return this.buildProperties.getVersion();
    }

    @ModelAttribute("module")
    protected abstract String module();

}
