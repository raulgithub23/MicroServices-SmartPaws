package com.microservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información detallada de un usuario")
public class UserDetailDto {
    @Schema(description = "ID único del usuario")
    private Long id;
    
    @Schema(description = "Rol del usuario")
    private String rol;
    
    @Schema(description = "Nombre completo")
    private String name;
    
    @Schema(description = "Email de contacto")
    private String email;
    
    @Schema(description = "Teléfono de contacto")
    private String phone;
    
    @Schema(description = "Indicador de si tiene imagen de perfil", example = "HAS_IMAGE")
    private String hasProfileImage; // Cambiado de profileImagePath a hasProfileImage
}