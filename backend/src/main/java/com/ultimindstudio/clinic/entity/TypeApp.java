package com.ultimindstudio.clinic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "type_app")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TypeApp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_type", nullable = false, unique = true)
    private String appType;
}
