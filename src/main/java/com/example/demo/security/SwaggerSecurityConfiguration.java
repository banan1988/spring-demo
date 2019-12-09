package com.example.demo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(2)
class SwaggerSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String WHITELIST_SWAGGER_RESOURCES_PATTERN = "/swagger-resources/**";
    private static final String WHITELIST_SWAGGER_UI_PATTERN = "/swagger-ui.html";
    private static final String WHITELIST_SWAGGER_API_PATTERN = "/v2/api-docs";

    private static final String[] WHITELIST_SWAGGER_PATTERNS = new String[]{
            WHITELIST_SWAGGER_RESOURCES_PATTERN,
            WHITELIST_SWAGGER_UI_PATTERN,
            WHITELIST_SWAGGER_API_PATTERN,
    };

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.requestMatchers()
                .antMatchers(WHITELIST_SWAGGER_PATTERNS)
                .and()
                .authorizeRequests().anyRequest().permitAll();
    }

}
