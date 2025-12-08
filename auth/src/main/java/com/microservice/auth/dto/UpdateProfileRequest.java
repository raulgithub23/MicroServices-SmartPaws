package com.microservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request para actualizar datos del perfil")
public class UpdateProfileRequest {
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String name;
    
    @Schema(description = "Teléfono de contacto", example = "+56912345678")
    private String phone;
}