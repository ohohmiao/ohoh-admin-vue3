package com.ohohmiao.modules.workflow.config;

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
 * 流程模块配置
 *
 * @author ohohmiao
 * @date 2024-12-02 16:53
 */
@Configuration
@EnableSwagger2WebMvc
public class WorkflowConfigurer {

    /** 是否开启swagger */
    @Value("${swagger.enabled}")
    private boolean enabled;

    @Bean(value = "workflowSwaggerDocket")
    public Docket workflowSwaggerDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enabled)
                .apiInfo(new ApiInfoBuilder()
                        .title("工作流模块")
                        .version("1.0.0")
                        .build())
                .groupName("工作流模块")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ohohmiao.modules.workflow"))
                .paths(PathSelectors.any())
                .build();
    }

}
