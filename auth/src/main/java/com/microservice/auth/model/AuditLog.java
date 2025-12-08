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
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action; 

    @Column(nullable = false)
    private String entityType;

    private Long entityId;

    @Column(length = 1000)
    private String details;

    private String ipAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public AuditLog(String action, String entityType, Long entityId, String details, User user) {
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.details = details;
        this.user = user;
    }
}