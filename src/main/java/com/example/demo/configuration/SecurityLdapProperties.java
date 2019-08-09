package com.example.demo.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "security.ldap")
public class SecurityLdapProperties {

    private String userDnPatterns;
    private String groupSearchBase;
    private String url;
    private String baseDn;
    private String managerDn;
    private String managerPassword;
    private String passwordAttribute;

}
