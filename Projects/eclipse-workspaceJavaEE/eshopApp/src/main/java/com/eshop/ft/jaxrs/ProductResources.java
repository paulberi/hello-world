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

import com.eshop.bt.to.ProductService;
import com.eshop.bt.to.ServiceDAO;
import com.eshop.bt.vo.Product;
import com.eshop.dt.eo.ProductEO;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;



@Path("/products")
public class ProductResources implements ServiceDAO<Product, ProductEO> {

	@Context
	UriInfo uriInfo;

	@Inject
	private ProductService productService;//=new ProductService();

	@GET
	public String test() {
		return "warup";
	}



	@Path("/postProduct")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Product create(ProductEO t) throws SQLException {
		// TODO Auto-generated method stub
		return productService.create(t);
	}

	@Path("/getProducts")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> findAll() throws SQLException {
		// TODO Auto-generated method stub
		return productService.findAll();
	}

	@Path("/{productId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Product find(@PathParam("productId") long productId) throws SQLException {
		// TODO Auto-generated method stub
		return productService.find(productId);
	}

	@Path("/{productId}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Product update(@PathParam("productId")long productId, ProductEO t) throws SQLException {
		// TODO Auto-generated method stub
		t.setProductId(productId);
		return productService.update(productId, t);
	}

	@Path("/{productId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Product delete(@PathParam("productId")long productId) throws SQLException {
		// TODO Auto-generated method stub
		return productService.delete(productId);
	}

}
