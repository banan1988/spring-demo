package com.example.demo.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "security.memory")
class SecurityMemoryProperties {

    private Map<String, Credentials> users = new HashMap<>();

    @Data
    static class Credentials {
        private String username;
        private String password;
        private String[] authorities;
    }

}
