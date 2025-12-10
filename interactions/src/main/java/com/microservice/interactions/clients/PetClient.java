package com.microservice.interactions.clients;

import com.microservice.interactions.dto.external.PetExternalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "pet-service", url = "${ms.pet.url:http://localhost:8080}")
public interface PetClient {
    @GetMapping("/api/pets/{id}")
    PetExternalDto getPetById(@PathVariable("id") Long id);
}

