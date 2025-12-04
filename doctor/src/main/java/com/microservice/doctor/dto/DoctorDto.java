package com.microservice.doctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información completa de un doctor veterinario")
public class DoctorDto {
    
    @Schema(description = "ID único del doctor", example = "1")
    private Long id;
    
    @Schema(description = "Nombre completo del doctor", example = "Dr. Carlos Méndez")
    private String name;
    
    @Schema(description = "Especialidad médica", example = "Veterinario General")
    private String specialty;
    
    @Schema(description = "Email de contacto", example = "carlos.mendez@smartpaws.cl")
    private String email;
    
    @Schema(description = "Teléfono de contacto", example = "+56912345678")
    private String phone;
    
    @Schema(description = "Horarios de atención del doctor")
    private List<ScheduleDto> schedules;
}
