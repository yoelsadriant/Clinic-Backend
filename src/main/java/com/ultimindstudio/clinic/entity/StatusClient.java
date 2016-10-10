package com.ultimindstudio.clinic.entity;

import javax.persistence.*;

/**
 * Created by Yoel on 10/3/2016.
 */
@Entity
@Table(name = "status_client")
public class StatusClient {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
