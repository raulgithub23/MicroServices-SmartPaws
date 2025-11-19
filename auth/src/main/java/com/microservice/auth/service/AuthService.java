package com.microservice.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.microservice.auth.dto.UserListDto;
import com.microservice.auth.model.User;
import com.microservice.auth.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Registro de usuario

    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está en uso");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        saved.setPassword(null);
        return saved;
    }

    //Login de usuario
     
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        user.setPassword(null);
        return user;
    }

    //Obtener usuario por ID
     
    public User getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // No devolver la contraseña
        user.setPassword(null);
        return user;
    }

    //Obtener lista de usuarios (solo nombres), Solo devuelve: id, name, rol
     
    public List<UserListDto> getAllUsers() {
        return userRepository.findAll().stream()
            .map(user -> new UserListDto(user.getId(), user.getName(), user.getRol()))
            .collect(Collectors.toList());
    }
}
