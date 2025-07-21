package com.waitfall.server.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author by 秋
 * @date 2025/7/8 22:50
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry){
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        interceptorRegistry.addInterceptor(new SaInterceptor(handle -> SaRouter.match("/**")
                .notMatch(EXCLUDE_PATH_PATTERNS)
                .check(r -> StpUtil.checkLogin())))
                .addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        corsRegistry.addMapping("/**")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowedMethods("*")
                .maxAge(3600);
    }

    private static final String[] EXCLUDE_PATH_PATTERNS ={
            "/v3/api-docs/**", "/__inspector__",
            "/doc.html","/*/css/**", "/*/js/**", "/favicon.ico", "/v3/api-docs/swagger-config"
    };
}

