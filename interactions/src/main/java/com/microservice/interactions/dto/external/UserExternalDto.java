package com.microservice.interactions.dto.external;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserExternalDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
}
