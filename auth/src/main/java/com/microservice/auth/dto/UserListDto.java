package com.microservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información resumida de un usuario para listados")
public class UserListDto {
    @Schema(description = "ID único del usuario")
    private Long id;
    
    @Schema(description = "Nombre completo")
    private String name;
    
    @Schema(description = "Rol del usuario")
    private String rol;
}