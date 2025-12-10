package com.microservice.interactions.clients;

import com.microservice.interactions.dto.external.UserExternalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "${ms.auth.url:http://localhost:8080}") 
public interface UserClient {
    @GetMapping("/auth/user/{id}")
    UserExternalDto getUserById(@PathVariable("id") Long id);
}