package com.fuj.fujitsuproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FujitsuProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FujitsuProjectApplication.class, args);
    }

}
