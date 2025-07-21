package com.waitfall.system.config;

import com.waitfall.framework.core.config.SwaggerConfig;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SystemWebConfig {

    /**
     * system 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi systemGroupedOpenApi() {
        return SwaggerConfig.buildGroupedOpenApi("系统模块", "com.waitfall.system.controller");
    }

}
