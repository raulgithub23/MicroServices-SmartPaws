package com.microservice.doctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Horario de atención de un doctor")
public class ScheduleDto {
    
    @Schema(description = "ID del horario (null al crear)", example = "1")
    private Long id;
    
    @Schema(description = "Día de la semana", example = "Lunes", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dayOfWeek;
    
    @Schema(description = "Hora de inicio (HH:mm)", example = "09:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private String startTime;
    
    @Schema(description = "Hora de fin (HH:mm)", example = "13:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private String endTime;
}