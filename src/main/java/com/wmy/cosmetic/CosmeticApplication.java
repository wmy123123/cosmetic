package com.wmy.cosmetic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.wmy.cosmetic.mapper")
public class CosmeticApplication {
    public static void main(String[] args) {
        SpringApplication.run(CosmeticApplication.class, args);
    }
}
