package com.microservice.interactions.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Datos requeridos para agendar una nueva cita")
public record AppointmentCreateDTO(
    @Schema(description = "ID del usuario dueño de la mascota", example = "55")
    Long userId,

    @Schema(description = "ID de la mascota", example = "12")
    Long petId,

    @NotNull
    @Schema(description = "ID del doctor seleccionado", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    Long doctorId,

    @NotNull
    @Schema(description = "Fecha deseada (YYYY-MM-DD)", example = "2025-11-15", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate date,

    @NotNull
    @Schema(description = "Hora deseada (HH:mm:ss)", example = "09:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalTime time,

    @Schema(description = "Notas opcionales del usuario", example = "La mascota ha estado cojeando un poco")
    String notes
) {}
