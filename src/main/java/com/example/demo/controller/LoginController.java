package com.example.demo.controller;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController extends AbstractBaseController {

    public static final String LOGIN_ENDPOINT = "/login";

    @Override
    String module() {
        return "login";
    }

    /**
     * POST is handle by SpringSecurity
     */
    @GetMapping(LOGIN_ENDPOINT)
    public String login(final Model model, final HttpServletRequest request,
                        @RequestParam(required = false) final String error,
                        @RequestParam(required = false) final String logout) {
        if (error != null) {
            final HttpSession session = request.getSession(false);
            if (session != null) {
                final AuthenticationException ex = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                final String errorMsg = ex != null ? ex.getMessage() : "Invalid credentials";
                model.addAttribute("errorMsg", errorMsg);
            }
        } else if (logout != null) {
            model.addAttribute("successMsg", "You have been signed out");
        }
        return "security/login";
    }

}
