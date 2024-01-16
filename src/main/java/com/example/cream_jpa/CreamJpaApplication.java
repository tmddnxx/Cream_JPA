package com.example.cream_jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CreamJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreamJpaApplication.class, args);
    }

}
