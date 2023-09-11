package com.rentals.carRentals.entity;

import javax.persistence.AttributeConverter;

public class CarBrandsSwitch implements AttributeConverter<CarBrands,String>{

	@Override
	public String convertToDatabaseColumn(CarBrands attribute) {
		// TODO Auto-generated method stub
		 if (attribute == null) {
	            return null;
	        }
		switch (attribute) {
		case Volvo:
			return "Volvo";
		case Toyota:
			return "Toyota";
		case Mercedes:
			return "Mercedes";
		case Mitsubishi:
			return "Mitsubishi";
			
		default:
			throw new IllegalArgumentException(attribute + " not supported.");
		}
	}

	@Override
	public CarBrands convertToEntityAttribute(String dbData) {
		// TODO Auto-generated method stub
		 if (dbData == null) {
	            return null;
	        }
	        switch (dbData) {
	        case "Volvo":
	            return CarBrands.Volvo;
	        case "Toyota":
	            return CarBrands.Toyota;
	        case "Mercedes":
	            return CarBrands.Mercedes;
	        case "Mitsubishi":
	            return CarBrands.Mitsubishi;
	       
	        default:
	            throw new IllegalArgumentException(dbData + " not supported.");
	        }
	}

}
