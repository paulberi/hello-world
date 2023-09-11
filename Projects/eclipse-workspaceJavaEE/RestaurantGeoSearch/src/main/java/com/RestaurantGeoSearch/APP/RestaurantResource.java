package com.RestaurantGeoSearch.APP;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/restaurants")
public class RestaurantResource {
	
	@Autowired
	RestaurantGeoSearchService service;
	
	@RequestMapping(method=RequestMethod.POST)
	public void create(@RequestBody RestaurantRepresentation restaurantRep) {
		service.createRestaurant(restaurantRep);
	}
	
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Restaurant> findAll(){
		return service.findAll();
		
	}
	@RequestMapping(value="/findByDistance/{long}/{lat}/{dist}", method=RequestMethod.GET)
	public List<Restaurant> findByDistance(@PathVariable(value="long") float longitude, @PathVariable(value="lat") float latitude,@PathVariable(value="dist") int distance){
		return service.findByDistance(longitude, latitude, distance);
		
	}
}
