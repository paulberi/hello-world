package com.jsf.entity;

public class Alien {
	
	private int aid;
	private String name;
	private String Tech;
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTech() {
		return Tech;
	}
	public void setTech(String tech) {
		Tech = tech;
	}
	@Override
	public String toString() {
		return "Alien [aid=" + aid + ", name=" + name + ", Tech=" + Tech + "]";
	}
	

}
