package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.RoomSearchType;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {

    private final CustomerService customerService;
    private final ReservationService reservationService;

    // The singleton instance of the HotelResource class
    private static HotelResource instance;

    /**
     * Private constructor prevents external instantiation of more than one instance of HotelResource
     * @param customerService the singleton object of CustomerService class
     * @param reservationService the singleton object of ReservationService class
     */
    private HotelResource(CustomerService customerService, ReservationService reservationService) {
        this.customerService = customerService;
        this.reservationService = reservationService;
    }

    public static HotelResource getInstance(CustomerService customerService, ReservationService reservationService) {
        if (instance == null) {
            instance = new HotelResource(customerService, reservationService);
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
     * Creates a new customer with the given email, first name, and last name.
     * @param email the email address of the new customer
     * @param firstName the first name of the new customer
     * @param lastName the last name of the new customer
     */
    public void createACustomer(String email, String firstName, String lastName){
        customerService.addCustomer(email, firstName, lastName);
    }

    /**
     * Retrieves the room with the given room number from the data store.
     * @param roomNumber the room number of the room to retrieve
     * @return the room with the given room number, or null if no such room exists
     */
    public IRoom getRoom(String roomNumber){
        return reservationService.getARoom(roomNumber);
    }

    /**
     * Creates a new reservation for the given customer, room, check-in date, and check-out date.
     * @param customerEmail the email address of the customer making the reservation
     * @param room the room to reserve
     * @param checkInDate the check-in date of the reservation
     * @param checkOutDate the check-out date of the reservation
     * @return the new reservation
     */
    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate){
        Customer customer = this.getCustomer(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer with given email is not in the system. Create an account first");
        }
        return reservationService.reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    /**
     * Retrieves all reservations for the customer with the given email address.
     * @param customerEmail the email address of the customer whose reservations to retrieve
     * @return a collection of all reservations for the customer with the given email address
     */
    public Collection<Reservation> getCustomerReservations(String customerEmail){
        return reservationService.getCustomerReservations(this.getCustomer(customerEmail));
    }

    /**
     * Retrieves all available rooms for the given check-in date and check-out date.
     * @param checkIn the check-in date
     * @param checkOut the check-out date
     * @return a collection of all available rooms for the given check-in and check-out dates
     */
    public  Collection<IRoom> findARoom(Date checkIn, Date checkOut, RoomSearchType roomSearchType){
        return reservationService.findRooms(checkIn, checkOut, roomSearchType);
    }


}
