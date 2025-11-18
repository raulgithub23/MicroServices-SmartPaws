package com.microservice.interactions.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointments")
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Modelo de Cita Médica")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la cita", example = "1")
    private Long id;

    // claves foraneas
    @NotNull(message = "El ID de usuario es obligatorio")
    @Column(name = "user_id", nullable = false)
    @Schema(description = "ID del usuario que agenda", required = true)
    private Long userId;

    @NotNull(message = "El ID de la mascota es obligatorio")
    @Column(name = "pet_id", nullable = false)
    @Schema(description = "ID de la mascota citada", required = true)
    private Long petId;

    @NotNull(message = "El ID del doctor es obligatorio")
    @Column(name = "doctor_id", nullable = false)
    @Schema(description = "ID del doctor asignado", required = true)
    private Long doctorId;
    // ---------------------

    @NotNull(message = "La fecha es obligatoria")
    @Schema(description = "Fecha de la cita (YYYY-MM-DD)", example = "2025-10-22", required = true)
    private LocalDate date;

    @NotNull(message = "La hora es obligatoria")
    @Schema(description = "Hora de la cita (HH:MM)", example = "10:30", required = true)
    private LocalTime time;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Notas y descripción de la cita")
    private String notes;
    
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void creacionFechaRegistro() {
        this.fechaRegistro = LocalDateTime.now();
    }
}