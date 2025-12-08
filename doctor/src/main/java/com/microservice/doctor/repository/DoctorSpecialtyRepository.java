package com.microservice.doctor.repository;

import com.microservice.doctor.model.DoctorSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorSpecialtyRepository extends JpaRepository<DoctorSpecialty, Long> {
    Optional<DoctorSpecialty> findByName(String name);
    List<DoctorSpecialty> findByActiveTrue();
}