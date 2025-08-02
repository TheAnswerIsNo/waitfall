package com.waitfall.ai.config;

import cn.dev33.satoken.filter.SaTokenContextFilterForJakartaServlet;
import com.waitfall.framework.config.SwaggerConfig;
import jakarta.servlet.DispatcherType;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.EnumSet;

@Configuration(proxyBeanMethods = false)
public class AiWebConfig {

    /**
     * ai 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi aiGroupedOpenApi() {
        return SwaggerConfig.buildGroupedOpenApi("ai模块", "com.waitfall.ai.controller");
    }


    /**
     * 注册 Sa-Token 上下文过滤器  当使用Flux时只有最开始时同步，后续异步返回，因此定义过滤器允许异步处理填充上下文
     */
    @Bean
    public FilterRegistrationBean<SaTokenContextFilterForJakartaServlet> saTokenContextFilterForJakartaServlet() {
        FilterRegistrationBean<SaTokenContextFilterForJakartaServlet> bean =
                new FilterRegistrationBean<>(new SaTokenContextFilterForJakartaServlet());
        // 配置 Filter 拦截的 URL 模式
        bean.addUrlPatterns("/*");
        // 设置 Filter 的执行顺序,数值越小越先执行
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        bean.setAsyncSupported(true);
        bean.setDispatcherTypes(EnumSet.of(DispatcherType.ASYNC, DispatcherType.REQUEST));
        return bean;
    }
}
