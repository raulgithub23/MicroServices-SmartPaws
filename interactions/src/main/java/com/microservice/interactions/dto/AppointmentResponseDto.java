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
@Schema(description = "Información detallada de una cita existente")
public class AppointmentResponseDto {

    @Schema(description = "ID único de la cita")
    private Long id;

    private Long userId;
    private Long petId;
    private Long doctorId;

    @Schema(description = "Fecha en formato ISO", example = "2025-10-22")
    private String date;

    @Schema(description = "Hora en formato HH:mm", example = "10:30")
    private String time;

    private String notes;
}