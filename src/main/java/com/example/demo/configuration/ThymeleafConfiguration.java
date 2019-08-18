package com.example.demo.configuration;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
public class ThymeleafConfiguration {

//    @Autowired
//    private SpringResourceTemplateResolver defaultTemplateResolver;
//
//    @Bean
//    public SpringTemplateEngine templateEngine() {
//        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.addTemplateResolver(new UrlTemplateResolver());
//        templateEngine.addTemplateResolver(defaultTemplateResolver);
//        templateEngine.addDialect(new SpringSecurityDialect());
//        templateEngine.addDialect(new LayoutDialect());
//        templateEngine.addDialect(new Java8TimeDialect());
//        return templateEngine;
//    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

}
