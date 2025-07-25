package com.waitfall.framework.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.SecurityService;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AutoConfiguration
@ConditionalOnClass({OpenAPI.class})
@ConditionalOnProperty(
        prefix = "springdoc.api-docs",
        name = {"enabled"},
        havingValue = "true",
        matchIfMissing = true
)
public class SwaggerConfig {

    @Bean
    public OpenAPI createApi() {
        Map<String, SecurityScheme> securitySchemas = this.buildSecuritySchemes();
        OpenAPI openApi = new OpenAPI()
                // 接口文档标题
                .info(new Info().title("API接口文档")
                        // 接口文档简介
                        .description("等秋接口文档")
                        // 接口文档版本
                        .version("1.0.0")
                        // 开发者联系方式
                        .contact(new Contact().name("秋")
                                .url("https://github.com/TheAnswerIsNo")
                                .email("2512448214@qq.com")))
                .components((new Components())
                        .securitySchemes(securitySchemas)).addSecurityItem((new SecurityRequirement()).addList("authorization"));
        securitySchemas.keySet().forEach((key) -> openApi.addSecurityItem((new SecurityRequirement()).addList(key)));
        return openApi;
    }

    private Map<String, SecurityScheme> buildSecuritySchemes() {
        Map<String, SecurityScheme> securitySchemes = new HashMap<>();
        SecurityScheme securityScheme = (new SecurityScheme()).type(SecurityScheme.Type.APIKEY).name("authorization").in(SecurityScheme.In.HEADER);
        securitySchemes.put("authorization", securityScheme);
        return securitySchemes;
    }

    @Bean
    @Primary
    public OpenAPIService openApiBuilder(Optional<OpenAPI> openApi, SecurityService securityParser, SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils, Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomizers, Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomizers, Optional<JavadocProvider> javadocProvider) {
        return new OpenAPIService(openApi, securityParser, springDocConfigProperties, propertyResolverUtils, openApiBuilderCustomizers, serverBaseUrlCustomizers, javadocProvider);
    }

    @Bean
    public GroupedOpenApi allGroupedOpenApi() {
        return buildGroupedOpenApi("全部", "com.waitfall");
    }

    public static GroupedOpenApi buildGroupedOpenApi(String group, String packageToScan) {
        return GroupedOpenApi.builder().group(group).packagesToScan(packageToScan)
                .addOperationCustomizer((operation, handlerMethod) -> operation.addParametersItem(buildSecurityHeaderParameter())).build();
    }

    public static GroupedOpenApi buildGroupedOpenApi(String group, String packageToScan, String packagesToExclude) {
        return GroupedOpenApi.builder().group(group).packagesToScan(packageToScan)
                .packagesToExclude(packagesToExclude)
                .addOperationCustomizer((operation, handlerMethod) -> operation.addParametersItem(buildSecurityHeaderParameter())).build();
    }

    private static Parameter buildSecurityHeaderParameter() {
        return (new Parameter()).name("authorization").description("认证 Token").in(String.valueOf(SecurityScheme.In.HEADER))
                .schema((new StringSchema())._default("tempToken").description("认证 Token"));
    }
}
