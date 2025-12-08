package com.microservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos para restablecer contraseña con token")
public class ResetPasswordDto {
    @Schema(description = "Token de recuperación enviado por email")
    private String token;
    
    @Schema(description = "Nueva contraseña")
    private String newPassword;
}