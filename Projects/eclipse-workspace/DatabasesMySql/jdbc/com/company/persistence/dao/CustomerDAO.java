package com.company.persistence.dao;

import com.company.entities.Customer;
import com.company.persistence.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements DAO<Customer> {
    @Override
    public int create(Customer customer) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement("insert into customers(first_name, last_name) values(?,?)");
        statement.setString(1, customer.getFirstName());
        statement.setString(2, customer.getLastName());
        return statement.executeUpdate();
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from customer");
        ResultSet rs = statement.executeQuery();
        List<Customer> customers = new ArrayList<>();
        while(rs.next()) {
            Customer customer = new Customer();
            customer.setCustomerId(rs.getInt("customer_id"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            customers.add(customer);
        }
        return customers;
    }

    @Override
    public Customer find(Object id) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from customers where customer_id = ?");
        statement.setInt(1, (int)id);
        ResultSet rs = statement.executeQuery();
        Customer customer = null;
        if(rs.next()) {
            customer = new Customer();
            customer.setCustomerId(rs.getInt("customer_id"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
        }
        return customer;
    }

    @Override
    public int update(Customer customer) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement("update customers set first_name=?, last_name=? where customer_id = ?");
        statement.setString(1, customer.getFirstName());
        statement.setString(2, customer.getLastName());
        statement.setInt(3, customer.getCustomerId());
        return statement.executeUpdate();
    }

    @Override
    public int delete(Customer customer) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement("delete from customers where customer_id = ?");
        statement.setInt(1, customer.getCustomerId());
        return statement.executeUpdate();
  
    }
}
