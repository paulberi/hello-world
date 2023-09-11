package com.rentals.carRentals.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "User")
public class User implements Serializable{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="User_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer userId;

	@Column(name="Name")
	private String name;

	@Column(name="Address")
	private String address;

	@Column(name="User_name")
	private String username;

	@Column(name="Password")
	private String password;
	
	private boolean enabled;

	@Column(name = "Role")
	@Convert(converter = UserEnumSwitch.class)
	private UserRoles role;
	
	
	@ManyToMany(fetch= FetchType.EAGER)
	@JoinTable(name="user_roles", joinColumns=@JoinColumn(name="User_id"),inverseJoinColumns=@JoinColumn(name="role_id"))
	private Set<Role> roles=new HashSet<>();
	
	@OneToMany(fetch =  FetchType.EAGER, mappedBy = "user",
			cascade={CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE})
	@JsonIgnore
	private List<Rental> userRentals=new ArrayList<>();
	
	public User() {
	}
	public User(String name, String address, String username, String password, UserRoles role, Set<Role> roles, List<Rental> userRentals) {
		this.name=name;
		this.address=address;
		this.username=username;
		this.password=password;
		this.role=role;
		this.roles=roles;
		this.userRentals=userRentals;
	}

	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
		this.password = passwordEncoder.encode(password);
	}
	public UserRoles getRole() {
		return role;
	}
	public void setRole(UserRoles role) {
		this.role = role;
	}
	
	public List<Rental> getUserRentals() {
		return userRentals;
	}
	public void setUserRentals(List<Rental> userRentals) {
		this.userRentals = userRentals;
	}

	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean hasRole(String roleName) {
        Iterator<Role> iterator = this.roles.iterator();
        while (iterator.hasNext()) {
            Role role = iterator.next();
            if (role.getRole().equals(roleName)) {
                return true;
            }
        }
         
        return false;
    }
	@Override
	public String toString() {
		return "User [Id=" + userId + ", name=" + name + ", address=" + address + ", username=" + username + ", password="
				+ password + ", role=" + role + "]";
	}
}
