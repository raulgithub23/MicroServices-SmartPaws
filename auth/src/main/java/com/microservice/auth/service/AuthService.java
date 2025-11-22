package com.microservice.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.microservice.auth.dto.UserDetailDto;
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

    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está en uso");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        saved.setPassword(null);
        return saved;
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        user.setPassword(null);
        return user;
    }

    public User getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        user.setPassword(null);
        return user;
    }

    public List<UserListDto> getAllUsers() {
        return userRepository.findAll().stream()
            .map(user -> new UserListDto(user.getId(), user.getName(), user.getRol()))
            .collect(Collectors.toList());
    }

    public List<UserDetailDto> getAllUsersDetailed() {
        return userRepository.findAll().stream()
            .map(user -> new UserDetailDto(
                user.getId(),
                user.getRol(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getProfileImagePath()
            ))
            .collect(Collectors.toList());
    }

    public List<UserDetailDto> searchUsers(String query) {
        return userRepository.findAll().stream()
            .filter(user -> 
                user.getName().toLowerCase().contains(query.toLowerCase()) ||
                user.getEmail().toLowerCase().contains(query.toLowerCase())
            )
            .map(user -> new UserDetailDto(
                user.getId(),
                user.getRol(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getProfileImagePath()
            ))
            .collect(Collectors.toList());
    }

    public List<UserDetailDto> getUsersByRole(String rol) {
        return userRepository.findAll().stream()
            .filter(user -> user.getRol().equalsIgnoreCase(rol))
            .map(user -> new UserDetailDto(
                user.getId(),
                user.getRol(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getProfileImagePath()
            ))
            .collect(Collectors.toList());
    }

    public User updateUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        user.setRol(newRole);
        User updated = userRepository.save(user);
        updated.setPassword(null);
        return updated;
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        userRepository.deleteById(userId);
    }
    
    public User updateUserProfile(Long userId, String name, String phone) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        user.setName(name);
        user.setPhone(phone);
        
        User updated = userRepository.save(user);
        updated.setPassword(null);
        return updated;
    }

    public User updateProfileImage(Long userId, String imagePath) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        user.setProfileImagePath(imagePath);
        
        User updated = userRepository.save(user);
        updated.setPassword(null);
        return updated;
    }

}