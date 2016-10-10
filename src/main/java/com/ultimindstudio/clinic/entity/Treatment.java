package com.ultimindstudio.clinic.entity;

import javax.persistence.*;

/**
 * Created by Yoel on 9/30/2016.
 */
@Entity
@Table(name = "treatment")
public class Treatment {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String hargatreatment;


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

    public String getHargatreatment() {
        return hargatreatment;
    }

    public void setHargatreatment(String hargatreatment) {
        this.hargatreatment = hargatreatment;
    }
}
