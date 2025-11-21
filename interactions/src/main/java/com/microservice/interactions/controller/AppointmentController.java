package com.microservice.interactions.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.interactions.dto.AppointmentRequestDto;
import com.microservice.interactions.dto.AppointmentResponseDto;
import com.microservice.interactions.service.AppointmentService;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointments", description = "API para gestión de citas veterinarias")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    @Operation(summary = "Obtener historial general de citas")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Obtener próximas 3 citas generales (Dashboard)")
    public ResponseEntity<List<AppointmentResponseDto>> getUpcomingAppointments() {
        return ResponseEntity.ok(appointmentService.getUpcomingAppointments());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener historial de citas por usuario")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByUser(userId));
    }

    @GetMapping("/user/{userId}/upcoming")
    @Operation(summary = "Obtener próximas 3 citas de un usuario específico")
    public ResponseEntity<List<AppointmentResponseDto>> getUpcomingAppointmentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(appointmentService.getUpcomingAppointmentsByUser(userId));
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Obtener citas asignadas a un doctor")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }

    @GetMapping("/doctor/{doctorId}/date")
    @Operation(summary = "Obtener citas por doctor y fecha específica")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByDoctorAndDate(
            @PathVariable Long doctorId,
            @RequestParam("date") String date) { 
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorAndDate(doctorId, date));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de una cita por ID")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @PostMapping
    @Operation(summary = "Agendar una nueva cita")
    public ResponseEntity<Long> createAppointment(@Valid @RequestBody AppointmentRequestDto request) {
        Long id = appointmentService.createAppointment(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar/Eliminar una cita")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/count")
    @Operation(summary = "Total de citas registradas (para seeds/stats)")
    public ResponseEntity<Long> countAppointments() {
        return ResponseEntity.ok(appointmentService.count());
    }
}