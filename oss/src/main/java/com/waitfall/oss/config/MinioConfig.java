package com.waitfall.oss.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Data
@AutoConfiguration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    private String endpoint;

    private String bucketName;

    private String accessKey;

    private String secretKey;

    @Bean
    public MinioClient minioClient() {
   
        return MinioClient.builder()
                .credentials(accessKey, secretKey)
                .endpoint(endpoint)
                .build();
    }
}
