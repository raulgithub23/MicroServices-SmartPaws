package com.microservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información detallada de un usuario")
public class UserDetailDto {
    
    @Schema(description = "ID único del usuario", example = "55")
    private Long id;
    
    @Schema(description = "Rol del usuario", example = "USER")
    private String rol;
    
    @Schema(description = "Nombre completo", example = "Juan Pérez")
    private String name;
    
    @Schema(description = "Email de contacto", example = "juan.perez@email.com")
    private String email;
    
    @Schema(description = "Teléfono de contacto", example = "+56912345678")
    private String phone;
    
    @Schema(description = "Ruta de la imagen de perfil", example = "/images/profile/user55.jpg")
    private String profileImagePath;

    public UserDetailDto() {}

    public UserDetailDto(Long id, String rol, String name, String email, String phone, String profileImagePath) {
        this.id = id;
        this.rol = rol;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profileImagePath = profileImagePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }
}