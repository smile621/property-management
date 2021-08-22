package com.smile.eam.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Value("${spring.profiles.active:NA}")
    private String active;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2) //OAS_30
                .enable("dev".equals(active))// 仅在开发环境开启Swagger
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("net.daimaku.eam.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("资产管理项目API文档")
                .description("资产管理API")
                .version("1.0")
                .build();
    }
}