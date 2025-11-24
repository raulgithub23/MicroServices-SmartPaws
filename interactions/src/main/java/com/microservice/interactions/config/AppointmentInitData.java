package com.microservice.interactions.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microservice.interactions.model.Appointment;
import com.microservice.interactions.repository.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

@Configuration
public class AppointmentInitData {

    @SuppressWarnings("null")
    @Bean
    public CommandLineRunner initAppointments(AppointmentRepository appointmentRepository) {
        return args -> {
            if (appointmentRepository.count() == 0) {
                System.out.println("Iniciando carga de datos de Citas...");

                Appointment app1 = Appointment.builder()
                        .userId(6L)
                        .petId(1L) // Firulais
                        .doctorId(3L)
                        .date(LocalDate.of(2025, 12, 5))
                        .time(LocalTime.of(9, 0))
                        .notes("Control anual y vacunas séxtuples.")
                        .build();

                Appointment app2 = Appointment.builder()
                        .userId(6L)
                        .petId(2L) // Mishi
                        .doctorId(4L)
                        .date(LocalDate.of(2025, 12, 6))
                        .time(LocalTime.of(10, 30))
                        .notes("Evaluación para esterilización.")
                        .build();

                Appointment app3 = Appointment.builder()
                        .userId(6L)
                        .petId(1L) // Firulais
                        .doctorId(5L)
                        .date(LocalDate.of(2025, 12, 10))
                        .time(LocalTime.of(16, 0))
                        .notes("Revisión de alergia en la piel.")
                        .build();

                Appointment app4 = Appointment.builder()
                        .userId(7L)
                        .petId(4L) // Rocky
                        .doctorId(3L)
                        .date(LocalDate.of(2025, 12, 12))
                        .time(LocalTime.of(11, 30))
                        .notes("Limpieza dental y chequeo general.")
                        .build();

                Appointment app5 = Appointment.builder()
                        .userId(7L)
                        .petId(3L) // Lola
                        .doctorId(5L)
                        .date(LocalDate.of(2025, 12, 15))
                        .time(LocalTime.of(14, 0))
                        .notes("Caída excesiva de pelo.")
                        .build();

                Appointment app6 = Appointment.builder()
                        .userId(6L)
                        .petId(1L) // Firulais
                        .doctorId(5L)
                        .date(LocalDate.of(2025, 11, 10))
                        .time(LocalTime.of(16, 0))
                        .notes("Revisión Por molestia en el Pie.")
                        .build();

                appointmentRepository.saveAll(Arrays.asList(app1, app2, app3, app4, app5, app6));

                System.out.println("Datos de Citas cargados correctamente.");
            }
        };
    }
}
