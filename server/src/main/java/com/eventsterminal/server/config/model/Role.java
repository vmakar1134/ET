package com.eventsterminal.server.config.model;


import javax.persistence.*;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Roles name;

    public Long getId() {
        return id;
    }

    public Roles getName() {
        return name;
    }

}
