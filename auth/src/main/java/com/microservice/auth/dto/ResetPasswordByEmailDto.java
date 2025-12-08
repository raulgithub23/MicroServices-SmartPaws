package com.microservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos para restablecer contraseña usando email")
public class ResetPasswordByEmailDto {
    @Schema(description = "Email del usuario", example = "usuario@smartpaws.cl")
    private String email;
    
    @Schema(description = "Nueva contraseña")
    private String newPassword;
}