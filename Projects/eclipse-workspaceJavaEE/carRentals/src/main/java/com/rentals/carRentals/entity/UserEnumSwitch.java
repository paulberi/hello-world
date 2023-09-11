package com.rentals.carRentals.entity;

import javax.persistence.AttributeConverter;

public class UserEnumSwitch implements AttributeConverter<UserRoles,String>{

	
	@Override
	public String convertToDatabaseColumn(UserRoles attribute) {
		// TODO Auto-generated method stub
		 if (attribute == null) {
	            return null;
	        }
		switch (attribute) {
		case Admin:
			return "Admin";
		case Customer:
			return "Customer";
		default:
			throw new IllegalArgumentException(attribute + " not supported.");
		}
	}

	@Override
	public UserRoles convertToEntityAttribute(String dbData) {
		// TODO Auto-generated method stub
		 if (dbData == null) {
	            return null;
	        }
	        switch (dbData) {
	        case "Admin":
	            return UserRoles.Admin;
	        case "Customer":
	            return UserRoles.Customer;
	        default:
	            throw new IllegalArgumentException(dbData + " not supported.");
	        }
	}

}
