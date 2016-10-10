package com.ultimindstudio.clinic.entity;

import javax.persistence.*;

/**
 * Created by Yoel on 9/28/2016.
 */
@Entity
@Table(name = "login")
public class Login {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_type_app", nullable = false)
    private TypeApp typeApp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TypeApp getTypeApp() {
        return typeApp;
    }

    public void setTypeApp(TypeApp typeApp) {
        this.typeApp = typeApp;
    }



}
