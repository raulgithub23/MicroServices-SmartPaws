package com.microservice.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microservice.auth.model.User;
import com.microservice.auth.repository.UserRepository;
import com.microservice.auth.service.AuthService;



@Configuration
public class AuthInitData {

    @Autowired
    private AuthService authService;

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {

            if (userRepository.count() == 0) {

                authService.register(new User(
                    0L,
                    "ADMIN",
                    "Gabriel Vidal",
                    "ga.vidal@duocuc.cl",
                    "20123123-2",
                    "password123",
                    "drawable://larry"
                ));

                authService.register(new User(
                    0L,
                    "USER",
                    "Raul Fernandez",
                    "ra.fernandez@duocuc.cl",
                    "20456789-5",
                    "123",
                    "drawable://larry"
                ));
                System.out.println("Usuarios iniciales cargados en BD.");
            }
        };
    }
}
