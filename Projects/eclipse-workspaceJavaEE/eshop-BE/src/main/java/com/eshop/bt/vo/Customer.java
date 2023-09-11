package com.eshop.bt.vo;

//@Stateful

public class Customer {
	
	private String name;
	private String email;
	private String password;
	private String address;
	private long customerId;
	
	public Customer() {	}
	
	public Customer(long customerId, String name, String email, String password, String address) {
		super();
		this.customerId=customerId;
		this.name=name;
		this.email=email;
		this.password=password;
		this.address=address;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
