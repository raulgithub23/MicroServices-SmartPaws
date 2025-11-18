package com.microservice.pet.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pets")
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Modelo de Mascota para el sistema de Usuarios")
public class Pets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la mascota", example = "1")
    private Long id;

    @NotNull(message = "El ID de usuario no puede ser nulo")
    @Column(name = "user_id", nullable = false)
    @Schema(description = "ID del usuario propietario (Clave Foránea)", example = "101", required = true)
    private Long userId;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    @Column(nullable = false)
    @Schema(description = "Nombre de la mascota", example = "Max", required = true)
    private String name;

    @NotBlank(message = "La especie no puede estar vacía")
    @Size(max = 50, message = "La especie no puede exceder los 50 caracteres")
    @Column(nullable = false)
    @Schema(description = "Especie de la mascota", example = "Perro", required = true)
    private String especie;

    @Column(name = "fecha_nacimiento")
    @Schema(description = "Fecha de nacimiento de la mascota (YYYY-MM-DD)", example = "2020-05-15")
    private LocalDate fechaNacimiento;

    @DecimalMin(value = "0.01", message = "El peso debe ser un valor positivo")
    @Column(columnDefinition = "NUMERIC(5,2)")
    @Schema(description = "Peso de la mascota en kilogramos", example = "15.5")
    private Float peso;

    @Size(max = 20, message = "El género no puede exceder los 20 caracteres")
    @Schema(description = "Género de la mascota", example = "Macho")
    private String genero;

    @Size(max = 50, message = "El color no puede exceder los 50 caracteres")
    @Schema(description = "Color predominante de la mascota", example = "Marrón y blanco")
    private String color;

    @Size(max = 1000, message = "Las notas no pueden exceder los 1000 caracteres")
    @Schema(description = "Notas adicionales sobre la mascota")
    private String notas;
    
    @Column(name = "fecha_registro", updatable = false)
    @Schema(description = "Fecha y hora de registro de la mascota", example = "2025-11-18T18:00:00")
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void creacionFechaRegistro() {
        this.fechaRegistro = LocalDateTime.now();
    }
}