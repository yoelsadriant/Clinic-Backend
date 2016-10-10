package com.ultimindstudio.clinic.entity;

import javax.persistence.*;

/**
 * Created by Yoel on 9/22/2016.
 */
@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_package", nullable = false)
    private Package pkg;

    @ManyToOne
    @JoinColumn(name = "id_treatment", nullable = false)
    private Treatment treatment;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String time;

    @ManyToOne
    @JoinColumn(name = "id_status", nullable = false)
    private StatusClient status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Package getPkg() {
        return pkg;
    }

    public void setPkg(Package pkg) {
        this.pkg = pkg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public StatusClient getStatus() {
        return status;
    }

    public void setStatus(StatusClient status) {
        this.status = status;
    }

}
