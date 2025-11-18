package com.microservice.interactions.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.interactions.model.Appointment;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByUserIdOrderByDateAscTimeAsc(Long userId);

    //Obtener las proximas 3 citas de un usuario
    @Query(value = "SELECT a FROM Appointment a WHERE a.userId = :userId AND a.date >= :currentDate ORDER BY a.date ASC, a.time ASC LIMIT 3")
    List<Appointment> findUpcomingAppointmentsByUser(Long userId, LocalDate currentDate);
    
    // detalle por ID de cita
    Optional<Appointment> findById(Long id);
}