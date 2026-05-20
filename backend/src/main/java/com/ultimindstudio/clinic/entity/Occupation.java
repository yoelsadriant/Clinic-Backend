package com.ultimindstudio.clinic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "occupation")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Occupation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}
