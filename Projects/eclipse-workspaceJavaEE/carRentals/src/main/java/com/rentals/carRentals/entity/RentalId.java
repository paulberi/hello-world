package com.rentals.carRentals.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RentalId implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "Car_id")
	private Integer carId;
	
	@Column(name="User_id")
	private Integer userId;

	public RentalId(Integer carId, Integer userId) {
		this.carId=carId;
		this.userId=userId;
	}

	public RentalId() {
		// TODO Auto-generated constructor stub
	}

	public Integer getCarId() {
		return carId;
	}

	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((carId == null) ? 0 : carId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RentalId other = (RentalId) obj;
		if (carId == null) {
			if (other.carId != null)
				return false;
		} else if (!carId.equals(other.carId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}


	
}
