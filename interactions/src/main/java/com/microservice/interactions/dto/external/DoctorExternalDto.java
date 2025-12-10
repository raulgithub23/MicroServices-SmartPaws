package com.microservice.interactions.dto.external;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorExternalDto {
    private Long id;
    private String name;
    private String specialty;
    private String email;
}
