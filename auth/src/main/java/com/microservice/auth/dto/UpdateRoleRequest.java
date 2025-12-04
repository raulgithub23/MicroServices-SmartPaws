package com.microservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request para actualizar el rol de un usuario")
public class UpdateRoleRequest {
    
    @Schema(description = "Nuevo rol del usuario", example = "ADMIN", allowableValues = {"USER", "ADMIN", "DOCTOR"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String rol;

    public UpdateRoleRequest() {}

    public UpdateRoleRequest(String rol) {
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}