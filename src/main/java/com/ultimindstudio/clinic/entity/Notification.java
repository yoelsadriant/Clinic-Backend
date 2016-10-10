package com.ultimindstudio.clinic.entity;

import javax.persistence.*;

/**
 * Created by Yoel on 10/6/2016.
 */
@Entity
@Table(name = "notification")
@NamedQuery(name = "Notification.findByDate",
        query = "select n from Notification n where n.datetime = ?1")
public class Notification {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String datetime;

    @Column
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
