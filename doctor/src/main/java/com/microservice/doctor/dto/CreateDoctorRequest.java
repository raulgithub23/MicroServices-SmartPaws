package com.microservice.doctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos requeridos para crear un nuevo doctor")
public class CreateDoctorRequest {
    
    @Schema(description = "Nombre completo del doctor", example = "Dra. María González", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    
    @Schema(description = "Especialidad médica", example = "Cirugía Veterinaria", requiredMode = Schema.RequiredMode.REQUIRED)
    private String specialty;
    
    @Schema(description = "Email de contacto", example = "maria.gonzalez@smartpaws.cl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
    
    @Schema(description = "Teléfono de contacto", example = "+56987654321", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;
    
    @Schema(description = "Lista de horarios de atención", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ScheduleDto> schedules;
}