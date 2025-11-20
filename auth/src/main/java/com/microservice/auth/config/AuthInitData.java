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

                //ADMIN 1
                authService.register(new User(
                    0L,
                    "ADMIN",
                    "Gabriel Vidal",
                    "ga.vidal@duocuc.cl",
                    "20123123-2",
                    "password123",
                    "drawable://larry"
                ));

                //ADMIN 2
                authService.register(new User(
                    0L,
                    "ADMIN",
                    "Raul Fernandez",
                    "ra.fernandez@duocuc.cl",
                    "20456789-5",
                    "123",
                    "drawable://larry"
                ));

                //NUEVO: DOCTOR 1
                authService.register(new User(
                    0L,
                    "DOCTOR",
                    "Dr. Carlos Méndez",
                    "carlos.mendez@smartpaws.cl",
                    "+56912345678",
                    "Doctor123!",
                    "drawable://larry"
                ));

                //NUEVO: DOCTOR 2
                authService.register(new User(
                    0L,
                    "DOCTOR",
                    "Dra. María González",
                    "maria.gonzalez@smartpaws.cl",
                    "+56987654321",
                    "Doctor123!",
                    "drawable://larry"
                ));

                //NUEVO: DOCTOR 3
                authService.register(new User(
                    0L,
                    "DOCTOR",
                    "Dr. Jorge Silva",
                    "jorge.silva@smartpaws.cl",
                    "+56911223344",
                    "Doctor123!",
                    "drawable://larry"
                ));


                //ADMINISTRADOR BASE ANTIGUO
                authService.register(new User(
                    0L,
                    "ADMIN",
                    "Administrador",
                    "a@a.cl",
                    "12345678",
                    "Admin123!",
                    "drawable://larry"
                ));

                //USUARIO normal BASE ANTIGUO
                authService.register(new User(
                    0L,
                    "USER",
                    "Jose",
                    "b@b.cl",
                    "12345678",
                    "Jose123!",
                    "drawable://larry"
                ));
                System.out.println("Usuarios iniciales cargados en BD.");
            }
        };
    }
}