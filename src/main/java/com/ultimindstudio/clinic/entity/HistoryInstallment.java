package com.ultimindstudio.clinic.entity;

import javax.persistence.*;

/**
 * Created by Yoel on 9/22/2016.
 */
@Entity
@Table(name = "history_installment")
public class HistoryInstallment {
    @Id
    @GeneratedValue
    private Long id;;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @Column
    private String date;

    @Column
    private int payment;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }



}
