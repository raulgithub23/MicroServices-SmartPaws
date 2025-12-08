package com.microservice.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.Collections;
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
    private List<User> userListTest;

    @BeforeEach
    void setUp() {
        // Inicializar Roles
        userRole = new Role("USER", "Usuario estándar");
        userRole.setId(1L);
        
        adminRole = new Role("ADMIN", "Administrador");
        adminRole.setId(2L);

        // Inicializar Usuario
        userTest = new User();
        userTest.setId(1L);
        userTest.setName("Juan Pérez");
        userTest.setEmail("juan@test.com");
        userTest.setPhone("123456789");
        userTest.setPassword("hashedPassword123");
        userTest.setRoles(new HashSet<>(Collections.singletonList(userRole)));

        userListTest = Arrays.asList(userTest);
    }

    // --- REGISTER TESTS ---

    @Test
    void testRegister_Exitoso_AsignaRolPorDefecto() {
        User newUser = new User();
        newUser.setEmail("nuevo@test.com");
        newUser.setPassword("password123");
        newUser.setName("Nuevo Usuario");
        // No asignamos roles para probar que asigna USER por defecto

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        
        // Simulamos que al guardar retorna el usuario con ID y Rol
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        User result = authService.register(newUser);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getPassword()).isNull(); // Debe limpiarse por seguridad
        assertThat(result.getRoles()).contains(userRole);
        
        verify(passwordEncoder).encode("password123");
        verify(roleRepository).findByName("USER");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegister_EmailDuplicado() {
        User newUser = new User();
        newUser.setEmail("juan@test.com");

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(userTest));

        assertThatThrownBy(() -> authService.register(newUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El email ya está en uso");
    }

    // --- LOGIN TESTS ---

    @Test
    void testLogin_Exitoso() {
        when(userRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(userTest));
        when(passwordEncoder.matches("password123", "hashedPassword123")).thenReturn(true);

        User result = authService.login("juan@test.com", "password123");

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("juan@test.com");
        assertThat(result.getPassword()).isNull();
    }

    @Test
    void testLogin_CredencialesInvalidas_EmailNoExiste() {
        when(userRepository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login("noexiste@test.com", "password123"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Credenciales inválidas");
    }

    @Test
    void testLogin_CredencialesInvalidas_PasswordIncorrecta() {
        when(userRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(userTest));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword123")).thenReturn(false);

        assertThatThrownBy(() -> authService.login("juan@test.com", "wrongPassword"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Credenciales inválidas");
    }

    // --- GET & SEARCH TESTS ---

    @Test
    void testGetUserById_Existente() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userTest));

        User result = authService.getUserById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPassword()).isNull();
    }

    @Test
    void testGetUserById_NoExistente() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.getUserById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuario no encontrado");
    }

    @Test
    void testGetAllUsers_Exitoso() {
        when(userRepository.findAll()).thenReturn(userListTest);

        List<UserListDto> result = authService.getAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("Juan Pérez");
        assertThat(result.get(0).getRol()).isEqualTo("USER");
    }

    @Test
    void testGetAllUsersDetailed_Exitoso() {
        when(userRepository.findAll()).thenReturn(userListTest);

        List<UserDetailDto> result = authService.getAllUsersDetailed();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("juan@test.com");
        assertThat(result.get(0).getPhone()).isEqualTo("123456789");
    }

    @Test
    void testSearchUsers_PorNombre() {
        when(userRepository.findAll()).thenReturn(userListTest);

        // El servicio filtra en memoria usando stream(), por eso mockeamos findAll
        List<UserDetailDto> result = authService.searchUsers("juan");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).contains("Juan");
    }

    @Test
    void testSearchUsers_SinResultados() {
        when(userRepository.findAll()).thenReturn(userListTest);

        List<UserDetailDto> result = authService.searchUsers("Maria");

        assertThat(result).isEmpty();
    }

    @Test
    void testGetUsersByRole_Exitoso() {
        when(userRepository.findAll()).thenReturn(userListTest);

        // El servicio filtra en memoria verificando el Set<Role>
        List<UserDetailDto> result = authService.getUsersByRole("USER");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRol()).isEqualTo("USER");
    }

    // --- UPDATE & DELETE TESTS ---

    @Test
    void testUpdateUserRole_Exitoso() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userTest));
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(adminRole));
        
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = authService.updateUserRole(1L, "ADMIN");

        // Verificamos que ahora tenga el rol ADMIN
        assertThat(result.getRoles()).contains(adminRole);
        assertThat(result.getRoles()).doesNotContain(userRole); // Asumiendo que limpia roles previos
        assertThat(result.getPassword()).isNull();
    }

    @Test
    void testUpdateUserProfile_Exitoso() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userTest));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = authService.updateUserProfile(1L, "Juan Actualizado", "987654321");

        assertThat(result.getName()).isEqualTo("Juan Actualizado");
        assertThat(result.getPhone()).isEqualTo("987654321");
        verify(userRepository).save(userTest);
    }

    @Test
    void testDeleteUser_Exitoso() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        authService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUser_NoExistente() {
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> authService.deleteUser(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuario no encontrado");
    }

    // --- PASSWORD RESET TESTS (New Logic) ---

    @Test
    void testVerifyEmailForReset_Exitoso() {
        when(userRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(userTest));

        // No debe lanzar excepción
        authService.verifyEmailForReset("juan@test.com");

        verify(userRepository).findByEmail("juan@test.com");
    }

    @Test
    void testVerifyEmailForReset_NoExiste() {
        when(userRepository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.verifyEmailForReset("noexiste@test.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No existe un usuario con ese email");
    }

    @Test
    void testResetPasswordByEmail_Exitoso() {
        when(userRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(userTest));
        when(passwordEncoder.encode("newPassword123")).thenReturn("encodedNewPassword");

        authService.resetPasswordByEmail("juan@test.com", "newPassword123");

        verify(userRepository).save(userTest);
        verify(passwordEncoder).encode("newPassword123");
        assertThat(userTest.getPassword()).isEqualTo("encodedNewPassword");
    }
}