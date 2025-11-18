package com.microservice.interactions.service;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.microservice.interactions.dto.AppointmentWithDetailsDTO;
import com.microservice.interactions.model.Appointment;
import com.microservice.interactions.repository.AppointmentRepository;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    
    // Asunción: Los servicios de otros microservicios se inyectarían aquí para obtener nombres y detalles (PetService, DoctorService)

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Convierte una entidad Appointment a su DTO con detalles.
     * En un entorno real de microservicios, esta función llamaría a otros servicios
     * (e.g., PetService, DoctorService) para obtener los nombres reales.
     */
    private AppointmentWithDetailsDTO convertToDtoWithDetails(Appointment appointment) {
        // SIMULACIÓN: Estos valores vendrían de llamadas HTTP a otros microservicios
        // o de cachés locales.
        String petName = "Mascota " + appointment.getPetId();
        String doctorName = "Dr. " + appointment.getDoctorId();
        String doctorSpecialty = "Especialidad Mock";
        
        return new AppointmentWithDetailsDTO(
            appointment.getId(),
            appointment.getUserId(),
            appointment.getPetId(),
            appointment.getDoctorId(),
            appointment.getDate(),
            appointment.getTime(),
            appointment.getNotes(),
            petName,
            doctorName,
            doctorSpecialty
        );
    }
    
    // --- MÉTODOS CRUD SIMILARES A TU REPOSITORY ---

    /**
     * Obtiene la lista completa de citas de un usuario.
     */
    public List<AppointmentWithDetailsDTO> getAppointmentsByUser(Long userId) {
        return appointmentRepository.findByUserIdOrderByDateAscTimeAsc(userId)
                .stream()
                .map(this::convertToDtoWithDetails)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las próximas 3 citas de un usuario.
     */
    public List<AppointmentWithDetailsDTO> getUpcomingAppointmentsByUser(Long userId) {
        return appointmentRepository.findUpcomingAppointmentsByUser(userId, LocalDate.now())
                .stream()
                .map(this::convertToDtoWithDetails)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el detalle de una cita por ID.
     */
    public AppointmentWithDetailsDTO getAppointmentDetail(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada en la base de datos"));
        
        return convertToDtoWithDetails(appointment);
    }

    /**
     * Crea una nueva cita.
     */
    public Appointment createAppointment(Appointment newAppointment) {
        // Aquí iría la lógica de validación de disponibilidad de doctor/hora
        return appointmentRepository.save(newAppointment);
    }

    /**
     * Elimina una cita por ID.
     */
    public void deleteAppointmentById(Long appointmentId) {
        // Se recomienda verificar si existe antes de borrar, aunque deleteById ya lo gestiona.
        if (!appointmentRepository.existsById(appointmentId)) {
             throw new RuntimeException("Cita no encontrada para borrar");
        }
        appointmentRepository.deleteById(appointmentId);
    }
}