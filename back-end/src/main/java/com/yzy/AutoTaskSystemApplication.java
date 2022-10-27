package com.yzy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AutoTaskSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoTaskSystemApplication.class, args);
    }

}
