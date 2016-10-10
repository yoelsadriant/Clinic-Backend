package com.ultimindstudio.clinic.entity;

import javax.persistence.*;

/**
 * Created by Yoel on 9/30/2016.
 */
@Entity
@Table(name = "client_package_detail")
public class ClientPackageDetail {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_client_package", nullable =  false)
    private ClientPackage clientPackage;

    @ManyToOne
    @JoinColumn(name = "id_package_detail", nullable = false)
    private PackageDetail packageDetail;



    @Column
    private int quantityclientpackagedetail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientPackage getClientPackage() {
        return clientPackage;
    }

    public void setClientPackage(ClientPackage clientPackage) {
        this.clientPackage = clientPackage;
    }

    public PackageDetail getPackageDetail() {
        return packageDetail;
    }

    public void setPackageDetail(PackageDetail packageDetail) {
        this.packageDetail = packageDetail;
    }

    public int getQuantityclientpackagedetail() {
        return quantityclientpackagedetail;
    }


    public void setQuantityclientpackagedetail(int quantityclientpackagedetail) {
        this.quantityclientpackagedetail = quantityclientpackagedetail;
    }

}
