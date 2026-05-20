package com.ultimindstudio.clinic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "client_package_detail")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClientPackageDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_package_id", nullable = false)
    private ClientPackage clientPackage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_detail_id", nullable = false)
    private PackageDetail packageDetail;

    @Column(nullable = false)
    private int quantity;
}
