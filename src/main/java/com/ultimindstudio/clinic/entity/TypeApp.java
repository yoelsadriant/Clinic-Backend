package com.ultimindstudio.clinic.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoel on 9/28/2016.
 */

@Entity
@Table(name = "type_app")
public class TypeApp {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String appType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

}
