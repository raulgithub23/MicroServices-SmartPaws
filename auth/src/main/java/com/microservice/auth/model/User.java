package com.microservice.auth.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auth_table") 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    private String rol = "USER";

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String password;

    private String profileImagePath = "drawable://larry";

    // Constructor para login
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Constructor completo para cargar usuarios iniciales
    public User(
        Long id,
        String rol,
        String name,
        String email,
        String phone,
        String password,
        String profileImagePath
    ) {
        this.id = id;
        this.rol = rol;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.profileImagePath = profileImagePath;
    }
}
