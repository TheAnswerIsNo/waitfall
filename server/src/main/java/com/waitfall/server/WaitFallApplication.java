package com.waitfall.server;

import lombok.extern.slf4j.Slf4j;
import org.dromara.autotable.springboot.EnableAutoTable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
@EnableAutoTable
@ComponentScan(basePackages = {"com.waitfall"})
@MapperScan("com.waitfall.*.domain.mapper")
public class WaitFallApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(WaitFallApplication.class, args);
        Environment environment = applicationContext.getEnvironment();
        String port = environment.getProperty("server.port");
        log.info("项目接口文档：http://{}:{}/doc.html","localhost",port);
    }

}
