package com.microservice.doctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request para actualizar horarios de un doctor")
public class UpdateSchedulesRequest {
    
    @Schema(description = "Nueva lista de horarios", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ScheduleDto> schedules;
}