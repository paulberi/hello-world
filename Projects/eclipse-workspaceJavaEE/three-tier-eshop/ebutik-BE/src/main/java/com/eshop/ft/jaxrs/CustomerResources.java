package com.eshop.ft.jaxrs;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.eshop.bt.to.CustomerService;
import com.eshop.bt.to.ServiceDAO;
import com.eshop.bt.vo.Customer;
import com.eshop.dt.eo.CustomerEO;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;



@Path("/customers")
public class CustomerResources implements ServiceDAO<Customer,CustomerEO>{
	
	@Context
    UriInfo uriInfo;
	
	
	@Inject
	private CustomerService customerService;

	@GET
	public String test() {
		return "warup";
	}



	@Path("/postCustomer")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Customer create(CustomerEO t) throws SQLException {
		// TODO Auto-generated method stub
		return customerService.create(t);
	}

	@Path("/getCustomers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Customer> findAll() throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("hello");
		return customerService.findAll();
	}

	@Path("/{customerId}")//when you use a variable in the path u use curly braces to differentiate it from an ordinary path name
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Customer find(@PathParam("customerId") long id) throws SQLException {
		// TODO Auto-generated method stub
		return customerService.find(id);
	}

	@Path("/{customerId}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Customer update(@PathParam("customerId") long customerId, CustomerEO customer) throws SQLException {
		// TODO Auto-generated method stub
		customer.setCustomerId(customerId);
		return customerService.update(customerId,customer);
	}

	@Path("/{customerId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Customer delete(@PathParam("customerId")long id) throws SQLException {
		// TODO Auto-generated method stub
		return customerService.delete(id);
	}

}

