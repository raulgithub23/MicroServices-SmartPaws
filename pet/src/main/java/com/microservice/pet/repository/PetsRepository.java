package com.microservice.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.pet.model.Pets;


@Repository
public interface PetsRepository extends JpaRepository<Pets, Long> {

    //  Busca todas las mascotas asociadas a un ID de usuario
    List<Pets> findByUserId(Long userId);

    // Busca una mascota específica por su ID y el ID de su propietario.
    // para asegurar que solo el dueño pueda acceder a su mascota.
    Optional<Pets> findByIdAndUserId(Long id, Long userId);

    // Por nombre para poder filtrar
    List<Pets> findByNameContainingIgnoreCase(String name);

}