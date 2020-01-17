package com.example.springbootswagger2.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 李家幸
 * @class 计科三班
 * @create 2020-01-14 21:17
 */
@Configuration
@EnableSwagger2
public class Swagger2AutoConfiguration {
    @Bean
    public Docket swaggerSpringMvcPlugin(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select().apis(RequestHandlerSelectors
                .withMethodAnnotation(ApiOperation.class)).build();
    }


    private ApiInfo apiInfo(){
        return new ApiInfoBuilder().description("这是一个很NB的Api工具")
                /*名片*/
                .contact(new Contact("Kahen","http://www.Kahen.com","171543319@m.gduf.edu.cn"))
                /*版本*/
                .version("1.0")
                /*所有者*/
                .license("清新一中")
                //构造
                .build();
    }
}
