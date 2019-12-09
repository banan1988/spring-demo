package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = BaseMvcController.class)
class BaseMvcAdviceController {

    @Autowired
    private BuildProperties buildProperties;

    @ModelAttribute("appName")
    String appName() {
        if (this.buildProperties != null) {
            return this.buildProperties.getName();
        }
        return "?";
    }

    @ModelAttribute("appVersion")
    String appVersion() {
        if (this.buildProperties != null) {
            return this.buildProperties.getVersion();
        }
        return "?";
    }

}
