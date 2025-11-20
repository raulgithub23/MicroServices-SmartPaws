package com.microservice.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDoctorRequest {
    private String name;
    private String specialty;
    private String email;
    private String phone;
    private List<ScheduleDto> schedules;
}