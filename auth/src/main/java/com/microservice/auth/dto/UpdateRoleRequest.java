package com.microservice.auth.dto;

public class UpdateRoleRequest {
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