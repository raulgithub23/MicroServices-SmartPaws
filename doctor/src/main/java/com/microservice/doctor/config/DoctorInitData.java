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

                CreateDoctorRequest doctor1 = new CreateDoctorRequest(
                    "Dr. Carlos Méndez",
                    "Medicina General",
                    "carlos.mendez@smartpaws.cl",
                    "+56912345678",
                    Arrays.asList(
                        new ScheduleDto(null, "Lunes", "09:00", "13:00"),
                        new ScheduleDto(null, "Lunes", "15:00", "18:00"),
                        new ScheduleDto(null, "Miércoles", "09:00", "13:00"),
                        new ScheduleDto(null, "Viernes", "09:00", "13:00")
                    )
                );

                CreateDoctorRequest doctor2 = new CreateDoctorRequest(
                    "Dra. María González",
                    "Cirugía",
                    "maria.gonzalez@smartpaws.cl",
                    "+56987654321",
                    Arrays.asList(
                        new ScheduleDto(null, "Martes", "10:00", "14:00"),
                        new ScheduleDto(null, "Jueves", "10:00", "14:00"),
                        new ScheduleDto(null, "Sábado", "09:00", "12:00")
                    )
                );

                CreateDoctorRequest doctor3 = new CreateDoctorRequest(
                    "Dr. Jorge Silva",
                    "Dermatología",
                    "jorge.silva@smartpaws.cl",
                    "+56911223344",
                    Arrays.asList(
                        new ScheduleDto(null, "Lunes", "14:00", "18:00"),
                        new ScheduleDto(null, "Miércoles", "14:00", "18:00"),
                        new ScheduleDto(null, "Viernes", "14:00", "18:00")
                    )
                );

                doctorService.createDoctor(doctor1);
                doctorService.createDoctor(doctor2);
                doctorService.createDoctor(doctor3);

                System.out.println("Doctores de prueba creados exitosamente");
            }
        };
    }
}
