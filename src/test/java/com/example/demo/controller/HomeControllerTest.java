package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureTestDatabase
@WebMvcTest(value = HomeController.class, properties = {
        "spring.h2.console.enabled=false",
        "security.method=memory"
})
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void homeIsSecured() throws Exception {
        // given
        final MockHttpServletRequestBuilder request = get("/");

        // when
        final ResultActions result = mockMvc.perform(request);

        // then
        result.andExpect(unauthenticated())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser
    void homeAvailableForAuthenticated() throws Exception {
        // given
        final MockHttpServletRequestBuilder request = get("/");

        // when
        final ResultActions result = mockMvc.perform(request);

        // then
        result.andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Home")));
    }

}