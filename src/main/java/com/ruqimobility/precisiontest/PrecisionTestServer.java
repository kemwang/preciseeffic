package com.ruqimobility.precisiontest;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.ruqi.auth.support.restful.config.EnableCasRestfulClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.SpringVersion;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = { DruidDataSourceAutoConfigure.class})
@ComponentScan({"com.ruqimobility","com.ruqimobility.precisiontest"})
@EnableCaching
@EnableAsync
@EnableCasRestfulClient
@EnableScheduling
public class PrecisionTestServer {

    public static void main(String[] args) {
        System.setProperty("log4j2.formatMsgNoLookups", "true");
        String springVersion = SpringVersion.getVersion();
        String springBootVersion = SpringBootVersion.getVersion();
        System.out.println("Spring版本:"+springVersion+"\nSpringBoot版本:"+springBootVersion);
        SpringApplication.run(PrecisionTestServer.class, args);
    }

}
