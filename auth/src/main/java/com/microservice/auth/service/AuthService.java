package com.microservice.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.microservice.auth.model.User;
import com.microservice.auth.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository repo;

    public User register(User user) {

        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        user.setPassword(encoder.encode(user.getPassword()));

        return repo.save(user);
    }

    public User login(String email, String rawPassword) {

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Correo no encontrado"));

        if (!encoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return user;
    }
}
