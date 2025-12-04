package com.microservice.doctor.config;


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
                        .title("API de Doctores - SmartPaws")
                        .version("1.0")
                        .description("API REST la carga de horario de doctores. " +
                        "Permite crear, consultar, actualizar y eliminar doctores, así como consultar el historial de las proximas citas." )
                        );
    }
}

