package com.company.service;

import com.company.entities.Customer;
import com.company.repository.CustomerRepository;

import java.util.List;

public class CustomerService {

    private CustomerRepository customerRepository = new CustomerRepository();

    public void createCustomer(Customer customer) {
        // LOGIC Validation, Logging, etc.
        customerRepository.create(customer);
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }


    public Customer getCustomerById(int id) {
        return customerRepository.getById(id);
    }

    public void updateCustomer(Customer customer) {
        customerRepository.update(customer);
    }

    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }

    public List<Customer> getByFirstName(String name) {
        return customerRepository.getByFirstName(name);
    }

    public List<Customer> getCustomerByFirstnameWithLastname(String firstName) {
        return customerRepository.getCustomerByFirstnameWithLastname(firstName);
    }

    public void selectiveUpdate(String name) {
        customerRepository.selectiveUpdate(name);
    }
}
