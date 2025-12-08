package com.microservice.auth.service;

import com.microservice.auth.model.ProfileImage;
import com.microservice.auth.model.User;
import com.microservice.auth.repository.ProfileImageRepository;
import com.microservice.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

@Service
public class ImageService {

    @Autowired
    private ProfileImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB

    @Transactional
    public void uploadProfileImage(Long userId, String fileName, String contentType, String imageBase64) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar tipo de archivo
        if (!contentType.startsWith("image/")) {
            throw new RuntimeException("El archivo debe ser una imagen");
        }

        // Decodificar Base64
        byte[] imageData;
        try {
            imageData = Base64.getDecoder().decode(imageBase64);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Formato Base64 inválido");
        }

        // Validar tamaño
        if (imageData.length > MAX_IMAGE_SIZE) {
            throw new RuntimeException("La imagen no debe superar los 5MB");
        }

        // Eliminar imagen anterior si existe
        imageRepository.findByUserId(userId).ifPresent(imageRepository::delete);

        // Guardar nueva imagen
        ProfileImage profileImage = new ProfileImage(
            fileName,
            contentType,
            (long) imageData.length,
            imageData,
            user
        );

        imageRepository.save(profileImage);
    }

    @Transactional(readOnly = true)
    public ProfileImage getProfileImage(Long userId) {
        return imageRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("El usuario no tiene imagen de perfil"));
    }

    @Transactional
    public void deleteProfileImage(Long userId) {
        imageRepository.deleteByUserId(userId);
    }

    public String convertToBase64(byte[] imageData) {
        return Base64.getEncoder().encodeToString(imageData);
    }
}