package com.microservice.pet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.pet.model.Pets;
import com.microservice.pet.service.PetsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/pets")
@Tag(name = "Mascotas", description = "API para gestión de mascotas de usuarios")
public class PetsController {

    @Autowired
    private PetsService petsService;

    @Operation(summary = "Listar todas las mascotas", description = "Obtiene una lista de todas las mascotas registradas en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de mascotas obtenida exitosamente",
            content = @Content(mediaType = "application/json", 
            schema = @Schema(implementation = Pets.class))),
        @ApiResponse(responseCode = "204", description = "No hay mascotas disponibles")
    })
    @GetMapping()
    public ResponseEntity<List<Pets>> obtenerMascotas() {
        List<Pets> petsDisponibles = petsService.listarMascotas();
        if (petsDisponibles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(petsDisponibles);
    }

    @Operation(summary = "Crear nueva mascota", description = "Crea una nueva mascota asociada a un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Mascota creada exitosamente",
            content = @Content(mediaType = "application/json", 
            schema = @Schema(implementation = Pets.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o error de validación")
    })
    @PostMapping()
    public ResponseEntity<?> crearMascota(@RequestBody @Valid Pets pet) {
        try {
            Pets nuevaPet = petsService.guardarMascota(pet);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Buscar mascota por ID", description = "Obtiene una mascota específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mascota encontrada exitosamente",
            content = @Content(mediaType = "application/json", 
            schema = @Schema(implementation = Pets.class))),
        @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMascotaPorId(@PathVariable Long id) {
        try {
            Pets pet = petsService.buscarMascota(id);
            return ResponseEntity.ok(pet);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar mascota", description = "Actualiza la información de una mascota existente (acepta actualizaciones parciales)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mascota actualizada exitosamente",
            content = @Content(mediaType = "application/json", 
            schema = @Schema(implementation = Pets.class))),
        @ApiResponse(responseCode = "404", description = "Mascota no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMascota(
            @PathVariable Long id,
            @RequestBody @Valid Pets pet) {
        try {
            Pets petActualizada = petsService.actualizarMascota(id, pet);
            return ResponseEntity.ok(petActualizada);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar mascota", description = "Elimina una mascota del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Mascota eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMascota(@PathVariable Long id) {
        try {
            petsService.borrarMascota(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // METODOS DE BUSQUEDAS

    @Operation(summary = "Listar mascotas por usuario", description = "Obtiene todas las mascotas asociadas a un ID de usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mascotas encontradas exitosamente"),
        @ApiResponse(responseCode = "204", description = "No se encontraron mascotas para ese usuario")
    })
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<Pets>> listarMascotasPorUsuario(@PathVariable Long userId) {
        List<Pets> pets = petsService.listarMascotasPorUsuario(userId);
        if (pets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pets);
    }

    @Operation(summary = "Buscar mascotas por nombre", description = "Busca mascotas que contengan el nombre especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mascotas encontradas exitosamente"),
        @ApiResponse(responseCode = "204", description = "No se encontraron mascotas con ese nombre")
    })

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<Pets>> buscarPorNombre(@RequestParam String nombre) {
        List<Pets> pets = petsService.listarMascotas().stream()
                .filter(p -> p.getName().toLowerCase().contains(nombre.toLowerCase()))
                .toList(); 

        if (pets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pets);
    }
}