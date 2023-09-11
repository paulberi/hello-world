package com.RestaurantGeoSearch.APP;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class RestaurantGeoSearchService {

	@Autowired
	MongoOperations mongoOperations;
	public void createRestaurant(RestaurantRepresentation restaurant) {
		
		Restaurant  restaurantPersistence=new Restaurant();
		restaurantPersistence.setName(restaurant.getName());
		restaurantPersistence.setAddress(restaurant.getAddress());
		
		mongoOperations.save(restaurantPersistence);
		
		
	}
	
	public List<Restaurant> findAll(){
		return mongoOperations.findAll(Restaurant.class);
	
		
	}
	public List<Restaurant> findByDistance(float longitude, float latitude, int distance){
		
		Point basePoint=new Point(longitude,latitude);
		
		Distance radius =new Distance(distance, Metrics.KILOMETERS);
		
		Circle area=new Circle(basePoint, radius);
		
		Query query= new Query();
		
		query.addCriteria(Criteria.where("address.location").withinSphere(area));
		
		return mongoOperations.find(query, Restaurant.class);
		
	}
	
}
