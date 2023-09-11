package com.poortoys.examples;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(HelloLive.class)
public class HelloBean implements HelloLive {
    
    @Override
	public String getMessage() {
        return "Hello, world LIVE !!";
    }
    
}