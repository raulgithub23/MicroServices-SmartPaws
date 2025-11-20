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

    @Column(name = "user_id", nullable = false)
    @Schema(description = "ID del usuario propietario (Clave Foránea)", example = "101", required = true)
    private Long userId;

    @Column(nullable = false)
    @Schema(description = "Nombre de la mascota", example = "Max", required = true)
    private String name;

    @Column(nullable = false)
    @Schema(description = "Especie de la mascota", example = "Perro", required = true)
    private String especie;

    @Column(name = "fecha_nacimiento")
    @Schema(description = "Fecha de nacimiento de la mascota (YYYY-MM-DD)", example = "2020-05-15")
    private LocalDate fechaNacimiento;

    @Column(columnDefinition = "NUMERIC(5,2)")
    @Schema(description = "Peso de la mascota en kilogramos", example = "15.5")
    private Float peso;

    @Schema(description = "Género de la mascota", example = "Macho")
    private String genero;

    @Schema(description = "Color predominante de la mascota", example = "Marrón y blanco")
    private String color;

    @Schema(description = "Notas adicionales sobre la mascota")
    private String notas;
}