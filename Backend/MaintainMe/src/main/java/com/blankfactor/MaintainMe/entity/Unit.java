package com.blankfactor.MaintainMe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "unit")
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Column(name = "unit_number", nullable = false)
    private Integer unitNumber;

    @Column(name = "invoice_amount")
    private Float invoiceAmount;

    @Column(name = "sqm", nullable = false)
    private Float sqm;

    @Column(name = "ideal_sqm", nullable = false)
    private Float idealSqm;

    @Column(name = "residents")
    private Integer residents;

    @Column(name = "taxable_pets")
    private Integer taxablePets;


    @OneToMany(mappedBy = "unit", orphanRemoval = true)
    private List<User> users = new ArrayList<>();
    @JsonIgnore
    @ManyToMany(mappedBy = "units")
    private List<User> owners = new ArrayList<>();
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "unit_type_id", nullable = false)
    private UnitType unitType;

    @OneToMany(mappedBy = "unit", orphanRemoval = true)
    private List<Invoice> invoices = new ArrayList<>();

    @OneToMany(mappedBy = "unit", orphanRemoval = true)
    private List<Invitation> invitations = new ArrayList<>();

}