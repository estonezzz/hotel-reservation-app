package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.RoomSearchType;

import java.util.*;

/**
 * A service class that provides methods to manage reservations: add, find, retrieve rooms, get existing reservations
 */
public class ReservationService {

    // A hashset to store room objects
    private Set<IRoom> rooms = new HashSet<>();

    // A list to store all existing reservations
    private List<Reservation> reservations = new ArrayList<>();

    // The singleton instance of the ReservationService class
    private static ReservationService instance;

    /**
     * Private constructor prevents external instantiation.
     */
    private ReservationService() {}

    /**
     * Returns the singleton instance of the CustomerService class:  new one, if instance does not exist,
     * otherwise existing one
     * @return the singleton instance
     */
    public static ReservationService getInstance() {
        if (instance == null){
            instance = new ReservationService();
        }
        return instance;
    }

    /**
     * Adds a new room to the service.
     * @param room the room to add
     */
    public void addRoom(IRoom room) {rooms.add(room);
    }

    /**
     * Returns the room with the given ID.
     * @param roomId the ID of the room to retrieve
     * @return the room with the given ID, or null if no such room exists
     */
    public IRoom getARoom(String roomId){
        for (IRoom room : rooms) {
            if (room.getRoomNumber().equals(roomId)) {
                return room;
            }
        }
        return null;
    }

    /**
     * Makes a new reservation for the given customer, room, check-in date, and check-out date.
     * @param customer the customer making the reservation
     * @param room the room to reserve
     * @param checkinDate the check-in date
     * @param checkoutDate the check-out date
     * @return the new reservation object
     */
    public Reservation reserveARoom(Customer customer, IRoom room, Date checkinDate, Date checkoutDate) {
        if (!isRoomAvailable(room, checkinDate, checkoutDate)) {
            throw new IllegalArgumentException("The room is not available for the specified dates.");
        }
        Reservation reservation = new Reservation(customer, room, checkinDate, checkoutDate);
        reservations.add(reservation);
        return  reservation;
    }

    /**
     * Finds and returns a collection of available rooms matching the specified search type between the given check-in
     * and check-out dates.
     * @param checkInDate The check-in date.
     * @param checkOutDate The check-out date.
     * @param roomSearchType The type of rooms to search for (FREE_ROOMS, PAID_ROOMS, or BOTH).
     * @return A collection of available rooms matching the specified search type.
     */
    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate, RoomSearchType roomSearchType) {
        Collection<IRoom> availableRooms = new ArrayList<>();
        for (IRoom room : rooms) {
            if (isRoomAvailable(room, checkInDate, checkOutDate) && matchesSearchType(room, roomSearchType)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }


    /**
     * Checks if the given room matches the specified search type.
     * @param room The room to check.
     * @param roomSearchType The type of rooms to search for (FREE_ROOMS, PAID_ROOMS, or BOTH).
     * @return True if the room matches the specified search type, false otherwise.
     * @throws IllegalArgumentException if an invalid room search type is provided.
     */
    private boolean matchesSearchType(IRoom room, RoomSearchType roomSearchType) {
        switch (roomSearchType) {
            case FREE_ROOMS:
                return room.isFree();
            case PAID_ROOMS:
                return !room.isFree();
            case BOTH:
                return true;
            default:
                throw new IllegalArgumentException("Invalid room search type: " + roomSearchType);
        }
    }


    /**
     * Checks if the given room is available between the specified check-in and check-out dates.
     * A room is considered available if it's not already booked during the specified dates.
     *
     * @param room the room to check availability for
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return true if the room is available during the specified dates, false otherwise
     */
    boolean isRoomAvailable(IRoom room, Date checkInDate, Date checkOutDate) {
        for (Reservation reservation : reservations) {
            if (reservation.getRoom().equals(room)) {
                // New reservation starts before an existing reservation ends and ends after the existing reservation starts.
                if ((checkInDate.before(reservation.getCheckoutDate()) || checkInDate.equals(reservation.getCheckoutDate()))
                        && (checkOutDate.after(reservation.getCheckinDate()) || checkOutDate.equals(reservation.getCheckinDate()))) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Returns all reservations for the given customer.
     * @param customer the customer to retrieve reservations for
     * @return a collection of all reservations for the given customer
     */
    public Collection<Reservation> getCustomerReservations(Customer customer){
        Collection<Reservation> customerReservations = new ArrayList<>();

        // Iterate through all reservations and add to customerReservations if they're for the given customer
        for (Reservation reservation : reservations) {
            if (reservation.getCustomer().equals(customer)) {
                customerReservations.add(reservation);
            }
        }

        return customerReservations;
    }

    /**
     * Prints a list of all existing reservations.
     */
    public void printAllReservations(){
        for(Reservation reservation: reservations){
            System.out.println(reservation);
        }
    }

    /**
     * Returns a collection of all existing rooms.
     * @return a collection of all existing rooms
     */
    public Collection<IRoom> getAllRooms(){
        return new ArrayList<>(rooms);
    }


}
