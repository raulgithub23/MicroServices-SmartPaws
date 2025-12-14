package com.microservice.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.microservice.auth.dto.UserDetailDto;
import com.microservice.auth.dto.UserListDto;
import com.microservice.auth.model.Role;
import com.microservice.auth.model.User;
import com.microservice.auth.repository.RoleRepository;
import com.microservice.auth.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AuthService authService;

    private User userTest;
    private Role userRole;
    private Role adminRole;
    private Role doctorRole;

    @BeforeEach
    void setUp() {
        userRole = new Role(1L, "USER", "Usuario estándar", new HashSet<>());
        adminRole = new Role(2L, "ADMIN", "Administrador", new HashSet<>());
        doctorRole = new Role(3L, "DOCTOR", "Doctor veterinario", new HashSet<>());

        userTest = new User();
        userTest.setId(1L);
        userTest.setName("Juan Pérez");
        userTest.setEmail("juan@test.com");
        userTest.setPassword("password123");
        userTest.setPhone("123456789");
        userTest.addRole(userRole);
    }

    @Test
    void testRegister_Exitoso() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(userRepository.save(any(User.class))).thenReturn(userTest);

        User newUser = new User();
        newUser.setEmail("nuevo@test.com");
        newUser.setPassword("password123");
        newUser.setName("Nuevo Usuario");
        newUser.setPhone("987654321");

        UserDetailDto result = authService.register(newUser);

        assertNotNull(result);
        assertEquals("Juan Pérez", result.getName());
        assertEquals("juan@test.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_EmailDuplicado() {
        when(userRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(userTest));

        User newUser = new User();
        newUser.setEmail("juan@test.com");
        newUser.setPassword("password123");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(newUser);
        });

        assertEquals("El email ya está en uso", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_ConRolEspecifico() {
        User doctorUser = new User();
        doctorUser.setEmail("doctor@test.com");
        doctorUser.setPassword("password123");
        doctorUser.setName("Dr. Carlos");
        doctorUser.setPhone("987654321");
        doctorUser.addRole(doctorRole);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        UserDetailDto result = authService.register(doctorUser);

        assertNotNull(result);
        verify(roleRepository, never()).findByName("USER");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testLogin_Exitoso() {
        when(userRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(userTest));
        when(passwordEncoder.matches("password123", "password123")).thenReturn(true);

        UserDetailDto result = authService.login("juan@test.com", "password123");

        assertNotNull(result);
        assertEquals("juan@test.com", result.getEmail());
        assertEquals("Juan Pérez", result.getName());
    }

    @Test
    void testLogin_EmailNoExiste() {
        when(userRepository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login("noexiste@test.com", "password123");
        });

        assertEquals("Credenciales inválidas", exception.getMessage());
    }

    @Test
    void testLogin_PasswordIncorrecta() {
        when(userRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(userTest));
        when(passwordEncoder.matches("wrongPassword", "password123")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login("juan@test.com", "wrongPassword");
        });

        assertEquals("Credenciales inválidas", exception.getMessage());
    }

    @Test
    void testGetUserById_Exitoso() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userTest));

        UserDetailDto result = authService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Juan Pérez", result.getName());
        assertEquals("juan@test.com", result.getEmail());
    }

    @Test
    void testGetUserById_NoEncontrado() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.getUserById(999L);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void testGetAllUsers() {
        User user2 = new User();
        user2.setId(2L);
        user2.setName("María García");
        user2.setEmail("maria@test.com");
        user2.addRole(adminRole);

        when(userRepository.findAll()).thenReturn(Arrays.asList(userTest, user2));

        List<UserListDto> result = authService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Juan Pérez", result.get(0).getName());
        assertEquals("USER", result.get(0).getRol());
    }

    @Test
    void testGetAllUsersDetailed() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(userTest));

        List<UserDetailDto> result = authService.getAllUsersDetailed();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan Pérez", result.get(0).getName());
        assertEquals("juan@test.com", result.get(0).getEmail());
    }

    @Test
    void testSearchUsers_PorNombre() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(userTest));

        List<UserDetailDto> result = authService.searchUsers("juan");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan Pérez", result.get(0).getName());
    }

    @Test
    void testSearchUsers_PorEmail() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(userTest));

        List<UserDetailDto> result = authService.searchUsers("juan@test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("juan@test.com", result.get(0).getEmail());
    }

    @Test
    void testSearchUsers_SinResultados() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(userTest));

        List<UserDetailDto> result = authService.searchUsers("noexiste");

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetUsersByRole() {
        User adminUser = new User();
        adminUser.setId(2L);
        adminUser.setName("Admin User");
        adminUser.setEmail("admin@test.com");
        adminUser.addRole(adminRole);

        when(userRepository.findAll()).thenReturn(Arrays.asList(userTest, adminUser));

        List<UserDetailDto> result = authService.getUsersByRole("USER");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan Pérez", result.get(0).getName());
    }

    @Test
    void testUpdateUserRole_Exitoso() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userTest));
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(adminRole));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        UserDetailDto result = authService.updateUserRole(1L, "ADMIN");

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserRole_UsuarioNoEncontrado() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.updateUserRole(999L, "ADMIN");
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void testUpdateUserRole_RolNoEncontrado() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userTest));
        when(roleRepository.findByName("INVALID_ROLE")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.updateUserRole(1L, "INVALID_ROLE");
        });

        assertEquals("Rol no encontrado", exception.getMessage());
    }

    @Test
    void testDeleteUser_Exitoso() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> authService.deleteUser(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NoEncontrado() {
        when(userRepository.existsById(999L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.deleteUser(999L);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void testUpdateUserProfile_Exitoso() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userTest));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        UserDetailDto result = authService.updateUserProfile(1L, "Juan Pérez Actualizado", "987654321");

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserProfile_UsuarioNoEncontrado() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.updateUserProfile(999L, "Nuevo Nombre", "123456789");
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void testVerifyEmailForReset_EmailExiste() {
        when(userRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(userTest));

        assertDoesNotThrow(() -> authService.verifyEmailForReset("juan@test.com"));
    }

    @Test
    void testVerifyEmailForReset_EmailNoExiste() {
        when(userRepository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.verifyEmailForReset("noexiste@test.com");
        });

        assertEquals("No existe un usuario con ese email", exception.getMessage());
    }

    @Test
    void testResetPasswordByEmail_Exitoso() {
        when(userRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(userTest));
        when(passwordEncoder.encode("newPassword123")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        assertDoesNotThrow(() -> authService.resetPasswordByEmail("juan@test.com", "newPassword123"));
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("newPassword123");
    }

    @Test
    void testResetPasswordByEmail_EmailNoExiste() {
        when(userRepository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.resetPasswordByEmail("noexiste@test.com", "newPassword123");
        });

        assertEquals("No existe un usuario con ese email", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}