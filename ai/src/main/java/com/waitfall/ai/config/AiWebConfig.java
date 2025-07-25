package com.waitfall.ai.config;

import com.waitfall.framework.config.SwaggerConfig;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AiWebConfig {

    /**
     * ai 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi aiGroupedOpenApi() {
        return SwaggerConfig.buildGroupedOpenApi("ai模块", "com.waitfall.ai.controller");
    }

}
