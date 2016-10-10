package com.ultimindstudio.clinic.entity;

import javax.persistence.*;

/**
 * Created by Yoel on 9/22/2016.
 */
@Entity
@Table(name = "sales")
public class Sales {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_staff", nullable = false)
    private Staff staff;

    @Column(nullable = false)
    private String date;

    @ManyToOne
    @JoinColumn(name = "id_type_payment", nullable = false)
    private TypePayment typePayment;

    @ManyToOne
    @JoinColumn(name = "id_type_income", nullable = false)
    private TypeIncome typeIncome;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TypePayment getTypePayment() {
        return typePayment;
    }

    public void setTypePayment(TypePayment typePayment) {
        this.typePayment = typePayment;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public TypeIncome getTypeIncome() {
        return typeIncome;
    }

    public void setTypeIncome(TypeIncome typeIncome) {
        this.typeIncome = typeIncome;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
