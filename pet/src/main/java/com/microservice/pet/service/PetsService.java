package com.microservice.pet.service;


import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.pet.model.Pets;
import com.microservice.pet.repository.PetsRepository;

import java.util.List;
import java.util.Set;


@Service
@Transactional 
public class PetsService {

    @Autowired
    private PetsRepository petsRepository;
    
    private final Validator validator;

    public PetsService() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public List<Pets> listarMascotas() {
        return petsRepository.findAll();
    }

    public Pets buscarMascota(Long idPet) {
        return petsRepository.findById(idPet)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada en la base de datos"));
    }

    public List<Pets> listarMascotasPorUsuario(Long userId) {
        return petsRepository.findByUserId(userId);
    }
    
    public Pets guardarMascota(Pets pet) {
        Set<ConstraintViolation<Pets>> violations = validator.validate(pet);
        if (!violations.isEmpty()) {
            throw new RuntimeException("Error de validación: " + violations.iterator().next().getMessage());
        }
        return petsRepository.save(pet);
    }

    public String borrarMascota(Long idPet) {
        Pets petActual = buscarMascota(idPet); 
        petsRepository.deleteById(petActual.getId());
        return "Mascota Eliminada con éxito";
    }

    public Pets actualizarMascota(Long id, Pets petActualizada) {
        Pets petActual = buscarMascota(id);

        // Aplicar solo los cambios proporcionados
        if (petActualizada.getUserId() != null) {
            petActual.setUserId(petActualizada.getUserId());
        }
        if (petActualizada.getName() != null) {
            petActual.setName(petActualizada.getName());
        }
        if (petActualizada.getEspecie() != null) {
            petActual.setEspecie(petActualizada.getEspecie());
        }
        if (petActualizada.getFechaNacimiento() != null) {
            petActual.setFechaNacimiento(petActualizada.getFechaNacimiento());
        }
        if (petActualizada.getPeso() != null) {
            petActual.setPeso(petActualizada.getPeso());
        }
        if (petActualizada.getGenero() != null) {
            petActual.setGenero(petActualizada.getGenero());
        }
        if (petActualizada.getColor() != null) {
            petActual.setColor(petActualizada.getColor());
        }
        if (petActualizada.getNotas() != null) {
            petActual.setNotas(petActualizada.getNotas());
        }
        
        return guardarMascota(petActual);
    }

    public List<Pets> buscarPorNombre(String nombre) {
        return petsRepository.findByNameContainingIgnoreCase(nombre);
    }
}