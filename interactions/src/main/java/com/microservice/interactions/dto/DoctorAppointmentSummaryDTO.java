package com.microservice.interactions.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Resumen de cita optimizado para la vista de agenda del doctor")
public record DoctorAppointmentSummaryDTO(
    @Schema(example = "204")
    Long id,

    @Schema(example = "2025-10-22")
    String date,

    @Schema(example = "16:00")
    String time,

    @Schema(example = "Revisión post-operatoria")
    String notes,

    @Schema(example = "Luna")
    String petName,

    @Schema(example = "Gato / Siamés")
    String petEspecie,

    @Schema(example = "Ana García")
    String ownerName,

    @Schema(example = "+56 9 1234 5678")
    String ownerPhone
) {
    public DoctorAppointmentSummaryDTO(Long id, LocalDate date, LocalTime time, String notes, String petName, String petEspecie, String ownerName, String ownerPhone) {
        this(id, date.toString(), time.toString(), notes, petName, petEspecie, ownerName, ownerPhone);
    }
}
