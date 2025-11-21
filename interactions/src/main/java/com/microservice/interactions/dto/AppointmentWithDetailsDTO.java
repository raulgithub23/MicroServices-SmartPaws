package com.microservice.interactions.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Detalle completo de una cita incluyendo nombres de mascota y doctor")
public record AppointmentWithDetailsDTO(
    @Schema(description = "ID único de la cita", example = "101")
    Long id,

    @Schema(description = "ID del dueño (Usuario)", example = "55")
    Long userId,

    @Schema(description = "ID de la mascota", example = "12")
    Long petId,

    @Schema(description = "ID del doctor asignado", example = "3")
    Long doctorId,

    @Schema(description = "Fecha de la cita (Formato ISO-8601)", example = "2025-10-22")
    String date,

    @Schema(description = "Hora de la cita (Formato 24h)", example = "14:30")
    String time,

    @Schema(description = "Notas adicionales o motivo de consulta", example = "Vacunación anual y revisión general")
    String notes,

    @Schema(description = "Nombre de la mascota", example = "Firulais")
    String petName,

    @Schema(description = "Nombre del doctor", example = "Dr. House")
    String doctorName,

    @Schema(description = "Especialidad del doctor", example = "Cirugía General")
    String doctorSpecialty
) {
    public AppointmentWithDetailsDTO(Long id, Long userId, Long petId, Long doctorId, LocalDate date, LocalTime time, String notes, String petName, String doctorName, String doctorSpecialty) {
        this(id, userId, petId, doctorId, date.toString(), time.toString(), notes, petName, doctorName, doctorSpecialty);
    }
}