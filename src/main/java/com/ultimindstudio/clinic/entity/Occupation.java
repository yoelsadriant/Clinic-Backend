package com.ultimindstudio.clinic.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoel on 9/22/2016.
 */
@Entity
@Table(name = "occupation")
public class Occupation {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

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

}
