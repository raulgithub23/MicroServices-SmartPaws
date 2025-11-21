package com.microservice.doctor.controller;

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
import com.microservice.doctor.dto.CreateDoctorRequest;
import com.microservice.doctor.dto.DoctorDto;
import com.microservice.doctor.dto.ScheduleDto;
import com.microservice.doctor.dto.UpdateSchedulesRequest;
import com.microservice.doctor.service.DoctorService;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

    @MockBean
    private DoctorService doctorService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private DoctorDto doctorDto;
    private List<DoctorDto> doctorListDto;
    private CreateDoctorRequest createRequest;
    private UpdateSchedulesRequest updateSchedulesRequest;
    private ScheduleDto scheduleDto;

    @BeforeEach
    void setUp() {
        // Cambiado a String en lugar de LocalTime
        scheduleDto = new ScheduleDto(1L, "LUNES", "09:00", "17:00");

        doctorDto = new DoctorDto(
            1L,
            "Dr. Juan Pérez",
            "Veterinaria General",
            "juan.perez@vet.com",
            "123456789",
            Arrays.asList(scheduleDto)
        );

        doctorListDto = Arrays.asList(doctorDto);

        createRequest = new CreateDoctorRequest();
        createRequest.setName("Dr. Juan Pérez");
        createRequest.setSpecialty("Veterinaria General");
        createRequest.setEmail("juan.perez@vet.com");
        createRequest.setPhone("123456789");
        createRequest.setSchedules(Arrays.asList(scheduleDto));

        updateSchedulesRequest = new UpdateSchedulesRequest();
        updateSchedulesRequest.setSchedules(Arrays.asList(scheduleDto));
    }

    // GET - /doctors
    @Test
    void testGetAllDoctors_JsonAndOK() throws Exception {
        when(doctorService.getAllDoctors()).thenReturn(doctorListDto);

        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Dr. Juan Pérez"))
                .andExpect(jsonPath("$[0].specialty").value("Veterinaria General"));
    }

    // GET - /doctors/{id}
    @Test
    void testGetDoctorById_JsonAndOK() throws Exception {
        when(doctorService.getDoctorById(1L)).thenReturn(doctorDto);

        mockMvc.perform(get("/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Dr. Juan Pérez"));
    }

    @Test
    void testGetDoctorById_NotFound() throws Exception {
        when(doctorService.getDoctorById(999L))
                .thenThrow(new RuntimeException("Doctor no encontrado"));

        mockMvc.perform(get("/doctors/999"))
                .andExpect(status().isNotFound());
    }

    // GET - /doctors/by-email?email=...
    @Test
    void testGetDoctorByEmail_JsonAndOK() throws Exception {
        when(doctorService.getDoctorByEmail("juan.perez@vet.com")).thenReturn(doctorDto);

        mockMvc.perform(get("/doctors/by-email")
                .param("email", "juan.perez@vet.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan.perez@vet.com"));
    }

    @Test
    void testGetDoctorByEmail_NotFound() throws Exception {
        when(doctorService.getDoctorByEmail("noexiste@vet.com"))
                .thenThrow(new RuntimeException("Doctor no encontrado"));

        mockMvc.perform(get("/doctors/by-email")
                .param("email", "noexiste@vet.com"))
                .andExpect(status().isNotFound());
    }

    // POST - /doctors
    @Test
    void testCreateDoctor_JsonAndCreated() throws Exception {
        when(doctorService.createDoctor(any(CreateDoctorRequest.class))).thenReturn(doctorDto);

        mockMvc.perform(post("/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Dr. Juan Pérez"));
    }

    @Test
    void testCreateDoctor_EmailDuplicado() throws Exception {
        when(doctorService.createDoctor(any(CreateDoctorRequest.class)))
                .thenThrow(new RuntimeException("Ya existe un doctor con ese email"));

        mockMvc.perform(post("/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
    }

    // PUT - /doctors/{id}/schedules
    @Test
    void testUpdateSchedules_JsonAndOK() throws Exception {
        when(doctorService.updateSchedules(eq(1L), any())).thenReturn(doctorDto);

        mockMvc.perform(put("/doctors/1/schedules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateSchedulesRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testUpdateSchedules_DoctorNoExistente() throws Exception {
        when(doctorService.updateSchedules(eq(999L), any()))
                .thenThrow(new RuntimeException("Doctor no encontrado"));

        mockMvc.perform(put("/doctors/999/schedules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateSchedulesRequest)))
                .andExpect(status().isBadRequest());
    }

    // DELETE - /doctors/{id}
    @Test
    void testDeleteDoctor_OK() throws Exception {
        doNothing().when(doctorService).deleteDoctor(1L);

        mockMvc.perform(delete("/doctors/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteDoctor_NoExistente() throws Exception {
        doThrow(new RuntimeException("Doctor no encontrado"))
                .when(doctorService).deleteDoctor(999L);

        mockMvc.perform(delete("/doctors/999"))
                .andExpect(status().isBadRequest());
    }

    // GET - /doctors/count
    @Test
    void testCountDoctors_JsonAndOK() throws Exception {
        when(doctorService.countDoctors()).thenReturn(10L);

        mockMvc.perform(get("/doctors/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(10L));
    }
}