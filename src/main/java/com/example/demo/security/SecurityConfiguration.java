package com.example.demo.security;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.context.ShutdownEndpoint;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.example.demo.security.LoginController.LOGIN_ENDPOINT;

@EnableWebSecurity
@EnableConfigurationProperties({SecurityLdapProperties.class, SecurityMemoryProperties.class, SecurityJdbcProperties.class})
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LDAP = "ldap";
    private static final String MEMORY = "memory";
    private static final String JDBC = "jdbc";
    private static final Set<String> ALLOWED_METHODS = ImmutableSet.of(LDAP, MEMORY, JDBC);

    private static final String REMEMBER_ME_COOKIE_NAME = "remember-me";
    private static final String REMEMBER_ME_KEY = "X-?6?$Y8KSkXVr8teTshwTYvz-*4DTFk";
    private static final int REMEMBER_ME_TOKEN_VALIDITY_SECONDS = (int) TimeUnit.DAYS.toSeconds(1);

    private final String method;
    private final SecurityLdapProperties ldapProperties;
    private final SecurityMemoryProperties memoryProperties;
    private final SecurityJdbcProperties jdbcProperties;

    private final DataSource dataSource;

    public SecurityConfiguration(@Value("${security.method}") final String method,
                                 final SecurityLdapProperties ldapProperties,
                                 final SecurityMemoryProperties memoryProperties,
                                 final SecurityJdbcProperties jdbcProperties,
                                 final DataSource dataSource) {
        Preconditions.checkArgument(ALLOWED_METHODS.contains(method), "Incorrect method. Allowed methods: " + ALLOWED_METHODS);

        this.method = method;
        this.ldapProperties = ldapProperties;
        this.memoryProperties = memoryProperties;
        this.jdbcProperties = jdbcProperties;

        this.dataSource = dataSource;
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
            case JDBC:
                jdbcAuthentication(auth);
                return;
            default:
                final String msg = String.format("Not found implementation for provided method: %s. Allowed methods: %s", this.method, ALLOWED_METHODS);
                throw new Exception(msg);
        }
    }

    private void ldapAuthentication(final AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
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
                .inMemoryAuthentication();

        for (final SecurityMemoryProperties.Credentials credentials : memoryProperties.getUsers().values()) {
            builder = builder
                    .withUser(credentials.getUsername())
                    .password(credentials.getPassword())
                    .authorities(credentials.getAuthorities())
                    .and();
        }
    }

    private void jdbcAuthentication(final AuthenticationManagerBuilder auth) throws Exception {
        JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> builder = auth.jdbcAuthentication()
                // we've provided creation of tables via liquibase
//                .withDefaultSchema()
                .dataSource(dataSource);

        for (final SecurityJdbcProperties.Credentials credentials : jdbcProperties.getUsers().values()) {
            builder = builder
                    .withUser(credentials.getUsername())
                    .password(credentials.getPassword())
                    .authorities(credentials.getAuthorities())
                    .and();
        }
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // open
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/bootstrap4-glyphicons/**").permitAll()
                // endpoints
                .requestMatchers(EndpointRequest.to(ShutdownEndpoint.class)).hasAuthority("ACTUATOR_ADMIN")
                .requestMatchers(EndpointRequest.to(InfoEndpoint.class, HealthEndpoint.class)).permitAll()
                // rest is close
                .anyRequest().authenticated();

        //
        http.cors();
        http.csrf();

        // Sign In
        http.formLogin()
                .loginPage(LOGIN_ENDPOINT)
                .permitAll();

        // Sign Out
        http.logout()
                .permitAll();

        // handle "remember me"
        http.rememberMe()
                .tokenRepository(persistentTokenRepository())
                .rememberMeCookieName(REMEMBER_ME_COOKIE_NAME)
                .key(REMEMBER_ME_KEY)
                .tokenValiditySeconds(REMEMBER_ME_TOKEN_VALIDITY_SECONDS);
    }

    @Bean
    PersistentTokenRepository persistentTokenRepository() {
        final JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // we've provided creation of persistent_logins table via liquibase
        tokenRepository.setCreateTableOnStartup(false);
        return tokenRepository;
    }

}
