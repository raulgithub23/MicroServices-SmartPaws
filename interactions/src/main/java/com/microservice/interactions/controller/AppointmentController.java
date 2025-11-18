package com.microservice.interactions.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.microservice.interactions.dto.AppointmentWithDetailsDTO;
import com.microservice.interactions.model.Appointment;
import com.microservice.interactions.service.AppointmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Citas Médicas", description = "API para gestión de citas médicas de mascotas")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;


    @Operation(summary = "Crear nueva cita", description = "Agenda una nueva cita médica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cita creada exitosamente",
            content = @Content(mediaType = "application/json", 
            schema = @Schema(implementation = Appointment.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o error de validación")
    })
    @PostMapping()
    public ResponseEntity<?> createAppointment(@RequestBody @Valid Appointment appointment) {
        try {
            Appointment newAppointment = appointmentService.createAppointment(appointment);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAppointment); // HTTP 201
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // HTTP 400
        }
    }

    @Operation(summary = "Buscar cita por ID", description = "Obtiene los detalles de una cita específica por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita encontrada exitosamente",
            content = @Content(mediaType = "application/json", 
            schema = @Schema(implementation = AppointmentWithDetailsDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentDetail(@PathVariable Long id) {
        try {
            AppointmentWithDetailsDTO dto = appointmentService.getAppointmentDetail(id);
            return ResponseEntity.ok(dto); // HTTP 200
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // HTTP 404
        }
    }
    
    @Operation(summary = "Listar citas por usuario", description = "Obtiene todas las citas agendadas por un ID de usuario específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Citas obtenidas exitosamente",
            content = @Content(mediaType = "application/json", 
            schema = @Schema(implementation = AppointmentWithDetailsDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay citas para este usuario")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentWithDetailsDTO>> getAppointmentsByUser(@PathVariable Long userId) {
        List<AppointmentWithDetailsDTO> appointments = appointmentService.getAppointmentsByUser(userId);
        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build(); // HTTP 204
        }
        return ResponseEntity.ok(appointments); // HTTP 200
    }
    
    @Operation(summary = "Próximas citas", description = "Obtiene las próximas 3 citas pendientes para un usuario, ordenadas por fecha.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Citas próximas obtenidas exitosamente",
            content = @Content(mediaType = "application/json", 
            schema = @Schema(implementation = AppointmentWithDetailsDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay citas próximas para este usuario")
    })
    @GetMapping("/user/{userId}/upcoming")
    public ResponseEntity<List<AppointmentWithDetailsDTO>> getUpcomingAppointmentsByUser(@PathVariable Long userId) {
        List<AppointmentWithDetailsDTO> appointments = appointmentService.getUpcomingAppointmentsByUser(userId);
        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build(); // HTTP 204
        }
        return ResponseEntity.ok(appointments); // HTTP 200
    }

    @Operation(summary = "Actualizar cita", description = "Actualiza los detalles de una cita existente (requiere el objeto completo de Appointment).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita actualizada exitosamente",
            content = @Content(mediaType = "application/json", 
            schema = @Schema(implementation = Appointment.class))),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(
            @PathVariable Long id,
            @RequestBody @Valid Appointment appointment) {
        try {
            appointment.setId(id); 
            Appointment updatedAppointment = appointmentService.createAppointment(appointment);
            return ResponseEntity.ok(updatedAppointment); // HTTP 200
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); 
        }
    }

    @Operation(summary = "Eliminar cita", description = "Cancela y elimina una cita del sistema por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cita eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointmentById(id);
            return ResponseEntity.noContent().build(); // HTTP 204
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // HTTP 404
        }
    }
}