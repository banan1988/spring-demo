package com.example.demo.controller;

import com.example.demo.BaseMvcController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class HomeController implements BaseMvcController {

    @Override
    public String module() {
        return "home";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

}
