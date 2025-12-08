package com.microservice.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profile_images")
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long fileSize;

    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] imageData;

    @Column(nullable = false)
    private LocalDateTime uploadDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public ProfileImage(String fileName, String contentType, Long fileSize, byte[] imageData, User user) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.imageData = imageData;
        this.user = user;
        this.uploadDate = LocalDateTime.now();
    }
}