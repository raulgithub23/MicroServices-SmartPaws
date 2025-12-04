package com.microservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información resumida de un usuario para listados")
public class UserListDto {
    
    @Schema(description = "ID único del usuario", example = "10")
    private Long id;
    
    @Schema(description = "Nombre completo", example = "María García")
    private String name;
    
    @Schema(description = "Rol del usuario", example = "DOCTOR")
    private String rol;

    public UserListDto() {}

    public UserListDto(Long id, String name, String rol) {
        this.id = id;
        this.name = name;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}