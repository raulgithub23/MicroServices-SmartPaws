package com.microservice.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.microservice.auth.dto.LoginRequest;
import com.microservice.auth.dto.UpdateRoleRequest;
import com.microservice.auth.dto.UserDetailDto;
import com.microservice.auth.dto.UserListDto;
import com.microservice.auth.model.User;
import com.microservice.auth.service.AuthService;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            User registered = service.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(registered);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest req) {
        try {
            User user = service.login(req.getEmail(), req.getPassword());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    //Obtener usuario por ID cualquier usuario puede ver su perfil
     
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = service.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //Solo usuarios con rol ADMIN pueden acceder a la lista de usuarios (id, name, rol)

    @GetMapping("/users/detailed")
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

    //Buscar usuarios por nombre o email (solo ADMIN)
    @GetMapping("/users/search")
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

    //Filtrar usuarios por rol (solo ADMIN)
    @GetMapping("/users/by-role")
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

    //Actualizar rol de usuario (solo ADMIN)
    @PutMapping("/user/{id}/role")
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

    //Eliminar usuario (solo ADMIN)
    @DeleteMapping("/user/{id}")
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
    public ResponseEntity<?> createDoctor(
    @RequestBody User doctorUser,
    @RequestParam String adminRol
    ) {
        try {
            if (!"ADMIN".equalsIgnoreCase(adminRol)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado: Solo administradores");
            }

            // Aseguramos que el rol sea DOCTOR
            doctorUser.setRol("DOCTOR");
            
            User registered = service.register(doctorUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(registered);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}