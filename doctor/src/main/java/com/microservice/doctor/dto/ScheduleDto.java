package com.microservice.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {
    private Long id;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}