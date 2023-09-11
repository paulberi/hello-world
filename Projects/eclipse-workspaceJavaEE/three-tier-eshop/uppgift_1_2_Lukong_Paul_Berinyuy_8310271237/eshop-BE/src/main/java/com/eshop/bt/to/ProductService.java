package com.eshop.bt.to;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.eshop.bt.vo.Product;
import com.eshop.dt.dao.Database;
import com.eshop.dt.eo.ProductEO;


@Named
@ApplicationScoped
public class ProductService implements ServiceDAO<Product, ProductEO>{
	
	private Map<Long, Product> products =Database.getProducts();
	
	public ProductService() {
		
		ProductEO prod1 =new ProductEO(1, "Casava",89.0, "Tuber",45);
		ProductEO prod2= new ProductEO(2,"Beef",90.0, "meat", 200);
		ProductEO prod3= new ProductEO(3,"Huckle Berry",78.0, "Vegetables",  90);
		ProductEO prod4= new ProductEO(4, "Apple",20.0, "Fruit",20);
		ProductEO prod5= new ProductEO(5, "Water melon",25.0, "Fruit", 100);
		ProductEO prod6= new ProductEO(6, "Rice",40.0, "Grains", 500);
		ProductEO prod7= new ProductEO(7, "Casava",89.0, "Tuber",45);
		
		products.put(1L, new Product(prod1.getProductId(),prod1.getName(),prod1.getPrice(),prod1.getCategory(),prod1.getQuantityAvailable()));
		products.put(2L, new Product(prod2.getProductId(),prod2.getName(),prod2.getPrice(),prod2.getCategory(),prod2.getQuantityAvailable()));
		products.put(3L, new Product(prod3.getProductId(),prod3.getName(),prod3.getPrice(),prod3.getCategory(),prod3.getQuantityAvailable()));
		products.put(4L, new Product(prod4.getProductId(),prod4.getName(),prod4.getPrice(),prod4.getCategory(),prod4.getQuantityAvailable()));
		products.put(5L, new Product(prod5.getProductId(),prod5.getName(),prod5.getPrice(),prod5.getCategory(),prod5.getQuantityAvailable()));
		products.put(6L, new Product(prod6.getProductId(),prod6.getName(),prod6.getPrice(),prod6.getCategory(),prod6.getQuantityAvailable()));
		products.put(7L, new Product(prod7.getProductId(),prod7.getName(),prod7.getPrice(),prod7.getCategory(),prod7.getQuantityAvailable()));
		
	}
	


	@Override
	public Product create(ProductEO prod1) throws SQLException {
		// TODO Auto-generated method stub
		
		Product product=new Product(prod1.getProductId(),prod1.getName(),prod1.getPrice(),prod1.getCategory(),prod1.getQuantityAvailable());
		
		product.setProductId((products.size()+1));
		products.put(product.getProductId(), product);
		return product;
	}

	@Override
	public List<Product> findAll() throws SQLException {
		// TODO Auto-generated method stub
		return new ArrayList<Product>(products.values());
	}

	@Override
	public Product find(long id) throws SQLException {
		// TODO Auto-generated method stub
		return products.get(id);
	}

	@Override
	public Product update(long id,ProductEO product1) throws SQLException {
		// TODO Auto-generated method stub
		if(product1.getProductId()<=0 && product1.getProductId()!=id) {
			return null;
		}
		Product product=new Product();
		product.setProductId(product1.getProductId());
		product.setName(product1.getName());
		product.setPrice(product1.getPrice());
		product.setCategory(product1.getCategory());
		product.setQuantityAvailable(product1.getQuantityAvailable());
		products.put(product.getProductId(), product);
		return product;
	}

	@Override
	public Product delete(long id) throws SQLException {
		// TODO Auto-generated method stub
		return products.remove(id);
	}


}
