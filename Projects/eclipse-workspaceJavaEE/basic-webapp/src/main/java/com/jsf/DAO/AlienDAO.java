package com.jsf.DAO;

import com.jsf.entity.Alien;

public class AlienDAO {
	
	public Alien getAlien(int aid) {
		Alien a=new Alien();
		
		a.setAid(101);
		a.setName("Navin");
		a.setTech("Java");
		return a;
	}

}
