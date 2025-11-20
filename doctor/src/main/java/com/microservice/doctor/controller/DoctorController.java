package com.microservice.doctor.controller;

import com.microservice.doctor.dto.CreateDoctorRequest;
import com.microservice.doctor.dto.DoctorDto;
import com.microservice.doctor.dto.UpdateSchedulesRequest;
import com.microservice.doctor.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        try {
            List<DoctorDto> doctors = doctorService.getAllDoctors();
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
        try {
            DoctorDto doctor = doctorService.getDoctorById(id);
            return ResponseEntity.ok(doctor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/by-email")
    public ResponseEntity<DoctorDto> getDoctorByEmail(@RequestParam String email) {
        try {
            DoctorDto doctor = doctorService.getDoctorByEmail(email);
            return ResponseEntity.ok(doctor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> createDoctor(@RequestBody CreateDoctorRequest request) {
        try {
            DoctorDto created = doctorService.createDoctor(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/schedules")
    public ResponseEntity<?> updateSchedules(
        @PathVariable Long id,
        @RequestBody UpdateSchedulesRequest request
    ) {
        try {
            DoctorDto updated = doctorService.updateSchedules(id, request.getSchedules());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok("Doctor eliminado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countDoctors() {
        return ResponseEntity.ok(doctorService.countDoctors());
    }
}