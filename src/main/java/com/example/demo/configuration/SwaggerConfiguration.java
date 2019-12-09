package com.example.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
class SwaggerConfiguration {

    @Autowired
    private BuildProperties buildProperties;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder()
                .title(getAppName() + " REST API")
                .description(getAppDescription())
                .contact(new Contact("BaNaN", "https://github.com/banan1988/spring-demo", ""))
                .license("MIT")
                .licenseUrl("https://raw.githubusercontent.com/banan1988/spring-demo/master/LICENSE")
                .version(getAppVersion())
                .build();
    }

    private String getAppName() {
        if (this.buildProperties != null) {
            return this.buildProperties.getName();
        }
        return "?";
    }

    private String getAppDescription() {
        if (this.buildProperties != null) {
            return this.buildProperties.get("description");
        }
        return "?";
    }

    private String getAppVersion() {
        if (this.buildProperties != null) {
            return this.buildProperties.getVersion();
        }
        return "?";
    }

}
