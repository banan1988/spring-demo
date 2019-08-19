package com.example.demo.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "security.ldap")
class SecurityLdapProperties {

    private String userDnPatterns;
    private String groupSearchBase;
    private String url;
    private String baseDn;
    private String managerDn;
    private String managerPassword;
    private String passwordAttribute;

}
