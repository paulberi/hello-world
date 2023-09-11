package com.poortoys.examples;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(Hello.class)
public class HelloBean implements Hello{

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "hello world";
	}
    
   
    
}