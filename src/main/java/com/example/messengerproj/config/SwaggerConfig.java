package com.example.messengerproj.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().info(new Info().title("사내 메신저 API")
                .description("JWT기반 사내 메신저 프로젝트 API")
                .version("v1.0.0")
                .contact(new Contact().name("H-namho")));
    }
}
