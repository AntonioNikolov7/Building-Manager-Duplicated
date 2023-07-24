package com.blankfactor.MaintainMe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "building")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;


    @OneToOne(fetch =FetchType.EAGER, cascade =CascadeType.ALL,optional = false, orphanRemoval = true)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "floors", nullable = false)
    private Integer floors;

    @Column(name = "entrances", nullable = false)
    private Integer entrances;

    @OneToMany(mappedBy = "building", orphanRemoval = true)
    private List<Unit> units = new ArrayList<>();


    @OneToMany(mappedBy = "building", orphanRemoval = true)
    private List<UserRoleBuilding> userRoleBuildings = new ArrayList<>();

    @OneToMany(mappedBy = "building", orphanRemoval = true)
    private List<Invitation> invitations = new ArrayList<>();

}