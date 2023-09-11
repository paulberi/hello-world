package com.RestaurantGeoSearch.APP;

public class Address {
	
	private String Street1;
	private String street2;
	private String city;
	private String state;
	private String zipcode;
	private Location location;
	public String getStreet1() {
		return Street1;
	}
	public void setStreet1(String street1) {
		Street1 = street1;
	}
	public String getStreet2() {
		return street2;
	}
	public void setStreet2(String street2) {
		this.street2 = street2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
	

}
