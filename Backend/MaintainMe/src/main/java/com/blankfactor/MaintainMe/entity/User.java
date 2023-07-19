package com.blankfactor.MaintainMe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;



@Entity
@Builder
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "unit_owner",
            joinColumns = @JoinColumn(name = "user_id", nullable = true),
            inverseJoinColumns = @JoinColumn(name = "units_id", nullable = true))
    private List<Unit> units = new ArrayList<>();


    @JsonIgnore
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<UserRoleBuilding> userRoleBuildings = new ArrayList<>();

    @Column(name = "auto_pay")
    private Integer autoPay;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Invitation> invitations = new ArrayList<>();

    public List<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
    }

    public Integer getAutoPay() {
        return autoPay;
    }

    public void setAutoPay(Integer autoPay) {
        this.autoPay = autoPay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public List<UserRoleBuilding> getUserRoleBuildings() {
        return userRoleBuildings;
    }

    public void setUserRoleBuildings(List<UserRoleBuilding> userRoleBuildings) {
        this.userRoleBuildings = userRoleBuildings;
    }
}