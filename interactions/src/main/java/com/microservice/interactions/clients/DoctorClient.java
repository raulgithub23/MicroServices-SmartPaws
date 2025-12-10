package com.microservice.interactions.clients;


import com.microservice.interactions.dto.external.DoctorExternalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "doctor-service", url = "${ms.doctor.url}")
public interface DoctorClient {
    @GetMapping("/doctors/{id}")
    DoctorExternalDto getDoctorById(@PathVariable("id") Long id);
}
