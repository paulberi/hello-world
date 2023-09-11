package com.company.entities;

import javax.persistence.*;

@Entity
@Table(name = "ADDRESS")
public class Address {

    @Id
    @Column(name = "ADDRESS_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "STREET_NAME")
    private String streetName;

    @Column(name = "STREET_NUMBER")
    private int streetNumber;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    public Address() {
    }

    public Address(String streetName, int streetNumber) {
        this.streetName = streetName;
        this.streetNumber = streetNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
