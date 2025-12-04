package com.microservice.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.microservice.auth.dto.LoginRequest;
import com.microservice.auth.dto.UpdateImageRequest;
import com.microservice.auth.dto.UpdateProfileRequest;
import com.microservice.auth.dto.UpdateRoleRequest;
import com.microservice.auth.dto.UserDetailDto;
import com.microservice.auth.dto.UserListDto;
import com.microservice.auth.model.User;
import com.microservice.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "API para autenticación y gestión de usuarios")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            User registered = service.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(registered);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<User> login(@RequestBody LoginRequest req) {
        try {
            User user = service.login(req.getEmail(), req.getPassword());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = service.getUserById(id);
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

            User updated = service.updateUserRole(id, request.getRol());
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

            doctorUser.setRol("DOCTOR");
            
            User registered = service.register(doctorUser);
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
            User updated = service.updateUserProfile(id, request.getName(), request.getPhone());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/user/{id}/image")
    @Operation(summary = "Actualizar imagen de perfil de usuario")
    public ResponseEntity<?> updateProfileImage(
        @PathVariable Long id,
        @RequestBody UpdateImageRequest request
    ) {
        try {
            User updated = service.updateProfileImage(id, request.getImagePath());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}