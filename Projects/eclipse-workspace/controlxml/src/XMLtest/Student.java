package XMLtest;

import java.io.Serializable;

public class Student implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1962866888082163758L;
	String name;
	int id;
	String courses;
	
	public Student() {
		
	}
	public Student(String name, int id, String courses) {
		this.name=name;
		this.id=id;
		this.courses=courses;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCourses() {
		return courses;
	}
	public void setCourses(String courses) {
		this.courses = courses;
	}

}
