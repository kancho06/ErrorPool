package com.sparta.errorpool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ErrorPoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(ErrorPoolApplication.class, args);
    }

}
