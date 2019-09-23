package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(1)
@ConditionalOnProperty(prefix = "spring.h2.console", name = "enabled", havingValue = "true", matchIfMissing = false)
public class H2SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private H2ConsoleProperties properties;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        final String whitelistH2Pattern = antPattern(properties.getPath());

        // we need config just for console, nothing else
        http.antMatcher(whitelistH2Pattern)
                .authorizeRequests().anyRequest().permitAll()
                // this will ignore only h2-console csrf
                .and().csrf().ignoringAntMatchers(whitelistH2Pattern)
                // this will allow frames with same origin which is much more safe
                .and().headers().frameOptions().sameOrigin();
    }

    private String antPattern(final String path) {
        return (path.endsWith("/") ? path + "**" : path + "/**");
    }

}
