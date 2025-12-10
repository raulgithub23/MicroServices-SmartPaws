package com.microservice.interactions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.interactions.clients.DoctorClient;
import com.microservice.interactions.clients.PetClient;
import com.microservice.interactions.clients.UserClient;
import com.microservice.interactions.dto.AppointmentRequestDto;
import com.microservice.interactions.dto.AppointmentResponseDto;
import com.microservice.interactions.dto.AppointmentWithDetailsDTO;
import com.microservice.interactions.dto.DoctorAppointmentSummaryDTO;
import com.microservice.interactions.dto.external.DoctorExternalDto;
import com.microservice.interactions.dto.external.PetExternalDto;
import com.microservice.interactions.dto.external.UserExternalDto;
import com.microservice.interactions.model.Appointment;
import com.microservice.interactions.repository.AppointmentRepository;

import feign.FeignException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@SuppressWarnings("null")
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserClient userClient;
    @Autowired
    private PetClient petClient;
    @Autowired
    private DoctorClient doctorClient;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private AppointmentResponseDto mapToDto(Appointment entity) {
        return AppointmentResponseDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .petId(entity.getPetId())
                .doctorId(entity.getDoctorId())
                .date(entity.getDate().toString())
                .time(entity.getTime().toString())
                .notes(entity.getNotes())
                .build();
    }

    private AppointmentWithDetailsDTO mapToDetailedDto(Appointment entity) {
        String petName = "Desconocido";
        String doctorName = "Desconocido";
        String doctorSpecialty = "N/A";

        try {
            if (entity.getPetId() != null) {
                PetExternalDto pet = petClient.getPetById(entity.getPetId());
                if (pet != null) petName = pet.getName();
            }
        } catch (Exception e) { System.err.println("Error fetching Pet: " + e.getMessage()); }

        try {
            if (entity.getDoctorId() != null) {
                DoctorExternalDto doc = doctorClient.getDoctorById(entity.getDoctorId());
                if (doc != null) {
                    doctorName = doc.getName();
                    doctorSpecialty = doc.getSpecialty();
                }
            }
        } catch (Exception e) { System.err.println("Error fetching Doctor: " + e.getMessage()); }

        return new AppointmentWithDetailsDTO(
                entity.getId(),
                entity.getUserId(),
                entity.getPetId(),
                entity.getDoctorId(),
                entity.getDate().toString(),
                entity.getTime().toString(),
                entity.getNotes(),
                petName,
                doctorName,
                doctorSpecialty
        );
    }

    private DoctorAppointmentSummaryDTO mapToDoctorSummaryDto(Appointment entity) {
        String petName = "Desconocido";
        String petEspecie = "N/A";
        String ownerName = "Desconocido";
        String ownerPhone = "N/A";

        try {
            if (entity.getPetId() != null) {
                PetExternalDto pet = petClient.getPetById(entity.getPetId());
                if (pet != null) {
                    petName = pet.getName();
                    petEspecie = pet.getEspecie();
                }
            }
        } catch (Exception e) { /* Log error */ }

        try {
            if (entity.getUserId() != null) {
                UserExternalDto user = userClient.getUserById(entity.getUserId());
                if (user != null) {
                    ownerName = user.getName();
                    ownerPhone = user.getPhone();
                }
            }
        } catch (Exception e) { /* Log error */ }

        return new DoctorAppointmentSummaryDTO(
                entity.getId(),
                entity.getDate().toString(),
                entity.getTime().toString(),
                entity.getNotes(),
                petName,
                petEspecie,
                ownerName,
                ownerPhone
        );
    }

    private void validateExternalId(Runnable validationCall, String entityName) {
        try {
            validationCall.run();
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Error: " + entityName + " no existe con el ID proporcionado.");
        } catch (FeignException e) {
            System.err.println("Advertencia: No se pudo validar existencia de " + entityName + ". Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado validando " + entityName + ": " + e.getMessage());
        }
    }


    public List<AppointmentResponseDto> getAllAppointments() {
        return appointmentRepository.findAllByOrderByDateDescTimeDesc().stream()
                .map(this::mapToDto).toList();
    }

    public AppointmentWithDetailsDTO getAppointmentDetailsById(Long id) {
        Appointment app = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));
        return mapToDetailedDto(app);
    }

    public List<AppointmentResponseDto> getUpcomingAppointments() {
        return appointmentRepository.findByDateGreaterThanEqualOrderByDateAscTimeAsc(
                LocalDate.now(), PageRequest.of(0, 3)
        ).stream().map(this::mapToDto).toList();
    }

    public List<AppointmentResponseDto> getAppointmentsByUser(Long userId) {
        return appointmentRepository.findByUserIdOrderByDateDescTimeDesc(userId).stream()
                .map(this::mapToDto).toList();
    }

    public List<AppointmentWithDetailsDTO> getUpcomingAppointmentsByUserDetailed(Long userId) {
        return appointmentRepository.findByUserIdAndDateGreaterThanEqualOrderByDateAscTimeAsc(
                userId, LocalDate.now(), PageRequest.of(0, 3)
        ).stream().map(this::mapToDetailedDto).toList(); 
    }
    
    public List<AppointmentResponseDto> getUpcomingAppointmentsByUser(Long userId) {
        return appointmentRepository.findByUserIdAndDateGreaterThanEqualOrderByDateAscTimeAsc(
                userId, LocalDate.now(), PageRequest.of(0, 3)
        ).stream().map(this::mapToDto).toList();
    }

    public List<DoctorAppointmentSummaryDTO> getAppointmentsByDoctorAndDateRich(Long doctorId, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
        return appointmentRepository.findByDoctorIdAndDate(doctorId, date).stream()
                .map(this::mapToDoctorSummaryDto) 
                .toList();
    }
    
    public List<AppointmentResponseDto> getAppointmentsByDoctorAndDate(Long doctorId, String dateStr) {
         LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
         return appointmentRepository.findByDoctorIdAndDate(doctorId, date).stream()
                 .map(this::mapToDto)
                 .toList();
     }

    public List<AppointmentResponseDto> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorIdOrderByDateAscTimeAsc(doctorId).stream()
                .map(this::mapToDto).toList();
    }

    public AppointmentResponseDto getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));
    }

    public Long createAppointment(AppointmentRequestDto dto) {
        if (dto.getUserId() != null) {
            validateExternalId(() -> userClient.getUserById(dto.getUserId()), "Usuario");
        }
        if (dto.getPetId() != null) {
            validateExternalId(() -> petClient.getPetById(dto.getPetId()), "Mascota");
        }
        if (dto.getDoctorId() != null) {
            validateExternalId(() -> doctorClient.getDoctorById(dto.getDoctorId()), "Doctor");
        }

        Appointment appointment = Appointment.builder()
                .userId(dto.getUserId())
                .petId(dto.getPetId())
                .doctorId(dto.getDoctorId())
                .date(LocalDate.parse(dto.getDate(), DATE_FORMATTER))
                .time(LocalTime.parse(dto.getTime(), TIME_FORMATTER))
                .notes(dto.getNotes())
                .build();
        return appointmentRepository.save(appointment).getId();
    }

    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Cita no encontrada");
        }
        appointmentRepository.deleteById(id);
    }
    
    public long count() {
        return appointmentRepository.count();
    }
}