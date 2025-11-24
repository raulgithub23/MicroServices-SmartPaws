package com.microservice.doctor.config;

import com.microservice.doctor.dto.CreateDoctorRequest;
import com.microservice.doctor.dto.ScheduleDto;
import com.microservice.doctor.repository.DoctorRepository;
import com.microservice.doctor.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DoctorInitData {

    @Autowired
    private DoctorService doctorService;

    @Bean
    public CommandLineRunner initDoctors(DoctorRepository doctorRepository) {
        return args -> {
            if (doctorRepository.count() == 0) {

                // Dr. Carlos Méndez - Medicina General
                CreateDoctorRequest doctor1 = new CreateDoctorRequest(
                    "Dr. Carlos Méndez",
                    "Veterinario General", 
                    "carlos.mendez@smartpaws.cl",
                    "+56912345678",
                    Arrays.asList(
                        new ScheduleDto(null, "Lunes", "09:00", "13:00"),
                        new ScheduleDto(null, "Lunes", "15:00", "18:00"),
                        new ScheduleDto(null, "Miércoles", "09:00", "13:00"),
                        new ScheduleDto(null, "Viernes", "09:00", "13:00")
                    )
                );

                // Dra. María González - Cirugía Veterinaria
                CreateDoctorRequest doctor2 = new CreateDoctorRequest(
                    "Dra. María González",
                    "Cirugía Veterinaria", 
                    "maria.gonzalez@smartpaws.cl",
                    "+56987654321",
                    Arrays.asList(
                        new ScheduleDto(null, "Martes", "10:00", "14:00"),
                        new ScheduleDto(null, "Jueves", "10:00", "14:00"),
                        new ScheduleDto(null, "Sábado", "09:00", "12:00")
                    )
                );

                // Dr. Jorge Silva - Animales Exóticos
                CreateDoctorRequest doctor3 = new CreateDoctorRequest(
                    "Dr. Jorge Silva",
                    "Animales Exóticos", 
                    "jorge.silva@smartpaws.cl",
                    "+56911223344",
                    Arrays.asList(
                        new ScheduleDto(null, "Lunes", "14:00", "18:00"),
                        new ScheduleDto(null, "Miércoles", "14:00", "18:00"),
                        new ScheduleDto(null, "Viernes", "14:00", "18:00")
                    )
                );

                // **Dra. Ana Rojas - Dermatología Veterinaria (AGREGADO)**
                CreateDoctorRequest doctor4 = new CreateDoctorRequest(
                    "Dra. Ana Rojas",
                    "Dermatología Veterinaria",
                    "ana.rojas@smartpaws.cl",
                    "+56922334455",
                    Arrays.asList(
                        new ScheduleDto(null, "Martes", "15:00", "19:00"),
                        new ScheduleDto(null, "Jueves", "15:00", "19:00"),
                        new ScheduleDto(null, "Sábado", "13:00", "16:00")
                    )
                );

                // **Dr. Luis Pérez - Odontología Veterinaria (AGREGADO)**
                CreateDoctorRequest doctor5 = new CreateDoctorRequest(
                    "Dr. Luis Pérez",
                    "Odontología Veterinaria",
                    "luis.perez@smartpaws.cl",
                    "+56933445566",
                    Arrays.asList(
                        new ScheduleDto(null, "Lunes", "10:00", "12:00"),
                        new ScheduleDto(null, "Miércoles", "10:00", "12:00"),
                        new ScheduleDto(null, "Viernes", "10:00", "12:00")
                    )
                );

                doctorService.createDoctor(doctor1);
                doctorService.createDoctor(doctor2);
                doctorService.createDoctor(doctor3);
                doctorService.createDoctor(doctor4); // Nuevo porque estaba dando error
                doctorService.createDoctor(doctor5); // Y esta tambien

                System.out.println("Doctores de prueba creados exitosamente");
            }
        };
    }
}