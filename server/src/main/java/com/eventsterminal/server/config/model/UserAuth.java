package com.eventsterminal.server.config.model;


import com.eventsterminal.server.domain.UserProfile;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserAuth extends UserPrincipal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String username;

    private String password;

    @Column(columnDefinition = "NUMERIC(25)")
    private BigInteger socialId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "usersRoles",
            joinColumns = @JoinColumn(name = "userAuth_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    @OneToOne(mappedBy = "userAuth", cascade = CascadeType.ALL)
    private UserProfile userProfile;

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getSocialId() {
        return socialId;
    }

    public void setSocialId(BigInteger socialId) {
        this.socialId = socialId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String login) {
        this.username = login;
    }

}
