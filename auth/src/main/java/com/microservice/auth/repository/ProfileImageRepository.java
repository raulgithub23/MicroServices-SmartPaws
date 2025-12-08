package com.microservice.auth.repository;

import com.microservice.auth.model.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
    Optional<ProfileImage> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}