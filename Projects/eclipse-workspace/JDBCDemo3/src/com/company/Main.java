package com.company;

import com.company.entities.Customer;
import com.company.service.CustomerService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        CustomerService customerService = new CustomerService();

        // Create
        //if(customerService.createCustomer(new Customer("Örjan", "Svensson"))) {
        //    System.out.println("Customer created!");
        //}

        // Read all
        List<Customer> customers = customerService.getCustomers();
        for(Customer customer : customers) {
            System.out.println(customer);
        }

        // Read by id
        Customer customer = customerService.getCustomer(4);

        customer.setLastName("Andersson");
        if(customerService.updateCustomer(customers.get(1))) {
            System.out.println("Customer updated!");
        }

        if(customerService.deleteCustomer(customer)) {
            System.out.println("Customer deleted!");
        }

    }
}
