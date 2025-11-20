package com.microservice.auth.dto;

public class UserDetailDto {
    private Long id;
    private String rol;
    private String name;
    private String email;
    private String phone;
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