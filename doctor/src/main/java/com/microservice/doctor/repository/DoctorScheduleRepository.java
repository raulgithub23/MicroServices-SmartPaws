package com.microservice.doctor.repository;

import com.microservice.doctor.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    void deleteByDoctorId(Long doctorId);
}
