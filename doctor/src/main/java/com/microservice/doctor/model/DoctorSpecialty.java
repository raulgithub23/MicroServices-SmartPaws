package com.microservice.doctor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "specialties")
public class DoctorSpecialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "specialty")
    private Set<DoctorModel> doctors = new HashSet<>();

    public DoctorSpecialty(String name, String description) {
        this.name = name;
        this.description = description;
    }
}