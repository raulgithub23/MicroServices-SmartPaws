package com.microservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request para actualizar datos del perfil")
public class UpdateProfileRequest {
    
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String name;
    
    @Schema(description = "Teléfono de contacto", example = "+56912345678")
    private String phone;

    public UpdateProfileRequest() {}

    public UpdateProfileRequest(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}