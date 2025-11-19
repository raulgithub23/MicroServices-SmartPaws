package com.microservice.auth.dto;

public class UserListDto {
    private Long id;
    private String name;
    private String rol;

    public UserListDto() {}

    public UserListDto(Long id, String name, String rol) {
        this.id = id;
        this.name = name;
        this.rol = rol;
    }

    // Getters y Setters
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