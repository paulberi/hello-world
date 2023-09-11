package application.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.UpdateTimestamp;

import application.extra.GetId;

@Entity
@Table(name = "address")
public class Address implements GetId {

    @Id
    @Column(name = "address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "address")
    private String address;

    @Column(name = "address2")
    private String address2;

    @Column(name = "district")
    private String district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "phone")
    private String phone;

    @Column(name = "location")
    @ColumnTransformer(
            read = "ST_AsText(location)",
            write = "ST_GeomFromText(?)"
    )
    private String location;

    @Column(name = "last_update")
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    public Address() {
        // weee
    }

    // public Address(String address, String address2, int district, int postalCode,
    // int phone, Point location,
    // LocalDate lastUpdate) {
    // super();
    // this.address = address;
    // this.address2 = address2;
    // this.district = district;
    // this.postalCode = postalCode;
    // this.phone = phone;
    // this.location = location;
    // this.lastUpdate = lastUpdate;
    // }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {


        this.location = location;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getXCoordinate() {
        return this.location.split(" ")[0].substring(6);
    }

    public String getYCoordinate() {
        return this.location.split(" ")[1].replace(")", "");
    }

    @Override
    public String toString() {
        return address;
    }

}
