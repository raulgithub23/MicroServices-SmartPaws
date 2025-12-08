package com.microservice.auth.service;

import com.microservice.auth.model.AuditLog;
import com.microservice.auth.model.User;
import com.microservice.auth.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Transactional
    public void logAction(String action, String entityType, Long entityId, String details, User user) {
        AuditLog log = new AuditLog(action, entityType, entityId, details, user);
        auditLogRepository.save(log);
    }

    public void logLogin(User user) {
        logAction("LOGIN", "USER", user.getId(), "Usuario inició sesión", user);
    }

    public void logPasswordReset(User user) {
        logAction("PASSWORD_RESET", "USER", user.getId(), "Contraseña restablecida", user);
    }

    public void logProfileUpdate(User user) {
        logAction("UPDATE_PROFILE", "USER", user.getId(), "Perfil actualizado", user);
    }
}