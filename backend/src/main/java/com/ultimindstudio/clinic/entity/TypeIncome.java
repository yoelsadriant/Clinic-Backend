package com.ultimindstudio.clinic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "type_income")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TypeIncome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}
