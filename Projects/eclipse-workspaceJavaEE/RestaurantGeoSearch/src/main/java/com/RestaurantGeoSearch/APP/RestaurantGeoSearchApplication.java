package com.RestaurantGeoSearch.APP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages= {"com.RestaurantGeoSearch"})
public class RestaurantGeoSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantGeoSearchApplication.class, args);
	}

}
