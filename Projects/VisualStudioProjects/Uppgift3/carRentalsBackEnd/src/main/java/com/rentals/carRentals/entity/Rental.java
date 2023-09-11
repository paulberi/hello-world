package com.rentals.carRentals.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "Rental")
public class Rental implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@Column(name = "Rental_id")
	private RentalId rentalId = new RentalId();


	@ManyToOne(fetch=FetchType.LAZY)
	@MapsId("userId")
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name = "User_Id", nullable = false)
	//@JsonIgnore
	private User user;


	@ManyToOne(fetch=FetchType.LAZY)
	@MapsId("carId")
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name = "Car_Id",  nullable = false)
	//@JsonIgnore
	private Car car;

	

	@Temporal(TemporalType.DATE)
	@Column(name = "Start_Date")
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "Booking_Date")
	private Date dateOfBooking;

	public Rental() {

	}

	public Rental(User user, Car car,  Date startDate, Date dateOfBooking) {
		this.user = user;
		this.car = car;
//		this.userId=userId;
//		this.carId=carId;
		this.startDate = startDate;
		this.dateOfBooking = dateOfBooking;
	}
	public RentalId getRentalId() {
		return rentalId;
	}

	public void setRentalId(RentalId rentalId) {
		this.rentalId = rentalId;
	}



	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}



	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getDateOfBooking() {
		return dateOfBooking;
	}

	public void setDateOfBooking(Date dateOfBooking) {
		this.dateOfBooking = dateOfBooking;
	}

	@Override
	public String toString() {
		return "Rental [rentalId=" + rentalId + ", user=" + user + ", car=" + car + ", startDate=" + startDate
				+ ", dateOfBooking=" + dateOfBooking + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((car == null) ? 0 : car.hashCode());
		result = prime * result + ((dateOfBooking == null) ? 0 : dateOfBooking.hashCode());
		result = prime * result + ((rentalId == null) ? 0 : rentalId.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Rental other = (Rental) obj;
		if (car == null) {
			if (other.car != null)
				return false;
		} else if (!car.equals(other.car))
			return false;
		if (dateOfBooking == null) {
			if (other.dateOfBooking != null)
				return false;
		} else if (!dateOfBooking.equals(other.dateOfBooking))
			return false;
		if (rentalId == null) {
			if (other.rentalId != null)
				return false;
		} else if (!rentalId.equals(other.rentalId))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}
