package com.microservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request para actualizar la imagen de perfil")
public class UpdateImageRequest {
    
    @Schema(description = "Ruta de la nueva imagen de perfil", example = "/images/profile/user123.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String imagePath;

    public UpdateImageRequest() {}

    public UpdateImageRequest(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}