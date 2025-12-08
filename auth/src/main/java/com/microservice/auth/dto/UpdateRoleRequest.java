package com.microservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request para actualizar el rol de un usuario")
public class UpdateRoleRequest {
    @Schema(description = "Nuevo rol del usuario", example = "ADMIN", allowableValues = {"USER", "ADMIN", "DOCTOR"})
    private String rol;
}