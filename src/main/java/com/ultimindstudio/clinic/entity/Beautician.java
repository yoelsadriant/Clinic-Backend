package com.ultimindstudio.clinic.entity;

import javax.persistence.*;

/**
 * Created by Yoel on 9/30/2016.
 */
@Entity
@Table(name = "beautician")
public class Beautician {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_staff", nullable = false)
    private Staff staff;

    private int quantity;

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
