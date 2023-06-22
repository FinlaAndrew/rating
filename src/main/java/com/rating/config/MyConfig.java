package com.rating.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class MyConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate;
    }
}
