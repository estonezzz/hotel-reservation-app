package menu;

import api.HotelResource;
import model.IRoom;
import model.Reservation;
import model.RoomSearchType;
import service.CustomerService;
import service.ReservationService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class represents the main menu of the hotel reservation application.
 */
public class MainMenu extends Menu{

    private static final HotelResource hotelResource = HotelResource.getInstance(
            CustomerService.getInstance(), ReservationService.getInstance());

    public static final int FIND_AND_RESERVE_ROOM = 1;
    public static final int SEE_MY_RESERVATIONS = 2;
    public static final int CREATE_AN_ACCOUNT = 3;
    public static final int ADMIN_MENU = 4;
    public static final int EXIT_APP = 5;

    /**
     * Display the main menu and handle user input.
     *
     * @param scanner Scanner object for user input.
     */
    public static void displayMenu(Scanner scanner) {
        boolean quit = false;
        while (!quit) {
            System.out.println("\n*** Main Menu ***\n" +
                    "1. Find and reserve a room\n" +
                    "2. See my reservations\n" +
                    "3. Create an account\n" +
                    "4. Admin\n" +
                    "5. Exit\n");

            int choice = getUserChoice(scanner);

            switch (choice) {
                case FIND_AND_RESERVE_ROOM:
                    findAndReserveARoom(scanner);
                    break;
                case SEE_MY_RESERVATIONS:
                    seeMyReservations(scanner);
                    break;
                case CREATE_AN_ACCOUNT:
                    createAnAccount(scanner);
                    break;
                case ADMIN_MENU:
                    showAdminMenu(scanner);
                    break;
                case EXIT_APP:
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                    break;
            }
        }
    }

    private static void showAdminMenu(Scanner scanner) {
        AdminMenu.displayMenu(scanner);
    }

    /**
     * Allow the user to create a new account.Handle exceptions when entering name (no empty strings) and email
     */
    private static void createAnAccount(Scanner scanner) {
        boolean accountCreated = false;
        while (!accountCreated) {
            try {
                System.out.print("Enter your first name: ");
                String firstName = scanner.nextLine().trim();
                System.out.print("Enter your last name: ");
                String lastName = scanner.nextLine().trim();
                System.out.print("Enter your email, eg. example@domain.com: ");
                String email = scanner.nextLine().trim();

                hotelResource.createACustomer(email, firstName, lastName);
                System.out.println("Account created successfully!");
                accountCreated = true;
            }
            catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Display reservations associated with the user's email address.
     */
    private static void seeMyReservations(Scanner scanner) {

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        Collection<Reservation> reservations = hotelResource.getCustomerReservations(email);

        if (reservations.isEmpty()) {
            System.out.println("No reservations found for this email.");
        } else {
            System.out.println("Your reservations:");
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }


    /**
     * Prompts the user to select a room search type and returns the corresponding RoomSearchType.
     * @param scanner The Scanner object used for reading user input.
     * @return The RoomSearchType corresponding to the user's choice.
     * @throws IllegalArgumentException if the user inputs an invalid choice.
     */
    private static RoomSearchType getUserRoomPreference(Scanner scanner) {
        System.out.println("Select room type:");
        System.out.println("1. Free rooms");
        System.out.println("2. Paid rooms");
        System.out.println("3. All rooms");
        int choice = getUserChoice(scanner);

        switch (choice) {
            case 1:
                return RoomSearchType.FREE_ROOMS;
            case 2:
                return RoomSearchType.PAID_ROOMS;
            case 3:
                return RoomSearchType.BOTH;
            default:
                throw new IllegalArgumentException("Invalid room search type choice.");
        }
    }


    /**
     * Find available rooms and allow the user to book a room.
     *
     * @param scanner Scanner object for user input.
     */
    private static void findAndReserveARoom(Scanner scanner) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Set the dateFormat to be non-lenient

        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        String checkInDateString = scanner.nextLine();
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        String checkOutDateString = scanner.nextLine();

        try {
            Date checkInDate = dateFormat.parse(checkInDateString);
            Date checkOutDate = dateFormat.parse(checkOutDateString);
            RoomSearchType roomSearchType = getUserRoomPreference(scanner);

            // Check if the check-in or check-out date is in the past
            Date currentDate = new Date();
            if (checkInDate.before(currentDate) || checkOutDate.before(currentDate)) {
                System.out.println("Check-in and check-out dates must not be in the past");
                return;
            }
            Collection<IRoom> availableRooms = hotelResource.findARoom(checkInDate, checkOutDate, roomSearchType);

            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available for the given dates.");
                // Adding 7 days to check in and check-out dates
                Date[] recommendedDates = addDaysToDates(7, checkInDate, checkOutDate);
                Collection<IRoom> recommendedRooms = hotelResource.findARoom(recommendedDates[0], recommendedDates[1],
                        roomSearchType);

                if (!recommendedRooms.isEmpty()) {
                    displayRecommendedRooms(recommendedRooms, recommendedDates);
                    reserveSelectedRoom(scanner, recommendedDates[0], recommendedDates[1]);
                }
            } else {
                displayAvailableRooms(availableRooms);
                reserveSelectedRoom(scanner, checkInDate, checkOutDate);
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays the available rooms from the provided collection.
     *
     * @param availableRooms A collection of available IRoom objects
     */
    private static void displayAvailableRooms(Collection<IRoom> availableRooms) {
        System.out.println("Available rooms:");
        for (IRoom room : availableRooms) {
            System.out.println(room);
        }
    }


    /**
     * Displays the recommended rooms and the recommended dates.
     *
     * @param recommendedRooms A collection of recommended IRoom objects
     * @param recommendedDates An array containing the recommended check-in and check-out dates
     */
    private static void displayRecommendedRooms(Collection<IRoom> recommendedRooms, Date[] recommendedDates) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String checkinDateString = dateFormat.format(recommendedDates[0] );
        String checkoutDateString = dateFormat.format(recommendedDates[1]);
        System.out.println("But the following rooms are available for dates between " +
                checkinDateString + " and " + checkoutDateString+ ":");
        for (IRoom room : recommendedRooms) {
            System.out.println(room);
        }
    }

    /**
     * Reserves the selected room for the customer using the provided check-in and check-out dates.
     *
     * @param scanner     A Scanner object to read user input
     * @param checkInDate The check-in date for the reservation
     * @param checkOutDate The check-out date for the reservation
     */
    private static void reserveSelectedRoom(Scanner scanner, Date checkInDate, Date checkOutDate) {
        System.out.print("Enter the room number you want to reserve: ");
        String roomNumber = scanner.nextLine();
        IRoom selectedRoom = hotelResource.getRoom(roomNumber);

        if (selectedRoom == null) {
            System.out.println("Invalid room number.");
        } else {
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();

            Reservation reservation = hotelResource.bookARoom(email, selectedRoom, checkInDate, checkOutDate);
            System.out.println("Reservation successfully created!");
            System.out.println(reservation);
        }
    }

    /**
     * Adds the specified number of days to the given dates.
     *
     * @param days the number of days to add
     * @param dates the dates to which the days should be added
     * @return an array of updated dates
     */
    private static Date[] addDaysToDates(int days, Date... dates) {
        Date[] newDates = new Date[dates.length];

        for (int i = 0; i < dates.length; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dates[i]);
            calendar.add(Calendar.DATE, days);
            newDates[i] = calendar.getTime();
        }

        return newDates;
    }



}
