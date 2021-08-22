package com.smile.eam;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@MapperScan
@SpringBootApplication
public class EamApplication {
    public static void main(String[] args) {
        SpringApplication.run(EamApplication.class, args);
    }
}
