package com.example.demo.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureTestDatabase
@WebMvcTest(value = LoginController.class, properties = {
        "spring.h2.console.enabled=false",
        "security.method=memory"
})
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginAvailableForAll() throws Exception {
        // given
        final MockHttpServletRequestBuilder request = get("/login");

        // when
        final ResultActions result = mockMvc.perform(request);

        // then
        result.andExpect(status().isOk())
                .andExpect(content().string(containsString("Sign in")));
    }

    @Test
    void shouldAuthenticateValidUser() throws Exception {
        // given
        final SecurityMockMvcRequestBuilders.FormLoginRequestBuilder request = formLogin("/login")
                .user("user").password("pass");

        // when
        final ResultActions result = mockMvc.perform(request);

        // then
        result.andExpect(authenticated())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void shouldNotAuthenticateInvalidUser() throws Exception {
        // given
        final SecurityMockMvcRequestBuilders.FormLoginRequestBuilder request = formLogin("/login")
                .user("invalid").password("invalid");

        // when
        final ResultActions result = mockMvc.perform(request);

        // then
        result.andExpect(unauthenticated())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?error"));
    }

}