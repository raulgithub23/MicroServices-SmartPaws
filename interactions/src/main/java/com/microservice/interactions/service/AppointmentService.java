package com.microservice.interactions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.interactions.dto.AppointmentRequestDto;
import com.microservice.interactions.dto.AppointmentResponseDto;
import com.microservice.interactions.model.Appointment;
import com.microservice.interactions.repository.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@SuppressWarnings("null")
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Formateadores
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private AppointmentResponseDto mapToDto(Appointment entity) {
        return AppointmentResponseDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .petId(entity.getPetId())
                .doctorId(entity.getDoctorId())
                .date(entity.getDate().toString())
                .time(entity.getTime().toString())
                .notes(entity.getNotes())
                .build();
    }

    public List<AppointmentResponseDto> getAllAppointments() {
        return appointmentRepository.findAllByOrderByDateDescTimeDesc().stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<AppointmentResponseDto> getUpcomingAppointments() {
        return appointmentRepository.findByDateGreaterThanEqualOrderByDateAscTimeAsc(
                LocalDate.now(), PageRequest.of(0, 3)
        ).stream().map(this::mapToDto).toList();
    }

    public List<AppointmentResponseDto> getAppointmentsByUser(Long userId) {
        return appointmentRepository.findByUserIdOrderByDateDescTimeDesc(userId).stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<AppointmentResponseDto> getUpcomingAppointmentsByUser(Long userId) {
        return appointmentRepository.findByUserIdAndDateGreaterThanEqualOrderByDateAscTimeAsc(
                userId, LocalDate.now(), PageRequest.of(0, 3)
        ).stream().map(this::mapToDto).toList();
    }

    public List<AppointmentResponseDto> getAppointmentsByDoctorAndDate(Long doctorId, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
        return appointmentRepository.findByDoctorIdAndDate(doctorId, date).stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<AppointmentResponseDto> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorIdOrderByDateAscTimeAsc(doctorId).stream()
                .map(this::mapToDto)
                .toList();
    }

    public AppointmentResponseDto getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));
    }

    public Long createAppointment(AppointmentRequestDto dto) {
        Appointment appointment = Appointment.builder()
                .userId(dto.getUserId())
                .petId(dto.getPetId())
                .doctorId(dto.getDoctorId())
                .date(LocalDate.parse(dto.getDate(), DATE_FORMATTER))
                .time(LocalTime.parse(dto.getTime(), TIME_FORMATTER))
                .notes(dto.getNotes())
                .build();
        
        return appointmentRepository.save(appointment).getId();
    }

    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Cita no encontrada");
        }
        appointmentRepository.deleteById(id);
    }
    
    public long count() {
        return appointmentRepository.count();
    }
}