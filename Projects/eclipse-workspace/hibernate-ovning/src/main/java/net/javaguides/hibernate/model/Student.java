package net.javaguides.hibernate.model;

import javax.persistence.Entity;

@Entity
@Table(name = "student")
public class Student {
	
	private long id;
	private String firstName;
	private String lastName;
	private String email;
	
	
	public Student() {
	}
	public Student(long id, String firstName, String lastName, String email) {
		
		@Id
		@GeneratedValue(Strategy = GenerationType.AUTO)
		@Column(name = "id")
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

}
