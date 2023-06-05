package test;

import model.Customer;

public class Driver {
    public static void main(String[] args) {
        Customer customer = new Customer("first", "second", "andreyuuemaa1@duck.com.uk");
        System.out.println(customer);
    }
}
