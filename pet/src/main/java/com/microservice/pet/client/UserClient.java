package com.microservice.pet.client;

import com.microservice.pet.dto.UserDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice-auth", url = "https://xd6w381w-8081.use2.devtunnels.ms/") 
public interface UserClient {

    @GetMapping("/auth/user/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}