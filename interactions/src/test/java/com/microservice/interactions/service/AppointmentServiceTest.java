package com.microservice.interactions.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.microservice.interactions.dto.AppointmentRequestDto;
import com.microservice.interactions.dto.AppointmentResponseDto;
import com.microservice.interactions.model.Appointment;
import com.microservice.interactions.repository.AppointmentRepository;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService service;

    private Appointment appointmentTest;
    private List<Appointment> appointmentListTest;
    private AppointmentRequestDto requestDto;

    @BeforeEach
    void setUp() {
        appointmentTest = Appointment.builder()
                .id(1L)
                .userId(100L)
                .petId(200L)
                .doctorId(300L)
                .date(LocalDate.of(2024, 12, 25))
                .time(LocalTime.of(10, 30))
                .notes("Vacunación anual")
                .build();

        appointmentListTest = Arrays.asList(appointmentTest);

        requestDto = new AppointmentRequestDto();
        requestDto.setUserId(100L);
        requestDto.setPetId(200L);
        requestDto.setDoctorId(300L);
        requestDto.setDate("2024-12-25");
        requestDto.setTime("10:30");
        requestDto.setNotes("Vacunación anual");
    }

    @Test
    void testGetAllAppointments_Exitoso() {
        when(appointmentRepository.findAllByOrderByDateDescTimeDesc()).thenReturn(appointmentListTest);

        List<AppointmentResponseDto> result = service.getAllAppointments();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void testGetUpcomingAppointments_Exitoso() {
        // Usamos any() para LocalDate y Pageable para evitar fragilidad con LocalDate.now()
        when(appointmentRepository.findByDateGreaterThanEqualOrderByDateAscTimeAsc(any(LocalDate.class), any(Pageable.class)))
                .thenReturn(appointmentListTest);

        List<AppointmentResponseDto> result = service.getUpcomingAppointments();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getNotes()).isEqualTo("Vacunación anual");
    }

    @Test
    void testGetAppointmentsByUser_Exitoso() {
        Long userId = 100L;
        when(appointmentRepository.findByUserIdOrderByDateDescTimeDesc(userId)).thenReturn(appointmentListTest);

        List<AppointmentResponseDto> result = service.getAppointmentsByUser(userId);

        assertThat(result.get(0).getUserId()).isEqualTo(userId);
    }

    @Test
    void testGetUpcomingAppointmentsByUser_Exitoso() {
        Long userId = 100L;
        when(appointmentRepository.findByUserIdAndDateGreaterThanEqualOrderByDateAscTimeAsc(
                eq(userId), any(LocalDate.class), any(PageRequest.class)))
                .thenReturn(appointmentListTest);

        List<AppointmentResponseDto> result = service.getUpcomingAppointmentsByUser(userId);

        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAppointmentsByDoctorAndDate_Exitoso() {
        Long doctorId = 300L;
        String dateStr = "2024-12-25";
        LocalDate date = LocalDate.of(2024, 12, 25);

        when(appointmentRepository.findByDoctorIdAndDate(doctorId, date)).thenReturn(appointmentListTest);

        List<AppointmentResponseDto> result = service.getAppointmentsByDoctorAndDate(doctorId, dateStr);

        assertThat(result.get(0).getDoctorId()).isEqualTo(doctorId);
        assertThat(result.get(0).getDate()).isEqualTo(dateStr);
    }

    @Test
    void testGetAppointmentsByDoctor_Exitoso() {
        Long doctorId = 300L;
        when(appointmentRepository.findByDoctorIdOrderByDateAscTimeAsc(doctorId)).thenReturn(appointmentListTest);

        List<AppointmentResponseDto> result = service.getAppointmentsByDoctor(doctorId);

        assertThat(result).isNotEmpty();
    }

    @Test
    void testGetAppointmentById_Existente() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointmentTest));

        AppointmentResponseDto result = service.getAppointmentById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNotes()).isEqualTo("Vacunación anual");
    }

    @Test
    void testCreateAppointment_Exitoso() {
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointmentTest);

        Long resultId = service.createAppointment(requestDto);

        assertThat(resultId).isEqualTo(1L);
    }

    @Test
    void testDeleteAppointment_Exitoso() {
        when(appointmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(appointmentRepository).deleteById(1L);

        service.deleteAppointment(1L);

    }

    @Test
    void testCount_Exitoso() {
        when(appointmentRepository.count()).thenReturn(10L);

        long result = service.count();

        assertThat(result).isEqualTo(10L);
    }
}
