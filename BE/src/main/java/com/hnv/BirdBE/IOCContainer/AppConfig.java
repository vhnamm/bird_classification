package com.hnv.BirdBE.IOCContainer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();  // Spring giữ instance này, inject vào bất cứ đâu cần
    }
}