package com.poortoys.examples;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(Hello.class)
public class HelloBean implements Hello{
    
    public String getMessage() {
        return "Hello, world";
    }
    
}