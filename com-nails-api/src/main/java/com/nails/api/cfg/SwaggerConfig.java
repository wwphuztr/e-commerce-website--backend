package com.nails.api.cfg;

import io.swagger.models.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    HashSet<String> consumesAndProduces = new HashSet<>(Arrays.asList("application/json"));
    @Bean
    public Docket  qrCodeApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .forCodeGeneration(true)
                .consumes(consumesAndProduces)
                .produces(consumesAndProduces)
                .useDefaultResponseMessages(false)
                .select().apis(RequestHandlerSelectors.basePackage("com.nails.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfor());
    }

    private ApiInfo metaInfor() {
        return new ApiInfoBuilder().title("E-Commerce website for selling electronic components (API DOCUMENTATION)")
                .description("PROJECT FOR SOFEWARE ENGINEERING")
                .termsOfServiceUrl("http://springfox.io").contact("springfox")
                .license("Apache License Version 2.0").licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
                .version("2.0")
                .build();
    }
}
