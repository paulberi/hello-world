package com.yajava.ejb;

import javax.ejb.Remote;
import javax.ejb.Stateful;

@Stateful
@Remote(Hello.class)
public class HelloEJB implements Hello{

	private String name;
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String sayHello() {
		return "Hello " + name;
	}

}
