package com.surgee.ScandaPay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.cdimascio.dotenv.Dotenv;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor    
public class WebClientConfig {  
    private final Dotenv dotenv;
    @Bean
    public WebClient webClient() {

        final String FLUTTER_SECRET_KEY = dotenv.get("FLUTTER_API_SECRET_KEY");
        final String FLUTTER_BASE_URL = dotenv.get("FLUTTER_BASE_URL");

        return WebClient.builder()
                .baseUrl(FLUTTER_BASE_URL)
                .defaultHeaders(headers -> {
                    headers.setBearerAuth(FLUTTER_SECRET_KEY);
                    headers.set("Content-Type", "application/json");
                    // headers.set("Content-Type", "text/html; charset=UTF-8");
                    headers.set("Accept", "application/json");
                    
                })
                .build();
    }

}
