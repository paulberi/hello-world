package com.company.service;

import com.company.entities.Customer;
import com.company.persistence.dao.CustomerDAO;

import java.sql.SQLException;
import java.util.List;

public class CustomerService {

    private CustomerDAO customerDAO = new CustomerDAO();

    public List<Customer> getCustomers() {
        List<Customer> customers = null;
        try {
            customers = customerDAO.findAll();
            // Validate
            // Logging
            // Specific business processing
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customers;
    }

    public boolean createCustomer(Customer customer) {
        int result = 0;
        try {
            result = customerDAO.create(customer);
            // Validate
            // Logging
            // Specific business processing
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result == 1 ? true : false;
    }

    public Customer getCustomer(int id) {
        Customer customer = null;
        try {
            customer = customerDAO.find(id);
            // Validate
            // Logging
            // Specific business processing
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customer;
    }

    public boolean updateCustomer(Customer customer) {
        int result = 0;
        try {
            result = customerDAO.update(customer);
            // Validate
            // Logging
            // Specific business processing
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result == 1 ? true : false;
    }

    public boolean deleteCustomer(Customer customer) {
        int result = 0;
        try {
            result = customerDAO.delete(customer);
            // Validate
            // Logging
            // Specific business processing
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result == 1 ? true : false;
    }


}
