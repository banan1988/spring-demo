package com.example.demo.configuration;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.context.ShutdownEndpoint;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import java.util.Set;

import static com.example.demo.controller.LoginController.LOGIN_ENDPOINT;

@Configuration
@EnableConfigurationProperties({SecurityLdapProperties.class, SecurityMemoryProperties.class})
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LDAP = "ldap";
    private static final String MEMORY = "memory";
    private static final Set<String> ALLOWED_METHODS = ImmutableSet.of(LDAP, MEMORY);

    private static final int REMEMBER_ME_TOKEN_VALIDITY_SECONDS = 3600 * 24; // 1 day
    private static final String REMEMBER_ME_KEY = "X-?6?$Y8KSkXVr8teTshwTYvz-*4DTFk";
    private static final String REMEMBER_ME_COOKIE_NAME = "remember-me";

    private final String method;
    private final SecurityLdapProperties ldapProperties;
    private final SecurityMemoryProperties memoryProperties;

    public SecurityConfiguration(@Value("${security.method}") final String method,
                                 final SecurityLdapProperties ldapProperties,
                                 final SecurityMemoryProperties memoryProperties) {
        Preconditions.checkArgument(ALLOWED_METHODS.contains(method), "Incorrect method. Allowed methods: " + ALLOWED_METHODS);

        this.method = method;
        this.ldapProperties = ldapProperties;
        this.memoryProperties = memoryProperties;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        switch (this.method) {
            case LDAP:
                ldapAuthentication(auth);
                return;
            case MEMORY:
                memoryAuthentication(auth);
                return;
            default:
                final String msg = String.format("Not found implementation for provided method: %s. Allowed methods: %s", this.method, ALLOWED_METHODS);
                throw new Exception(msg);
        }
    }

    private void ldapAuthentication(final AuthenticationManagerBuilder auth) throws Exception {
//        final LdapAuthoritiesPopulator populator = new DefaultLdapAuthoritiesPopulator();
        auth
                .ldapAuthentication()
//                .ldapAuthoritiesPopulator(populator)
                .userDnPatterns(ldapProperties.getUserDnPatterns())
                .groupSearchBase(ldapProperties.getGroupSearchBase())
                .contextSource()
                .url(ldapProperties.getUrl() + ldapProperties.getBaseDn())
                .and()
                .passwordCompare()
                .passwordEncoder(new LdapShaPasswordEncoder())
                .passwordAttribute(ldapProperties.getPasswordAttribute());
    }

    private void memoryAuthentication(final AuthenticationManagerBuilder auth) throws Exception {
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> builder = auth
                .inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance());

        for (final SecurityMemoryProperties.Credentials credentials : memoryProperties.getUsers().values()) {
            builder = builder
                    .withUser(credentials.getUsername())
                    .password(credentials.getPassword())
                    .authorities(credentials.getAuthorities())
                    .and();
        }
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // what is open/close
                .requestMatchers(EndpointRequest.to(ShutdownEndpoint.class)).hasAuthority("ACTUATOR_ADMIN")
                .requestMatchers(EndpointRequest.to(InfoEndpoint.class, HealthEndpoint.class)).permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/bootstrap4-glyphicons/**").permitAll()
                // rest is close
                .anyRequest().fullyAuthenticated()
                //
                .and().cors()
                .and().csrf()
                // Sign In
                .and().formLogin()
                .loginPage(LOGIN_ENDPOINT)
                .permitAll()
                // Sign Out
                .and().logout()
                .deleteCookies(REMEMBER_ME_COOKIE_NAME)
                .permitAll()
                // handle "remember me"
                .and().rememberMe()
                .rememberMeCookieName(REMEMBER_ME_COOKIE_NAME)
                .key(REMEMBER_ME_KEY)
                .tokenValiditySeconds(REMEMBER_ME_TOKEN_VALIDITY_SECONDS);
    }

}
