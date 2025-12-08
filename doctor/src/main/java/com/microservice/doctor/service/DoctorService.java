package com.microservice.doctor.service;

import com.microservice.doctor.dto.CreateDoctorRequest;
import com.microservice.doctor.dto.DoctorDto;
import com.microservice.doctor.dto.ScheduleDto;
import com.microservice.doctor.model.DoctorModel;
import com.microservice.doctor.model.DoctorSchedule;
import com.microservice.doctor.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Transactional(readOnly = true)
    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DoctorDto getDoctorById(Long id) {
        DoctorModel doctor = doctorRepository.findByIdWithSchedules(id)
            .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
        return convertToDto(doctor);
    }

    @Transactional(readOnly = true)
    public DoctorDto getDoctorByEmail(String email) {
        DoctorModel doctor = doctorRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Doctor no encontrado con email: " + email));
        
        doctor.getSchedules().size();
        
        return convertToDto(doctor);
    }

    @Transactional
    public DoctorDto createDoctor(CreateDoctorRequest request) {
        if (doctorRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Ya existe un doctor con ese email");
        }

        DoctorModel doctor = new DoctorModel(
            request.getName(),
            request.getSpecialty(),
            request.getEmail(),
            request.getPhone()
        );

        if (request.getSchedules() != null && !request.getSchedules().isEmpty()) {
            List<DoctorSchedule> schedules = request.getSchedules().stream()
                .map(dto -> {
                    DoctorSchedule schedule = new DoctorSchedule(
                        dto.getDayOfWeek(),
                        dto.getStartTime(),
                        dto.getEndTime()
                    );
                    schedule.setDoctor(doctor);
                    return schedule;
                })
                .collect(Collectors.toList());
            doctor.setSchedules(schedules);
        }

        DoctorModel saved = doctorRepository.save(doctor);
        return convertToDto(saved);
    }

    @Transactional
    public DoctorDto updateSchedules(Long doctorId, List<ScheduleDto> scheduleDtos) {
        DoctorModel doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        doctor.getSchedules().clear(); 
        
        if (scheduleDtos != null && !scheduleDtos.isEmpty()) {
            List<DoctorSchedule> newSchedules = scheduleDtos.stream()
                .map(dto -> {
                    DoctorSchedule schedule = new DoctorSchedule(
                        dto.getDayOfWeek(),
                        dto.getStartTime(),
                        dto.getEndTime()
                    );
                    schedule.setDoctor(doctor);
                    return schedule;
                })
                .collect(Collectors.toList());
                
            doctor.getSchedules().addAll(newSchedules); 
        }
        
        DoctorModel updated = doctorRepository.save(doctor);
        return convertToDto(updated);
    }

    @Transactional
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor no encontrado");
        }
        doctorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long countDoctors() {
        return doctorRepository.count();
    }

    private DoctorDto convertToDto(DoctorModel doctor) {
        List<ScheduleDto> scheduleDtos = doctor.getSchedules().stream()
            .map(s -> new ScheduleDto(s.getId(), s.getDayOfWeek(), s.getStartTime(), s.getEndTime()))
            .collect(Collectors.toList());

        return new DoctorDto(
            doctor.getId(),
            doctor.getName(),
            doctor.getSpecialty(),
            doctor.getEmail(),
            doctor.getPhone(),
            scheduleDtos
        );
    }
}