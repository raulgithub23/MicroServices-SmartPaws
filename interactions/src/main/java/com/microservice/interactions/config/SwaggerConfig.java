package com.microservice.interactions.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
  @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Citas e Historial - SmartPaws")
                        .version("1.0")
                        .description("API REST para la gestión de Citas e Historial en el sistema de SmartPaws. " +
                        "Permite crear, consultar, actualizar y eliminar Citas, así como consultar el historial")
                        );
    }
}
