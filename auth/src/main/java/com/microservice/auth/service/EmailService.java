package com.microservice.auth.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    /**
     * En producción, aquí usarías JavaMailSender o un servicio externo como SendGrid
     * Por ahora, solo imprimimos en consola para desarrollo
     */
    public void sendPasswordResetEmail(String email, String token) {
        String resetLink = "http://localhost:8081/auth/reset-password?token=" + token;
        
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("    EMAIL DE RECUPERACIÓN DE CONTRASEÑA");
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("Para: " + email);
        System.out.println("Asunto: Recuperación de Contraseña - SmartPaws");
        System.out.println("\nHola,");
        System.out.println("\nHemos recibido una solicitud para restablecer tu contraseña.");
        System.out.println("\nHaz clic en el siguiente enlace para crear una nueva contraseña:");
        System.out.println(resetLink);
        System.out.println("\nEste enlace expirará en 1 hora.");
        System.out.println("\nSi no solicitaste este cambio, puedes ignorar este mensaje.");
        System.out.println("\nSaludos,");
        System.out.println("Equipo SmartPaws");
        System.out.println("═══════════════════════════════════════════════\n");
    }
}