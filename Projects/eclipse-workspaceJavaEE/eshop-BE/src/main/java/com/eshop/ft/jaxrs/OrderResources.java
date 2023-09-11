package com.eshop.ft.jaxrs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.eshop.bt.to.OrderService;
import com.eshop.bt.to.ServiceDAO;
import com.eshop.bt.vo.Customer;
import com.eshop.bt.vo.Order;
import com.eshop.bt.vo.Product;
import com.eshop.dt.eo.OrderEO;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;


@Path("/orders")
public class OrderResources implements ServiceDAO<Order, OrderEO> {

	@Context
	UriInfo uriInfo;

	@Inject
	private OrderService orderService;// =new OrderService();

	@Inject
	private CustomerResources cust;//= new CustomerResources();

	@Inject
	private ProductResources prod;//= new ProductResources();

	@GET
	public String test() {
		return "warup";
	}


	@Path("/postOrder")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Order create(OrderEO t) throws SQLException {
		// TODO Auto-generated method stub
		OrderEO newOrder = new OrderEO();
		long id = t.getCustomerId();

		List<String> products = t.getProductList();

		List<Product> dataProducts = prod.findAll();

		List<Customer> customer = cust.findAll();

		List<String> finalArray = new ArrayList<>();

		for (Customer oneCustomer : customer) {
			if (oneCustomer.getCustomerId() == id) {
				System.out.println("Customer found, id is " + oneCustomer.getCustomerId());
				newOrder.setCustomerId(id);

				for (Product product : dataProducts) {
					System.out.println(product.getName());

					for (String productName : products) {

						System.out.println(productName);

						if (productName.equals(product.getName())) {

							finalArray.add(productName);
							newOrder.setProductList(finalArray);
						} else {
							System.out.println("Product does not exist");
						}
					}
				}

			} else {
				System.out.println("id not found");
				
			}
		}

		return orderService.create(newOrder);
	}

	@Path("/getOrders")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Order> findAll() throws SQLException {
		// TODO Auto-generated method stub
		return orderService.findAll();
	}

	@Path("/{orderId}") // when you use a variable in the path u use curly braces to differentiate it
	// from an ordinary path name
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Order find(@PathParam("orderId") long orderId) throws SQLException {
		// TODO Auto-generated method stub
		return orderService.find(orderId);
	}
	@Path("/{orderId}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Order update(@PathParam("orderId")long orderId, OrderEO t) throws SQLException {
		// TODO Auto-generated method stub
		t.setOrderId(orderId);
		return orderService.update(orderId, t);
	}

	@Path("/{orderId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Order delete(@PathParam("orderId") long orderId) throws SQLException {
		return orderService.delete(orderId);
	}

}
