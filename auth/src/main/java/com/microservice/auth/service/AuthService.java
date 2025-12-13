package com.microservice.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.microservice.auth.dto.UserDetailDto;
import com.microservice.auth.dto.UserListDto;
import com.microservice.auth.model.Role;
import com.microservice.auth.model.User;
import com.microservice.auth.repository.RoleRepository;
import com.microservice.auth.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está en uso");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Asignar rol por defecto si no viene
        if (user.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Error: Rol USER no encontrado."));
            user.addRole(userRole);
        }

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
            .map(user -> {
                String roleName = user.getRoles().isEmpty() ? "USER" : 
                    user.getRoles().iterator().next().getName();
                return new UserListDto(user.getId(), user.getName(), roleName);
            })
            .collect(Collectors.toList());
    }

    public List<UserDetailDto> getAllUsersDetailed() {
        return userRepository.findAll().stream()
            .map(this::mapUserToDetailDto)
            .collect(Collectors.toList());
    }

    public List<UserDetailDto> searchUsers(String query) {
        return userRepository.findAll().stream()
            .filter(user -> 
                user.getName().toLowerCase().contains(query.toLowerCase()) ||
                user.getEmail().toLowerCase().contains(query.toLowerCase())
            )
            .map(this::mapUserToDetailDto)
            .collect(Collectors.toList());
    }

    public List<UserDetailDto> getUsersByRole(String roleName) {
        return userRepository.findAll().stream()
            .filter(user -> hasRole(user, roleName))
            .map(this::mapUserToDetailDto)
            .collect(Collectors.toList());
    }

    private UserDetailDto mapUserToDetailDto(User user) {
        String roleName = user.getRoles().isEmpty() ? "USER" : 
            user.getRoles().iterator().next().getName();
        
        // Verificar si tiene imagen de perfil
        String imageIndicator = user.getProfileImageEntity() != null ? "HAS_IMAGE" : null;
            
        return new UserDetailDto(
            user.getId(),
            roleName,
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            imageIndicator // Solo indicamos si tiene imagen, no la ruta
        );
    }

    private boolean hasRole(User user, String roleName) {
        return user.getRoles().stream()
            .anyMatch(role -> role.getName().equals(roleName));
    }

    @Transactional
    public User updateUserRole(Long userId, String newRoleName) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Role newRole = roleRepository.findByName(newRoleName)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        
        user.getRoles().clear();
        user.addRole(newRole);
        
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

    @Transactional
    public void verifyEmailForReset(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("No existe un usuario con ese email"));
    }

    @Transactional
    public void resetPasswordByEmail(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("No existe un usuario con ese email"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}