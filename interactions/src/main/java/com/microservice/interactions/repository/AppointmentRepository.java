package com.microservice.interactions.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.interactions.model.Appointment;
import org.springframework.data.domain.Pageable;
 
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByOrderByDateDescTimeDesc();

    List<Appointment> findByDateGreaterThanEqualOrderByDateAscTimeAsc(LocalDate date, Pageable pageable);

    List<Appointment> findByUserIdOrderByDateDescTimeDesc(Long userId);

    List<Appointment> findByUserIdAndDateGreaterThanEqualOrderByDateAscTimeAsc(Long userId, LocalDate date, Pageable pageable);

    List<Appointment> findByDoctorIdAndDate(Long doctorId, LocalDate date);

    List<Appointment> findByDoctorIdOrderByDateAscTimeAsc(Long doctorId);
}