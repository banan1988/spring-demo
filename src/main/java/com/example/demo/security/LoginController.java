package com.example.demo.security;

import com.example.demo.BaseMvcController;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
class LoginController implements BaseMvcController {

    static final String LOGIN_ENDPOINT = "/login";

    @Override
    public String module() {
        return "login";
    }

    /**
     * SpringSecurity handles POST
     *
     * @param model         model
     * @param error         error during login ?
     * @param logout        logout ?
     * @param authException AuthenticationException e.g. from LDAP authentication
     * @return login form
     */
    @GetMapping(LOGIN_ENDPOINT)
    public String login(final Model model,
                        @RequestParam(required = false) final String error,
                        @RequestParam(required = false) final String logout,
                        @SessionAttribute(value = WebAttributes.AUTHENTICATION_EXCEPTION, required = false) final AuthenticationException authException) {
        if (error != null) {
            final String errorMsg = getErrorMsg(authException);
            model.addAttribute("errorMsg", errorMsg);
        }
        if (logout != null) {
            model.addAttribute("successMsg", "You have been signed out");
        }
        return "security/login";
    }

    private String getErrorMsg(final AuthenticationException authException) {
        if (authException != null && authException.getMessage() != null) {
            return authException.getMessage();
        }
        return "Invalid credentials";
    }

}
