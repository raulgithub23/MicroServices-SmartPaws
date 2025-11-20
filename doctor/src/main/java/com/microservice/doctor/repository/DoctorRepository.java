package com.microservice.doctor.repository;

import com.microservice.doctor.model.DoctorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<DoctorModel, Long> {
    
    Optional<DoctorModel> findByEmail(String email);
    
    @Query("SELECT d FROM DoctorModel d LEFT JOIN FETCH d.schedules WHERE d.id = :id")
    Optional<DoctorModel> findByIdWithSchedules(Long id);
    
    boolean existsByEmail(String email);
}