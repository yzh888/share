package com.soft1851.content.center.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.stream.Collectors;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    /**
     * swagger 信息
     */
    private ApiInfo apiInfo(){
        return new ApiInfo(
                "share-app Swagger文档",
                "github地址: https://github.com/baby1273163614/share-app",
                "API V1.0",
                "Terms of service",
                new Contact("吴家浩","https://wjh.cn","1273163614@qq.com"),
                "Apache","http://www.apache.org/", Collections.emptyList());

    }

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.soft1851.content.center"))
                .build()
                .apiInfo(apiInfo());
    }
}
