package com.microservice.interactions.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.microservice.interactions.dto.AppointmentRequestDto;
import com.microservice.interactions.dto.AppointmentResponseDto;
import com.microservice.interactions.service.AppointmentService;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    @MockBean
    private AppointmentService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private AppointmentResponseDto responseDto;
    private AppointmentRequestDto requestDto;
    private List<AppointmentResponseDto> responseList;

    @BeforeEach
    void setUp() {
        responseDto = AppointmentResponseDto.builder()
                .id(1L)
                .userId(100L)
                .petId(200L)
                .doctorId(300L)
                .date("2024-12-25")
                .time("10:30")
                .notes("Checkup")
                .build();

        responseList = Arrays.asList(responseDto);

        requestDto = new AppointmentRequestDto();
        requestDto.setUserId(100L);
        requestDto.setPetId(200L);
        requestDto.setDoctorId(300L);
        requestDto.setDate("2024-12-25");
        requestDto.setTime("10:30");
        requestDto.setNotes("Checkup");
    }

    // GET - /api/appointments
    @Test
    void testGetAllAppointments_JsonAndOK() throws Exception {
        when(service.getAllAppointments()).thenReturn(responseList);

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].notes").value("Checkup"));
    }

    // GET - /api/appointments/upcoming
    @Test
    void testGetUpcomingAppointments_JsonAndOK() throws Exception {
        when(service.getUpcomingAppointments()).thenReturn(responseList);

        mockMvc.perform(get("/api/appointments/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    // GET - /api/appointments/user/{userId}
    @Test
    void testGetAppointmentsByUser_JsonAndOK() throws Exception {
        when(service.getAppointmentsByUser(100L)).thenReturn(responseList);

        mockMvc.perform(get("/api/appointments/user/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(100L));
    }

    // GET - /api/appointments/user/{userId}/upcoming
    @Test
    void testGetUpcomingAppointmentsByUser_JsonAndOK() throws Exception {
        when(service.getUpcomingAppointmentsByUser(100L)).thenReturn(responseList);

        mockMvc.perform(get("/api/appointments/user/100/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    // GET - /api/appointments/doctor/{doctorId}
    @Test
    void testGetAppointmentsByDoctor_JsonAndOK() throws Exception {
        when(service.getAppointmentsByDoctor(300L)).thenReturn(responseList);

        mockMvc.perform(get("/api/appointments/doctor/300"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId").value(300L));
    }

    // GET - /api/appointments/doctor/{doctorId}/date?date=...
    @Test
    void testGetAppointmentsByDoctorAndDate_JsonAndOK() throws Exception {
        String dateParam = "2024-12-25";
        when(service.getAppointmentsByDoctorAndDate(300L, dateParam)).thenReturn(responseList);

        mockMvc.perform(get("/api/appointments/doctor/300/date")
                .param("date", dateParam))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value(dateParam));
    }

    // GET - /api/appointments/{id}
    @Test
    void testGetAppointmentById_JsonAndOK() throws Exception {
        when(service.getAppointmentById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    // POST - /api/appointments
    @Test
    void testCreateAppointment_JsonAndCreated() throws Exception {
        when(service.createAppointment(any(AppointmentRequestDto.class))).thenReturn(1L);

        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$").value(1L));
    }

    // DELETE - /api/appointments/{id}
    @Test
    void testDeleteAppointment_NoContent() throws Exception {

        mockMvc.perform(delete("/api/appointments/1"))
                .andExpect(status().isNoContent());
    }

    // GET - /api/appointments/count
    @Test
    void testCountAppointments_JsonAndOK() throws Exception {
        when(service.count()).thenReturn(50L);

        mockMvc.perform(get("/api/appointments/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(50L));
    }
}
