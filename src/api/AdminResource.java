package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdminResource {

    private final CustomerService customerService;
    private final ReservationService reservationService;

    // The singleton instance of the AdminResource class
    private static AdminResource instance;

    /**
     * Private constructor prevents external instantiation of more than one instance of AdminResource
     * @param customerService the singleton object of CustomerService class
     * @param reservationService the singleton object of ReservationService class
     */
    private AdminResource(CustomerService customerService, ReservationService reservationService) {
        this.customerService = customerService;
        this.reservationService = reservationService;
    }

    public static AdminResource getInstance(CustomerService customerService, ReservationService reservationService) {
        if (instance == null) {
            instance = new AdminResource(customerService, reservationService);
        }
        return instance;
    }

    /**
     * Retrieves the customer with the given email address from the data store.
     * @param email the email address of the customer to retrieve
     * @return the customer with the given email address, or null if no such customer exists
     */
    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }


    /**
     * Adds rooms from a List into the data store. If a room with the same room number already exists,
     * it will be skipped and the user will be informed about the conflict.
     *
     * @param rooms List of IRoom objects to be added
     */
    public void addRooms(List<IRoom> rooms) {
        for (IRoom room : rooms) {
            IRoom existingRoom = reservationService.getARoom(room.getRoomNumber());
            if (existingRoom == null) {
                reservationService.addRoom(room);
                System.out.println("Added room: " + room.getRoomNumber());
            } else {
                System.out.println("Room with number " + room.getRoomNumber() + " already exists. Skipping...");
            }
        }
    }


    /**
     * Returns a collection of all rooms in the data store.
     * @return a collection of all rooms
     */
    public Collection<IRoom> getAllRooms(){
        return reservationService.getAllRooms();
    }

    /**
     * Returns a collection of all customers in the data store.
     * @return a collection of all customers
     */
    public Collection<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    /**
     * Displays all existing reservations
     */
    public void displayAllReservations(){
        reservationService.printAllReservations();
    }



}
