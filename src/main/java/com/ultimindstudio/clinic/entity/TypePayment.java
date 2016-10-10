package com.ultimindstudio.clinic.entity;

import javax.persistence.*;

/**
 * Created by Yoel on 10/3/2016.
 */
@Entity
@Table(name = "type_payment")
public class TypePayment {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String typePayment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypePayment() {
        return typePayment;
    }

    public void setTypePayment(String typePayment) {
        this.typePayment = typePayment;
    }
}
