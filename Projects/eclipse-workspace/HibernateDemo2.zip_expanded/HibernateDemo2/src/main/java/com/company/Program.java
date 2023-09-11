package com.company;

import com.company.entities.Address;
import com.company.entities.Customer;
import com.company.service.CustomerService;

import java.util.ArrayList;
import java.util.List;

public class Program {

    public static void main(String[] args){
        CustomerService customerService = new CustomerService();

        // Create customer
        Customer customer1 = new Customer("Anna", "Al");
        List<Address> addresses = new ArrayList<Address>();
        addresses.add(new Address("Storgatan", 16));
        addresses.add(new Address("Byvägen", 3));
        customer1.setAddresses(addresses);
        Customer customer2 = new Customer("Urban", null);
        customerService.createCustomer(customer1);
        customerService.createCustomer(customer2);

        // Get all customers
        List<Customer> customers = customerService.getCustomers();
        for(Customer c : customers) {
            System.out.println(c.getFirstName() + " " + c.getLastName());
        }

        // Read customer by id
        Customer customer = customerService.getCustomerById(1);

        // Read customer by firstname
        List<Customer> customerList = customerService.getByFirstName("Urban");
        for(Customer c : customerList) {
            System.out.println(c.getFirstName() + " " + c.getLastName());
        }

        // Selective update for customers without lastname
        customerService.selectiveUpdate("unknown");

        //getCustomerByFirstnameWithLastname(String firstName)
        List<Customer> customerList2 = customerService.getCustomerByFirstnameWithLastname("Urban");
        for(Customer c : customerList2) {
            System.out.println(c.getFirstName() + " " + c.getLastName());
        }


        // Update customer
        customer.setLastName("Johansson");
        customerService.updateCustomer(customer);

        // Delete customer
        customerService.deleteCustomer(customer);




    }
}
