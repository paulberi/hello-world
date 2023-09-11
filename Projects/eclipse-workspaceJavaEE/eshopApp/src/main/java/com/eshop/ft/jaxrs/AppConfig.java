package com.eshop.ft.jaxrs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationPath("resources")
public class AppConfig extends Application{
	

	
	private Set<Class<?>>resources=new HashSet<>();
	
	public AppConfig() {
		System.out.println("created AppConfig");
		//resources.add(Resources1.class);
		resources.add(CustomerResources.class);
		resources.add(OrderResources.class);
		resources.add(ProductResources.class);
	}
	
	
	public Set<Class<?>> getClasses() {
		return resources;
	}
}

	
