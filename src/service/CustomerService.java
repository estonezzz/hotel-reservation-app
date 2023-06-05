package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A service class that provides methods to add and retrieve customers.
 */
public class CustomerService {
    // A hashmap which stores  customer email addresses and associated customer objects
    private Map<String, Customer> customers = new HashMap<>();

    // The singleton instance of the CustomerService class
    private static CustomerService instance;

    /**
     * Private constructor prevents external instantiation.
     */
    private CustomerService() {}

    /**
     * Returns the singleton instance of the CustomerService class: new one, if instance does not exist,
     * otherwise existing one
     * @return the singleton instance
     */
    public static CustomerService getInstance() {
        if (instance == null){
            instance = new CustomerService();
        }
        return instance;
    }

    /**
     * Adds a new customer with the given email, first name, and last name.
     * @param email the customer email address
     * @param firstName the customer first name
     * @param lastName the customer last name
     * @throws IllegalArgumentException if the email is invalid
     */
    public void addCustomer(String email, String firstName, String lastName){
        if (customers.containsKey(email)) {
            throw new IllegalArgumentException("A customer with the same email address already exists.");
        }
        Customer customer = new Customer(firstName, lastName, email);
        customers.put(email, customer);
    }

    /**
     * Returns the customer with the given email.
     * @param customerEmail the email of the customer to retrieve
     * @return the customer with the given email, or null if no such customer exists
     */
    public Customer getCustomer(String customerEmail){
        return customers.get(customerEmail);
    }

    /**
     * Returns a collection of all customers in the service.
     * @return a collection of all customers in the service
     */
    public Collection<Customer> getAllCustomers(){
        return customers.values();
    }


}
