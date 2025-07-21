package com.waitfall.server.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author by 秋
 * @date 2025/7/8 22:52
 */
@Configuration
public class SaTokenConfiguration {

    @Value(value = "${token.jwtSecretKey}")
    private String jwtSecretKey;

    @Bean
    public StpLogic getStpLogic() {
        return new StpLogicJwtForSimple();
    }

    @Bean
    @Primary
    public SaTokenConfig getSaTokenConfigPrimary() {
        SaTokenConfig config = new SaTokenConfig();
        // token 名称（同时也是 cookie 名称）
        config.setTokenName("authorization");
        // token 有效期（单位：秒），默认30天，-1代表永不过期
        config.setTimeout(60 * 60 * 24 * 30L);
        // token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结，默认-1 代表不限制，永不冻结
        config.setActiveTimeout(-1);
        // 是否允许同一账号多地同时登录（为 true 时允许一起登录，为 false 时新登录挤掉旧登录）
        config.setIsConcurrent(true);
        // 在多人登录同一账号时，是否共用一个 token （为 true 时所有登录共用一个 token，为 false 时每次登录新建一个 token）
        config.setIsShare(true);
        // token 风格
        config.setTokenStyle("simple-uuid");
        // 是否输出操作日志
        config.setIsLog(true);
        // 是否读取cookie中的token
        config.setIsReadCookie(false);
        // 是否读取body中的token
        config.setIsReadBody(false);
        // 将token写入响应头中
        config.setIsWriteHeader(true);
        // 设置jwt密钥
        config.setJwtSecretKey(jwtSecretKey);
        return config;
    }
}
