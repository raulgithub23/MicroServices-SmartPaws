package com.microservice.auth.config;

import com.microservice.auth.model.Role;
import com.microservice.auth.model.User;
import com.microservice.auth.repository.RoleRepository;
import com.microservice.auth.repository.UserRepository;
import com.microservice.auth.service.AuthService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthInitData {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Bean
    @Transactional
    public CommandLineRunner initAuthDatabase() {
        return args -> {
            // Crear roles si no existen
            if (roleRepository.count() == 0) {
                System.out.println("Creando roles del sistema...");
                
                Role userRole = new Role("USER", "Usuario estándar del sistema");
                Role adminRole = new Role("ADMIN", "Administrador con acceso completo");
                Role doctorRole = new Role("DOCTOR", "Doctor veterinario");
                
                roleRepository.save(userRole);
                roleRepository.save(adminRole);
                roleRepository.save(doctorRole);
                
                System.out.println("Roles creados exitosamente");
            }

            // Crear usuarios iniciales si no existen
            if (userRepository.count() == 0) {
                System.out.println("Creando usuarios iniciales...");

                Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));
                Role doctorRole = roleRepository.findByName("DOCTOR")
                    .orElseThrow(() -> new RuntimeException("Rol DOCTOR no encontrado"));
                Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

                // ADMIN 1
                User admin1 = new User();
                admin1.setName("Gabriel Vidal"); // Unificado
                admin1.setEmail("ga.vidal@duocuc.cl");
                admin1.setPhone("20123123-2");   // Variable phone original
                admin1.setPassword("password123");
                admin1.addRole(adminRole);
                authService.register(admin1);

                // ADMIN 2
                User admin2 = new User();
                admin2.setName("Raul Fernandez"); // Unificado
                admin2.setEmail("ra.fernandez@duocuc.cl");
                admin2.setPhone("20456789-5");    // Variable phone original
                admin2.setPassword("123");
                admin2.addRole(adminRole);
                authService.register(admin2);

                // DOCTOR 1
                User doctor1 = new User();
                doctor1.setName("Carlos Méndez");
                doctor1.setEmail("carlos.mendez@smartpaws.cl");
                doctor1.setPhone("+56912345678");
                doctor1.setPassword("Doctor123!");
                doctor1.addRole(doctorRole);
                authService.register(doctor1);

                // DOCTOR 2
                User doctor2 = new User();
                doctor2.setName("María González");
                doctor2.setEmail("maria.gonzalez@smartpaws.cl");
                doctor2.setPhone("+56987654321");
                doctor2.setPassword("Doctor123!");
                doctor2.addRole(doctorRole);
                authService.register(doctor2);

                // DOCTOR 3
                User doctor3 = new User();
                doctor3.setName("Jorge Silva");
                doctor3.setEmail("jorge.silva@smartpaws.cl");
                doctor3.setPhone("+56911223344");
                doctor3.setPassword("Doctor123!");
                doctor3.addRole(doctorRole);
                authService.register(doctor3);

                // DOCTOR 4
                User doctor4 = new User();
                doctor4.setName("Ana Rojas");
                doctor4.setEmail("ana.rojas@smartpaws.cl");
                doctor4.setPhone("+56922334455");
                doctor4.setPassword("Doctor123!");
                doctor4.addRole(doctorRole);
                authService.register(doctor4);

                // DOCTOR 5
                User doctor5 = new User();
                doctor5.setName("Luis Pérez");
                doctor5.setEmail("luis.perez@smartpaws.cl");
                doctor5.setPhone("+56933445566");
                doctor5.setPassword("Doctor123!");
                doctor5.addRole(doctorRole);
                authService.register(doctor5);

                // ADMIN BASE
                User adminBase = new User();
                adminBase.setName("Administrador Sistema");
                adminBase.setEmail("a@a.cl");
                adminBase.setPhone("12345678");
                adminBase.setPassword("Admin123!");
                adminBase.addRole(adminRole);
                authService.register(adminBase);

                // USER BASE
                User userBase = new User();
                userBase.setName("Jose Usuario");
                userBase.setEmail("b@b.cl");
                userBase.setPhone("12345678");
                userBase.setPassword("Jose123!");
                userBase.addRole(userRole);
                authService.register(userBase);

                System.out.println("Usuarios iniciales creados exitosamente");
                System.out.println("Total de usuarios: " + userRepository.count());
            }
        };
    }
}