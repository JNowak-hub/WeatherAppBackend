package com.jakub.weather.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.util.Collections;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {



    @Bean
    public Docket get() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(basePackage("com.jakub.weather.controller"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .directModelSubstitute(LocalDate.class, String.class)
                .apiInfo(apiInfo())
                .genericModelSubstitutes(ResponseEntity.class)
                //TODO uzupełnić, gdy dodam security
//              .securitySchemes(Collections.singletonList(apiKey))
                ;
    }
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "WeatherApp",
                "Application for showing weather for your city",
                "0.1",
                null,
                new Contact("Nowak Jakub", null, "nowakjakub095@gmail.com"),
                null,
                null,
                Collections.EMPTY_LIST
        );
    }
}
