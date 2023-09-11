package com.yajava.ejb;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(Hello.class)
public class HelloBean implements Hello{
    
    public String echo(String str) {
        return str;
    }

	@Override
	public String echoReverse(String str) {
		return new StringBuilder(str).reverse().toString();
	}
    
}