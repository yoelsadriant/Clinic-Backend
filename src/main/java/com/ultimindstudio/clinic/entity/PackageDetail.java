package com.ultimindstudio.clinic.entity;

import javax.persistence.*;

/**
 * Created by Yoel on 9/30/2016.
 */
@Entity
@Table(name = "package_detail")
public class PackageDetail {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_treatment", nullable = false)
    private Treatment treatment;

    @ManyToOne
    @JoinColumn(name = "id_package", nullable = false)
    private Package pkg;



    @Column
    private int quantitypaketdetail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }

    public Package getPkg() {
        return pkg;
    }

    public void setPkg(Package pkg) {
        this.pkg = pkg;
    }

    public int getQuantitypaketdetail() {
        return quantitypaketdetail;
    }

    public void setQuantitypaketdetail(int quantitypaketdetail) {
        this.quantitypaketdetail = quantitypaketdetail;
    }
}
