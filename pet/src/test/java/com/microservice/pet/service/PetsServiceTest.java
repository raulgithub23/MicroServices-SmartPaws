package com.microservice.pet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microservice.pet.model.Pets;
import com.microservice.pet.repository.PetsRepository;

@ExtendWith(MockitoExtension.class)
public class PetsServiceTest {

    @Mock
    private PetsRepository petsRepository;

    @InjectMocks
    private PetsService petsService;

    private Pets petTest;
    private List<Pets> petsListTest;

    @BeforeEach
    void setUp() {
        petTest = new Pets();
        petTest.setId(1L);
        petTest.setUserId(10L);
        petTest.setName("Firulais");
        petTest.setEspecie("Perro");
        petTest.setFechaNacimiento(LocalDate.of(2020, 1, 1));
        petTest.setPeso(15.5f);
        petTest.setGenero("Macho");
        petTest.setColor("Cafe");
        petTest.setNotas("Es muy juguetón");

        petsListTest = Arrays.asList(petTest);
    }

    @Test
    void testListarMascotas_Existentes() {
        when(petsRepository.findAll()).thenReturn(petsListTest);

        List<Pets> result = petsService.listarMascotas();

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Firulais");
    }

    @Test
    void testBuscarMascota_Existente() {
        when(petsRepository.findById(1L)).thenReturn(Optional.of(petTest));

        Pets result = petsService.buscarMascota(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Firulais");
    }

    @Test
    void testListarMascotasPorUsuario_Existente() {
        Long userId = 10L;
        when(petsRepository.findByUserId(userId)).thenReturn(petsListTest);

        List<Pets> result = petsService.listarMascotasPorUsuario(userId);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getUserId()).isEqualTo(userId);
    }

    @Test
    void testGuardarMascota_Exitoso() {
        when(petsRepository.save(any(Pets.class))).thenReturn(petTest);

        Pets result = petsService.guardarMascota(petTest);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Firulais");
    }

    @Test
    void testBorrarMascota_Exitoso() {
        when(petsRepository.findById(1L)).thenReturn(Optional.of(petTest));
        doNothing().when(petsRepository).deleteById(1L);

        String result = petsService.borrarMascota(1L);

        assertThat(result).isEqualTo("Mascota Eliminada con éxito");
        verify(petsRepository).deleteById(1L);
    }

    @Test
    void testActualizarMascota_Exitoso() {
        Pets nuevosDatos = new Pets();
        nuevosDatos.setName("Rex");
        nuevosDatos.setPeso(16.0f);
        
        when(petsRepository.findById(1L)).thenReturn(Optional.of(petTest));
        
        when(petsRepository.save(any(Pets.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pets result = petsService.actualizarMascota(1L, nuevosDatos);

        assertThat(result.getName()).isEqualTo("Rex");
        assertThat(result.getPeso()).isEqualTo(16.0);
        assertThat(result.getEspecie()).isEqualTo("Perro");
    }

    @Test
    void testBuscarPorNombre_Exitoso() {
        String nombreBusqueda = "Firulais";
        when(petsRepository.findByNameContainingIgnoreCase(nombreBusqueda)).thenReturn(petsListTest);

        List<Pets> result = petsService.buscarPorNombre(nombreBusqueda);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getName()).isEqualTo("Firulais");
    }
}
