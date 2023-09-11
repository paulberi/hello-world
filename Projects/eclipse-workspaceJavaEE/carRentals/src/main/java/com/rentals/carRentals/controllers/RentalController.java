package com.rentals.carRentals.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rentals.carRentals.entity.Rental;
import com.rentals.carRentals.entity.RentalId;
import com.rentals.carRentals.exception.ResourceNotFoundException;
import com.rentals.carRentals.services.RentalService;

@RestController
@RequestMapping("/api/v1")
public class RentalController {
	
	Logger logger = LoggerFactory.getLogger(RentalController.class);

	@Autowired
	RentalService rentalService;
	
	 @GetMapping("/allorders")
	    public List<Rental> getAllRentals(){
	    	return rentalService.findAllRentals();
	    }
	 @GetMapping("/allordersByCarId/{carId}")
	    public List<Rental> getAllRentalsByCarId(@PathVariable(value="carId") Integer carId){
	    	return rentalService.findByCarId(carId);
	    }
	 @GetMapping("/myorders/{userId}")
	    public List<Rental> getAllRentalsByUserId(@PathVariable(value="userId") Integer userId){
	    	return rentalService.findByUserId(userId);
	    }
	 @RequestMapping(path="/{userId}:{carId}", method = RequestMethod.GET)
		public ResponseEntity<Rental> getRentalById(@PathVariable("userId") Integer userId,@PathVariable("carId") Integer carId) throws ResourceNotFoundException {
			return rentalService.findRentalById(userId, carId);
	 }
	 @RequestMapping(path = "/ordercar", method = RequestMethod.POST, consumes = "application/json")
	    public String getRental(@RequestBody 	Rental rental) throws ResourceNotFoundException {
	    	logger.info("order created");
	        return rentalService.addRental(rental);
	    }
	  @RequestMapping(path="/updateorder/{userId}:{carId}", method =RequestMethod.PUT)
		public ResponseEntity<Rental> updateRental(@PathVariable("userId") Integer userId, @PathVariable("carId") Integer carId, @RequestBody Rental rental) throws ResourceNotFoundException{
		  logger.info("order updated");
		  return rentalService.updateRental(userId, carId, rental);
			
		}
	  @RequestMapping(path="/allUserRentals/user/delete/{id}", method=RequestMethod.DELETE)
		public ResponseEntity<?> deleteRental(@PathVariable("id") RentalId id) throws ResourceNotFoundException {
			return rentalService.deleteRental(id);
			
		}
}
