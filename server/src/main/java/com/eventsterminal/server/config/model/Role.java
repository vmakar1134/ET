package com.eventsterminal.server.config.model;


import javax.persistence.*;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "NAME", unique = true)
    private Roles name;

    public Role() {
    }

    public Role(Roles name) {
        this.name = name;
    }

    public void setName(Roles name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Roles getName() {
        return name;
    }

}
