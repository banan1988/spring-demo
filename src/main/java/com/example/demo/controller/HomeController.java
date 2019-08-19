package com.example.demo.controller;

import com.example.demo.AbstractBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController extends AbstractBaseController {

    @Override
    protected String module() {
        return "home";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

}
