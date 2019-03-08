package com.qhjys.springcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
@Profile({"default", "test"})
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.qhjys.springcloud.controller"))
                .paths(PathSelectors.any())
                .build();
//                .globalOperationParameters(headerToken());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("部门服务接口")
                .description("swagger")
                .termsOfServiceUrl("")
                .version("1.0.0")
                .build();
    }

//    private List<Parameter> headerToken() {
//        ParameterBuilder parameterBuilder = new ParameterBuilder();
//        parameterBuilder.name("token").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
//        List<Parameter> parameters = new ArrayList<>();
//        parameters.add(parameterBuilder.build());
//        return parameters;
//    }
}
