package com.fuj.fujitsuproject;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(
        basePackages = "com.fuj.fujitsuproject.weather"
)
public class ProjectConfig {

}
