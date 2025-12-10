package com.microservice.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.pet.model.Pets;


@Repository
public interface PetsRepository extends JpaRepository<Pets, Long> {

    List<Pets> findByUserId(Long userId);

    Optional<Pets> findByIdAndUserId(Long id, Long userId);

    List<Pets> findByNameContainingIgnoreCase(String name);

}