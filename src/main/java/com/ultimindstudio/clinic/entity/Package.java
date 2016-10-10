package com.ultimindstudio.clinic.entity;

import javax.persistence.*;

/**
 * Created by Yoel on 9/28/2016.
 */
@Entity
@Table(name = "package")
public class Package {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String hargapackage;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHargapackage() {
        return hargapackage;
    }

    public void setHargapackage(String hargapackage) {
        this.hargapackage = hargapackage;
    }
}
