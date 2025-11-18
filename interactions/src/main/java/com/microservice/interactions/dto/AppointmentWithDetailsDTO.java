package com.microservice.interactions.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "Detalle completo de una cita, incluyendo nombres de entidades relacionadas")
public class AppointmentWithDetailsDTO {
    
    Long id;
    Long userId;
    Long petId;
    Long doctorId;
    LocalDate date;
    LocalTime time;
    String notes;
    
    // datos extras
    String petName;
    String doctorName;
    String doctorSpecialty;
    
    // Constructor completo 
    public AppointmentWithDetailsDTO(
        Long id, Long userId, Long petId, Long doctorId, 
        LocalDate date, LocalTime time, String notes, 
        String petName, String doctorName, String doctorSpecialty) {
            
        this.id = id;
        this.userId = userId;
        this.petId = petId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.notes = notes;
        this.petName = petName;
        this.doctorName = doctorName;
        this.doctorSpecialty = doctorSpecialty;
    }
}