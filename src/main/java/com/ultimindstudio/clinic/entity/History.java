package com.ultimindstudio.clinic.entity;

import javax.persistence.*;

/**
 * Created by Yoel on 9/22/2016.
 */
@Entity
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String date;

    @ManyToOne
    @JoinColumn(name = "id_staff", nullable = false)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "id_client_package", nullable = false)
    private ClientPackage clientPackage;

    @ManyToOne
    @JoinColumn(name = "id_treatment", nullable = false)
    private Treatment treatment;

    @Column
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public ClientPackage getClientPackage() {
        return clientPackage;
    }

    public void setClientPackage(ClientPackage clientPackage) {
        this.clientPackage = clientPackage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }
}
