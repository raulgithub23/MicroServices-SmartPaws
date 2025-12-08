package com.microservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request para solicitar recuperación de contraseña")
public class PasswordResetRequest {
    @Schema(description = "Email del usuario", example = "usuario@smartpaws.cl")
    private String email;
}
