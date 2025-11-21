package com.microservice.pet.config;

import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microservice.pet.model.Pets;
import com.microservice.pet.repository.PetsRepository;

@Configuration
public class CargarDatos {

    @Bean
    CommandLineRunner initDataBase(PetsRepository petsRepository) {
        return args -> {
            if (petsRepository.count() == 0) {
                System.out.println("Iniciando carga de datos de Mascotas...");

                petsRepository.save(new Pets(
                    null, 
                    6L, 
                    "Firulais", 
                    "Perro", 
                    LocalDate.of(2020, 5, 20), 
                    12.5f, 
                    "Macho", 
                    "Café y Blanco", 
                    "Es muy juguetón y le gusta correr."
                ));

                petsRepository.save(new Pets(
                    null, 
                    7L, 
                    "Mishi", 
                    "Gato", 
                    LocalDate.of(2021, 8, 10), 
                    4.2f, 
                    "Hembra", 
                    "Gris", 
                    "Duerme la mayor parte del día."
                ));

                petsRepository.save(new Pets(
                    null, 
                    2L, 
                    "Bolt", 
                    "Perro", 
                    LocalDate.of(2023, 1, 15), 
                    12.15f, 
                    "Macho", 
                    "Dorado", 
                    "Le encanta el ejercicio."
                ));

                petsRepository.save(new Pets(
                    null, 
                    3L, 
                    "Lola", 
                    "Gato", 
                    LocalDate.of(2022, 11, 5), 
                    5.0f, 
                    "Hembra", 
                    "Blanco", 
                    "Come mucho"
                ));

                petsRepository.save(new Pets(
                    null, 
                    4L, 
                    "Rocky", 
                    "Perro", 
                    LocalDate.of(2019, 3, 30), 
                    25.0f, 
                    "Macho", 
                    "Negro", 
                    "Perro guardián muy leal."
                ));

                System.out.println("Datos de Mascotas cargados correctamente.");
            }
        };
    }
}