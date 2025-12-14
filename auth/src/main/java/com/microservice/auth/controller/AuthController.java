package com.microservice.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.microservice.auth.dto.ImageUploadRequest;
import com.microservice.auth.dto.LoginRequest;
import com.microservice.auth.dto.UpdateProfileRequest;
import com.microservice.auth.dto.UpdateRoleRequest;
import com.microservice.auth.dto.UserDetailDto;
import com.microservice.auth.dto.UserListDto;
import com.microservice.auth.model.ProfileImage;
import com.microservice.auth.model.Role;
import com.microservice.auth.model.User;
import com.microservice.auth.repository.RoleRepository;
import com.microservice.auth.service.AuthService;
import com.microservice.auth.service.ImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.microservice.auth.dto.PasswordResetRequest;
import com.microservice.auth.dto.ResetPasswordByEmailDto;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "API para autenticación y gestión de usuarios")
public class AuthController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private AuthService service;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario")
    public ResponseEntity<UserDetailDto> register(@RequestBody User user) {
        try {
            UserDetailDto registered = service.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(registered);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<UserDetailDto> login(@RequestBody LoginRequest req) {
        try {
            UserDetailDto user = service.login(req.getEmail(), req.getPassword());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<UserDetailDto> getUserById(@PathVariable Long id) {
        try {
            UserDetailDto user = service.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/users/detailed")
    @Operation(summary = "Obtener lista detallada de usuarios (Solo ADMIN)")
    public ResponseEntity<?> getAllUsersDetailed(@RequestParam String adminRol) {
        try {
            if (!"ADMIN".equalsIgnoreCase(adminRol)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado: Solo administradores");
            }

            List<UserDetailDto> users = service.getAllUsersDetailed();
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users/search")
    @Operation(summary = "Buscar usuarios por nombre o email (Solo ADMIN)")
    public ResponseEntity<?> searchUsers(
        @RequestParam String query,
        @RequestParam String adminRol
    ) {
        try {
            if (!"ADMIN".equalsIgnoreCase(adminRol)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado");
            }

            List<UserDetailDto> users = service.searchUsers(query);
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users/by-role")
    @Operation(summary = "Filtrar usuarios por rol (Solo ADMIN)")
    public ResponseEntity<?> getUsersByRole(
        @RequestParam String role,
        @RequestParam String adminRol
    ) {
        try {
            if (!"ADMIN".equalsIgnoreCase(adminRol)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado");
            }

            List<UserDetailDto> users = service.getUsersByRole(role);
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/user/{id}/role")
    @Operation(summary = "Actualizar rol de usuario (Solo ADMIN)")
    public ResponseEntity<?> updateUserRole(
        @PathVariable Long id,
        @RequestBody UpdateRoleRequest request,
        @RequestParam String adminRol
    ) {
        try {
            if (!"ADMIN".equalsIgnoreCase(adminRol)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado");
            }

            UserDetailDto updated = service.updateUserRole(id, request.getRol());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/user/{id}")
    @Operation(summary = "Eliminar usuario (Solo ADMIN)")
    public ResponseEntity<?> deleteUser(
        @PathVariable Long id,
        @RequestParam String adminRol
    ) {
        try {
            if (!"ADMIN".equalsIgnoreCase(adminRol)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado");
            }

            service.deleteUser(id);
            return ResponseEntity.ok("Usuario eliminado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users")
    @Operation(summary = "Obtener lista simple de usuarios (Solo ADMIN)")
    public ResponseEntity<?> getAllUsers(@RequestParam String rol) {
        try {
            if (!"ADMIN".equalsIgnoreCase(rol)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado: Solo administradores pueden ver la lista de usuarios");
            }

            List<UserListDto> users = service.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/doctor/create")
    @Operation(summary = "Crear un usuario con rol DOCTOR (Solo ADMIN)")
    public ResponseEntity<?> createDoctor(
        @RequestBody User doctorUser,
        @RequestParam String adminRol
    ) {
        try {
            if (!"ADMIN".equalsIgnoreCase(adminRol)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado: Solo administradores");
            }

            Role doctorRole = roleRepository.findByName("DOCTOR")
                .orElseThrow(() -> new RuntimeException("Rol DOCTOR no encontrado"));
            
            doctorUser.addRole(doctorRole);
            
            UserDetailDto registered = service.register(doctorUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(registered);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/user/{id}/profile")
    @Operation(summary = "Actualizar perfil de usuario (nombre y teléfono)")
    public ResponseEntity<?> updateUserProfile(
        @PathVariable Long id,
        @RequestBody UpdateProfileRequest request
    ) {
        try {
            UserDetailDto updated = service.updateUserProfile(id, request.getName(), request.getPhone());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Verificar email para recuperación de contraseña")
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetRequest request) {
        try {
            service.verifyEmailForReset(request.getEmail());
            return ResponseEntity.ok()
                .body(Map.of("success", true, "message", "Email verificado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/reset-password-by-email")
    @Operation(summary = "Restablecer contraseña usando email")
    public ResponseEntity<?> resetPasswordByEmail(@RequestBody ResetPasswordByEmailDto request) {
        try {
            service.resetPasswordByEmail(request.getEmail(), request.getNewPassword());
            return ResponseEntity.ok()
                .body(Map.of("success", true, "message", "Tu contraseña ha sido actualizada exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/user/{id}/upload-image")
    @Operation(summary = "Subir imagen de perfil (Base64)")
    public ResponseEntity<?> uploadProfileImage(
        @PathVariable Long id,
        @RequestBody ImageUploadRequest request
    ) {
        try {
            imageService.uploadProfileImage(
                id,
                request.getFileName(),
                request.getContentType(),
                request.getImageBase64()
            );
            return ResponseEntity.ok()
                .body(Map.of("success", true, "message", "Imagen de perfil actualizada exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/user/{id}/image")
    @Operation(summary = "Obtener imagen de perfil")
    public ResponseEntity<?> getProfileImage(@PathVariable Long id) {
        try {
            ProfileImage image = imageService.getProfileImage(id);
            String base64Image = imageService.convertToBase64(image.getImageData());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("fileName", image.getFileName());
            response.put("contentType", image.getContentType());
            response.put("fileSize", image.getFileSize());
            response.put("imageBase64", base64Image);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/user/{id}/image")
    @Operation(summary = "Eliminar imagen de perfil")
    public ResponseEntity<?> deleteProfileImage(@PathVariable Long id) {
        try {
            imageService.deleteProfileImage(id);
            return ResponseEntity.ok()
                .body(Map.of("success", true, "message", "Imagen de perfil eliminada exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}