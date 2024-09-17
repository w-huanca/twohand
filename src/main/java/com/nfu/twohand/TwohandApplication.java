package com.nfu.twohand;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.nfu.twohand.mapper")
public class TwohandApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwohandApplication.class, args);
    }

}
