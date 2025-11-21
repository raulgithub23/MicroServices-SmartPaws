package com.microservice.doctor.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microservice.doctor.dto.CreateDoctorRequest;
import com.microservice.doctor.dto.DoctorDto;
import com.microservice.doctor.dto.ScheduleDto;
import com.microservice.doctor.model.DoctorModel;
import com.microservice.doctor.model.DoctorSchedule;
import com.microservice.doctor.repository.DoctorRepository;
import com.microservice.doctor.repository.DoctorScheduleRepository;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorScheduleRepository scheduleRepository;

    @InjectMocks
    private DoctorService doctorService;

    private DoctorModel doctorTest;
    private List<DoctorModel> doctorListTest;
    private CreateDoctorRequest createRequest;
    private DoctorSchedule scheduleTest;
    private List<ScheduleDto> scheduleDtoList;

    @BeforeEach
    void setUp() {
        // Cambiado a String en lugar de LocalTime
        scheduleTest = new DoctorSchedule("LUNES", "09:00", "17:00");
        scheduleTest.setId(1L);

        doctorTest = new DoctorModel("Dr. Juan Pérez", "Veterinaria General", "juan.perez@vet.com", "123456789");
        doctorTest.setId(1L);
        scheduleTest.setDoctor(doctorTest);
        doctorTest.setSchedules(Arrays.asList(scheduleTest));

        doctorListTest = Arrays.asList(doctorTest);

        createRequest = new CreateDoctorRequest();
        createRequest.setName("Dr. Juan Pérez");
        createRequest.setSpecialty("Veterinaria General");
        createRequest.setEmail("juan.perez@vet.com");
        createRequest.setPhone("123456789");

        // Cambiado a String en lugar de LocalTime
        ScheduleDto scheduleDto = new ScheduleDto(null, "LUNES", "09:00", "17:00");
        scheduleDtoList = Arrays.asList(scheduleDto);
    }

    @Test
    void testGetAllDoctors_Exitoso() {
        when(doctorRepository.findAll()).thenReturn(doctorListTest);

        List<DoctorDto> result = doctorService.getAllDoctors();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Dr. Juan Pérez");
        assertThat(result.get(0).getSchedules()).hasSize(1);
    }

    @Test
    void testGetDoctorById_Existente() {
        when(doctorRepository.findByIdWithSchedules(1L)).thenReturn(Optional.of(doctorTest));

        DoctorDto result = doctorService.getDoctorById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Dr. Juan Pérez");
        assertThat(result.getSpecialty()).isEqualTo("Veterinaria General");
    }

    @Test
    void testGetDoctorById_NoExistente() {
        when(doctorRepository.findByIdWithSchedules(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> doctorService.getDoctorById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Doctor no encontrado");
    }

    @Test
    void testGetDoctorByEmail_Existente() {
        when(doctorRepository.findByEmail("juan.perez@vet.com")).thenReturn(Optional.of(doctorTest));

        DoctorDto result = doctorService.getDoctorByEmail("juan.perez@vet.com");

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("juan.perez@vet.com");
    }

    @Test
    void testGetDoctorByEmail_NoExistente() {
        when(doctorRepository.findByEmail("noexiste@vet.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> doctorService.getDoctorByEmail("noexiste@vet.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Doctor no encontrado con email");
    }

    @Test
    void testCreateDoctor_Exitoso() {
        createRequest.setSchedules(scheduleDtoList);
        when(doctorRepository.existsByEmail(createRequest.getEmail())).thenReturn(false);
        when(doctorRepository.save(any(DoctorModel.class))).thenReturn(doctorTest);

        DoctorDto result = doctorService.createDoctor(createRequest);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Dr. Juan Pérez");
        verify(doctorRepository).save(any(DoctorModel.class));
    }

    @Test
    void testCreateDoctor_EmailDuplicado() {
        when(doctorRepository.existsByEmail(createRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> doctorService.createDoctor(createRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ya existe un doctor con ese email");
    }

    @Test
    void testUpdateSchedules_Exitoso() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctorTest));
        doNothing().when(scheduleRepository).deleteByDoctorId(1L);
        when(doctorRepository.save(any(DoctorModel.class))).thenReturn(doctorTest);

        DoctorDto result = doctorService.updateSchedules(1L, scheduleDtoList);

        assertThat(result).isNotNull();
        verify(scheduleRepository).deleteByDoctorId(1L);
        verify(doctorRepository).save(any(DoctorModel.class));
    }

    @Test
    void testUpdateSchedules_DoctorNoExistente() {
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> doctorService.updateSchedules(999L, scheduleDtoList))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Doctor no encontrado");
    }

    @Test
    void testDeleteDoctor_Exitoso() {
        when(doctorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(doctorRepository).deleteById(1L);

        doctorService.deleteDoctor(1L);

        verify(doctorRepository).deleteById(1L);
    }

    @Test
    void testDeleteDoctor_NoExistente() {
        when(doctorRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> doctorService.deleteDoctor(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Doctor no encontrado");
    }

    @Test
    void testCountDoctors_Exitoso() {
        when(doctorRepository.count()).thenReturn(5L);

        long result = doctorService.countDoctors();

        assertThat(result).isEqualTo(5L);
    }
}