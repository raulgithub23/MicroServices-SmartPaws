package com.microservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request para subir imagen de perfil en Base64")
public class ImageUploadRequest {
    @Schema(description = "Nombre del archivo", example = "profile.jpg")
    private String fileName;
    
    @Schema(description = "Tipo de contenido", example = "image/jpeg")
    private String contentType;
    
    @Schema(description = "Imagen codificada en Base64")
    private String imageBase64;
}