package com.microservice.pet.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.pet.model.Pets;
import com.microservice.pet.service.PetsService;

@WebMvcTest(PetsController.class)
public class PetsControllerTest {

    @MockBean
    private PetsService petsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    // GET - /api/pets
    @Test
    void testObtenerMascotas_JsonAndOK() throws Exception {
        when(petsService.listarMascotas()).thenReturn(petsListTest);

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Firulais"));
    }

    // POST - /api/pets
    @Test
    void testCrearMascota_JsonAndCreated() throws Exception {
        when(petsService.guardarMascota(any(Pets.class))).thenReturn(petTest);

        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petTest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Firulais"));
    }

    // GET - /api/pets/{id}
    @Test
    void testBuscarMascotaPorId_JsonAndOK() throws Exception {
        when(petsService.buscarMascota(1L)).thenReturn(petTest);

        mockMvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    // PUT - /api/pets/{id}
    @Test
    void testActualizarMascota_JsonAndOK() throws Exception {
        Pets petActualizada = new Pets();
        petActualizada.setId(1L);
        petActualizada.setName("Rex");

        when(petsService.actualizarMascota(eq(1L), any(Pets.class))).thenReturn(petActualizada);

        mockMvc.perform(put("/api/pets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petActualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rex"));
    }

    // DELETE - /api/pets/{id}
    @Test
    void testEliminarMascota_NoContent() throws Exception {
        when(petsService.borrarMascota(1L)).thenReturn("Mascota Eliminada con éxito");

        mockMvc.perform(delete("/api/pets/1"))
                .andExpect(status().isNoContent());
    }

    // GET - /api/pets/usuario/{userId}
    @Test
    void testListarMascotasPorUsuario_JsonAndOK() throws Exception {
        when(petsService.listarMascotasPorUsuario(10L)).thenReturn(petsListTest);

        mockMvc.perform(get("/api/pets/usuario/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(10L));
    }

    // GET - /api/pets/buscar/nombre?nombre=...
    @Test
    void testBuscarPorNombre_JsonAndOK() throws Exception {
        when(petsService.buscarPorNombre("Firulais")).thenReturn(petsListTest);

        mockMvc.perform(get("/api/pets/buscar/nombre")
                .param("nombre", "Firulais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Firulais"));
    }
}
