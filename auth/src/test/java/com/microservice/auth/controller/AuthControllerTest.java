package com.microservice.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.auth.dto.LoginRequest;
import com.microservice.auth.dto.UpdateRoleRequest;
import com.microservice.auth.dto.UserDetailDto;
import com.microservice.auth.dto.UserListDto;
import com.microservice.auth.model.Role;
import com.microservice.auth.model.User;
import com.microservice.auth.repository.RoleRepository;
import com.microservice.auth.service.AuthService;
import com.microservice.auth.service.ImageService;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @MockBean
    private AuthService authService;

    @MockBean
    private ImageService imageService;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User userTest;
    private Role userRole;
    private Role doctorRole;
    private LoginRequest loginRequest;
    private UpdateRoleRequest updateRoleRequest;
    private List<UserListDto> userListDto;
    private List<UserDetailDto> userDetailDtoList;

    @BeforeEach
    void setUp() {
        userRole = new Role("USER", "Usuario estándar");
        doctorRole = new Role("DOCTOR", "Doctor veterinario");

        userTest = new User();
        userTest.setId(1L);
        userTest.setName("Juan Pérez");
        userTest.setEmail("juan@test.com");
        userTest.setPassword(null);
        userTest.setPhone("123456789");
        userTest.addRole(userRole);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("juan@test.com");
        loginRequest.setPassword("password123");

        updateRoleRequest = new UpdateRoleRequest();
        updateRoleRequest.setRol("ADMIN");

        userListDto = Arrays.asList(new UserListDto(1L, "Juan Pérez", "USER"));

        userDetailDtoList = Arrays.asList(
            new UserDetailDto(1L, "USER", "Juan Pérez", "juan@test.com", "123456789", null)
        );
    }

    @Test
    void testRegister_JsonAndCreated() throws Exception {
        User newUser = new User();
        newUser.setEmail("nuevo@test.com");
        newUser.setPassword("password123");
        newUser.setName("Nuevo Usuario");

        when(authService.register(any(User.class))).thenReturn(userTest);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Juan Pérez"));
    }

    @Test
    void testRegister_EmailDuplicado() throws Exception {
        User newUser = new User();
        newUser.setEmail("duplicado@test.com");

        when(authService.register(any(User.class)))
                .thenThrow(new RuntimeException("El email ya está en uso"));

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_JsonAndOK() throws Exception {
        when(authService.login("juan@test.com", "password123")).thenReturn(userTest);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@test.com"));
    }

    @Test
    void testLogin_CredencialesInvalidas() throws Exception {
        when(authService.login("juan@test.com", "wrongPassword"))
                .thenThrow(new RuntimeException("Credenciales inválidas"));

        LoginRequest wrongLogin = new LoginRequest();
        wrongLogin.setEmail("juan@test.com");
        wrongLogin.setPassword("wrongPassword");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrongLogin)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetUserById_JsonAndOK() throws Exception {
        when(authService.getUserById(1L)).thenReturn(userTest);

        mockMvc.perform(get("/auth/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Juan Pérez"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(authService.getUserById(999L))
                .thenThrow(new RuntimeException("Usuario no encontrado"));

        mockMvc.perform(get("/auth/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllUsers_JsonAndOK() throws Exception {
        when(authService.getAllUsers()).thenReturn(userListDto);

        mockMvc.perform(get("/auth/users")
                .param("rol", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Juan Pérez"));
    }

    @Test
    void testGetAllUsers_AccesoDenegado() throws Exception {
        mockMvc.perform(get("/auth/users")
                .param("rol", "USER"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetAllUsersDetailed_JsonAndOK() throws Exception {
        when(authService.getAllUsersDetailed()).thenReturn(userDetailDtoList);

        mockMvc.perform(get("/auth/users/detailed")
                .param("adminRol", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("juan@test.com"));
    }

    @Test
    void testGetAllUsersDetailed_AccesoDenegado() throws Exception {
        mockMvc.perform(get("/auth/users/detailed")
                .param("adminRol", "USER"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testSearchUsers_JsonAndOK() throws Exception {
        when(authService.searchUsers("juan")).thenReturn(userDetailDtoList);

        mockMvc.perform(get("/auth/users/search")
                .param("query", "juan")
                .param("adminRol", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Juan Pérez"));
    }

    @Test
    void testSearchUsers_AccesoDenegado() throws Exception {
        mockMvc.perform(get("/auth/users/search")
                .param("query", "juan")
                .param("adminRol", "USER"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetUsersByRole_JsonAndOK() throws Exception {
        when(authService.getUsersByRole("USER")).thenReturn(userDetailDtoList);

        mockMvc.perform(get("/auth/users/by-role")
                .param("role", "USER")
                .param("adminRol", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rol").value("USER"));
    }

    @Test
    void testGetUsersByRole_AccesoDenegado() throws Exception {
        mockMvc.perform(get("/auth/users/by-role")
                .param("role", "USER")
                .param("adminRol", "USER"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateUserRole_JsonAndOK() throws Exception {
        when(authService.updateUserRole(eq(1L), eq("ADMIN"))).thenReturn(userTest);

        mockMvc.perform(put("/auth/user/1/role")
                .param("adminRol", "ADMIN")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRoleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testUpdateUserRole_AccesoDenegado() throws Exception {
        mockMvc.perform(put("/auth/user/1/role")
                .param("adminRol", "USER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRoleRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteUser_OK() throws Exception {
        doNothing().when(authService).deleteUser(1L);

        mockMvc.perform(delete("/auth/user/1")
                .param("adminRol", "ADMIN"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUser_AccesoDenegado() throws Exception {
        mockMvc.perform(delete("/auth/user/1")
                .param("adminRol", "USER"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteUser_NoExistente() throws Exception {
        doThrow(new RuntimeException("Usuario no encontrado"))
                .when(authService).deleteUser(999L);

        mockMvc.perform(delete("/auth/user/999")
                .param("adminRol", "ADMIN"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateDoctor_JsonAndCreated() throws Exception {
        User doctorUser = new User();
        doctorUser.setEmail("doctor@test.com");
        doctorUser.setName("Dr. Carlos");
        doctorUser.addRole(doctorRole);

        when(roleRepository.findByName("DOCTOR")).thenReturn(java.util.Optional.of(doctorRole));
        when(authService.register(any(User.class))).thenReturn(doctorUser);

        mockMvc.perform(post("/auth/doctor/create")
                .param("adminRol", "ADMIN")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctorUser)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateDoctor_AccesoDenegado() throws Exception {
        User doctorUser = new User();
        doctorUser.setEmail("doctor@test.com");

        mockMvc.perform(post("/auth/doctor/create")
                .param("adminRol", "USER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctorUser)))
                .andExpect(status().isForbidden());
    }
}