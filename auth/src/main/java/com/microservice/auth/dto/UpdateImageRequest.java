package com.microservice.auth.dto;

public class UpdateImageRequest {
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