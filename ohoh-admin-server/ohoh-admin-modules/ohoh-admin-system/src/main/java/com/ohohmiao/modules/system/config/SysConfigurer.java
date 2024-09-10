package com.ohohmiao.modules.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * 系统模块配置
 *
 * @author ohohmiao
 * @date 2023-05-31 16:44
 */
@Configuration
@EnableSwagger2WebMvc
public class SysConfigurer {

    /** 是否开启swagger */
    @Value("${swagger.enabled}")
    private boolean enabled;

    @Bean(value = "sysSwaggerDocket")
    public Docket sysSwaggerDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enabled)
                .apiInfo(new ApiInfoBuilder()
                        .title("系统模块")
                        .version("1.0.0")
                        .build())
                .groupName("系统模块")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ohohmiao.modules.system"))
                .paths(PathSelectors.any())
                .build();
    }

}
