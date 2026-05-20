package com.ultimindstudio.clinic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "type_payment")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TypePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}
