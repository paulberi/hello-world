package com.rentals.carRentals.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Car")
public class Car implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "Car_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer carId;

	@Column(name = "Model")
	@Convert(converter = CarBrandsSwitch.class)
	private CarBrands carBrands;
	
	@Column(name="ModelType")
	private String name;

	@Column(name="Passengers")
	private int passengers;

	@Column(name="Price_per_day")
	private double price;
	
	@OneToMany(fetch =  FetchType.EAGER, mappedBy = "car",
			cascade={CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE})
	@JsonIgnore
	private List<Rental> carRentals;
	
	public Car() {
		carRentals=new ArrayList<>();
	}
	public Car(CarBrands carBrands, String name, int passengers, double price) {
		this.carBrands=carBrands;
		this.name=name;
		this.passengers=passengers;
		this.price=price;
	}
	
	public Integer getCarId() {
		return carId;
	}
	public void setCarId(Integer carId) {
		this.carId = carId;
	}
	public CarBrands getCarBrands() {
		return carBrands;
	}
	public void setCarBrands(CarBrands carBrands) {
		this.carBrands = carBrands;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPassengers() {
		return passengers;
	}
	public void setPassengers(int passengers) {
		this.passengers = passengers;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public List<Rental> getCarRentals() {
		return carRentals;
	}
	public void setCarRentals(List<Rental> carRentals) {
		this.carRentals = carRentals;
	}
	@Override
	public String toString() {
		return "Car [Id=" + carId + ", carBrands=" + carBrands + ", name=" + name + ", passengers=" + passengers
				+ ", price=" + price + "]";
	}
	
}
