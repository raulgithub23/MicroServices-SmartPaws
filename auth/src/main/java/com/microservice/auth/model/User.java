package com.microservice.auth.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auth_table") // Mantenemos el nombre de tu tabla original
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    // VARIABLE ORIGINAL: name (Se respeta, nada de firstName/lastName)
    @Column(nullable = false)
    private String name;

    // VARIABLE ORIGINAL: email
    @Column(nullable = false, unique = true)
    private String email;

    // VARIABLE ORIGINAL: phone (Se respeta, nada de phoneNumber)
    @Column(nullable = false)
    private String phone;

    // VARIABLE ORIGINAL: password
    @Column(nullable = false)
    private String password;

    // VARIABLE ORIGINAL: profileImagePath
    private String profileImagePath = "drawable://larry";

    // --- INTEGRACIÓN DE NUEVAS FUNCIONALIDADES (SIN TOCAR LO DE ARRIBA) ---

    @Column(nullable = false)
    private Boolean enabled = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // EL ROL SEPARADO (Tal como pediste, ahora es una relación)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Relación con la imagen física (BLOB) si decides usarla, opcional
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfileImage profileImageEntity;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructor compatible con tu lógica de login original
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public void addRole(Role role) {
        this.roles.add(role);
    }
}