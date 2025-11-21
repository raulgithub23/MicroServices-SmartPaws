package com.microservice.interactions.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos requeridos para agendar una nueva cita")
public class AppointmentRequestDto {

    @Schema(description = "ID del usuario dueño de la mascota", example = "101")
    private Long userId;

    @Schema(description = "ID de la mascota", example = "50")
    private Long petId;

    @Schema(description = "ID del doctor asignado", example = "10")
    private Long doctorId;

    @Schema(description = "Fecha de la cita (yyyy-MM-dd)", example = "2025-10-22")
    private String date;

    @Schema(description = "Hora de la cita (HH:mm)", example = "10:30")
    private String time;

    @Schema(description = "Notas adicionales para el doctor", example = "Vacunación anual y revisión general")
    private String notes;
}
