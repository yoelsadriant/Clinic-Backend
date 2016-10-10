package com.ultimindstudio.clinic.entity;

import javax.persistence.*;

/**
 * Created by Yoel on 10/3/2016.
 */
@Entity
@Table(name = "type_income")
public class TypeIncome {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String typeIncome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeIncome() {
        return typeIncome;
    }

    public void setTypeIncome(String typeIncome) {
        this.typeIncome = typeIncome;
    }
}
