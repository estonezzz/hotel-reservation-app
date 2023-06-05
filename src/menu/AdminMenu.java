package menu;

import api.AdminResource;
import api.HotelResource;
import model.*;
import service.CustomerService;
import service.ReservationService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This class represents the admin menu of the hotel reservation application.
 */
public class AdminMenu extends Menu{

    private static final AdminResource adminResource = AdminResource.getInstance(
            CustomerService.getInstance(), ReservationService.getInstance());
    public static final int SEE_ALL_CUSTOMERS = 1;
    public static final int SEE_ALL_ROOMS = 2;
    public static final int SEE_ALL_RESERVATIONS = 3;
    public static final int ADD_ROOM = 4;
    public static final int LOAD_ROOMS_FROM_CSV = 5;
    public static final int BACK_TO_MAIN_MENU = 6;

    /**
     * Displays the admin menu and processes user input.
     *
     * @param scanner the Scanner instance used to read user input
     */
    public static void displayMenu(Scanner scanner) {
        boolean quit = false;
        while (!quit) {
            System.out.println("\n*** Admin Menu ***\n" +
                    "1. See all customers\n" +
                    "2. See all rooms\n" +
                    "3. See all reservations\n" +
                    "4. Add a room\n" +
                    "5. Load rooms from CSV file\n" +
                    "6. Back to Main Menu\n");

            int choice = getUserChoice(scanner);

            // Process the user's choice
            switch (choice) {
                case SEE_ALL_CUSTOMERS:
                    displayAllCustomers();
                    break;
                case SEE_ALL_ROOMS:
                    displayAllRooms();
                    break;
                case SEE_ALL_RESERVATIONS:
                    displayAllReservations();
                    break;
                case ADD_ROOM:
                    addARoom(scanner);
                    break;
                case LOAD_ROOMS_FROM_CSV:
                    loadRoomsFromCsv(scanner);
                    break;
                case BACK_TO_MAIN_MENU:
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                    break;
            }
        }
    }

    /**
     * Displays all customers in the system.
     */
    private static void displayAllCustomers() {
        System.out.println("\n*** All Customers ***");
        Collection<Customer> customers = adminResource.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("There are no customers in the system");
        }
        else {
            for (Customer customer : customers) {
                System.out.println(customer);
            }
        }
    }

    /**
     * Displays all rooms in the system.
     */
    private static void displayAllRooms() {
        System.out.println("\n*** All Rooms ***");
        Collection<IRoom> rooms =  adminResource.getAllRooms();
        if (rooms.isEmpty()){
            System.out.println("There are no rooms in the system");
        }
        else {
            for (IRoom room : rooms) {
                System.out.println(room);
            }
        }
    }

    /**
     * Displays all reservations in the system.
     */
    private static void displayAllReservations() {
        System.out.println("\n*** All Reservations ***");
        adminResource.displayAllReservations();
    }

    /**
     * Prompts the user to add one or more rooms to the hotel.
     * Allows the user to enter room information and provides the option to add more rooms.
     *
     * @param scanner the Scanner object for reading user input
     */
    private static void addARoom(Scanner scanner) {
        Set<IRoom> rooms = new HashSet<>();

        System.out.println("\n*** Add a Room ***");

        boolean addAnotherRoom;
        do {
            System.out.println("Enter room information:");

            // Get a unique room number from the user
            String roomNumber;
            boolean roomNumberExists;
            do {
                System.out.print("Enter room number: ");
                roomNumber = scanner.nextLine();
                roomNumberExists = false;

                // Check if the room number is a positive integer
                if (!roomNumber.matches("\\d+") || Integer.parseInt(roomNumber) <= 0) {
                    System.out.println("Invalid room number. Please enter a positive integer.");
                    roomNumberExists = true;
                    continue;
                }

                for (IRoom room : rooms) {
                    if (room.getRoomNumber().equals(roomNumber)) {
                        System.out.println("Room number already exists. Please enter a unique room number.");
                        roomNumberExists = true;
                        break;
                    }
                }
            } while (roomNumberExists);

            // Get room price from the user
            double roomPrice;
            while (true) {
                System.out.print("Enter room price: ");
                try {
                    roomPrice = Double.parseDouble(scanner.nextLine());
                    if (roomPrice < 0) {
                        throw new IllegalArgumentException("Room price must be 0 or a positive value");
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please room price in numeric format.");
                }
                catch (IllegalArgumentException e){
                    System.out.println(e.getMessage());
                }
            }


            // Get room type from the user
            RoomType roomType;
            while (true) {
                System.out.print("Enter room type (1 for Single, 2 for Double): ");
                if (scanner.hasNextInt()) {
                    int roomTypeChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (roomTypeChoice == 1) {
                        roomType = RoomType.SINGLE;
                        break;
                    } else if (roomTypeChoice == 2) {
                        roomType = RoomType.DOUBLE;
                        break;
                    } else {
                        System.out.println("Invalid choice. Please enter 1 for Single or 2 for Double.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter an integer.");
                    scanner.nextLine(); // Clear the invalid input
                }
            }

            // Create a room object based on the entered information

            try {
                IRoom room;
                if (roomPrice == 0.0) {
                    room = new FreeRoom(roomNumber, roomType);
                } else {
                    room = new Room(roomNumber, roomPrice, roomType);
                }
                // Add the room to the set of rooms
                rooms.add(room);
            }catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }


            // Ask the user if they want to add another room
            System.out.print("Do you want to add another room? Y/N: ");
            String addAnotherRoomChoice = scanner.nextLine();
            // Anything but "Y" or "y" will lead to NO option
            addAnotherRoom = addAnotherRoomChoice.equalsIgnoreCase("Y");

        } while (addAnotherRoom);

        // Add the rooms to the hotel system
        adminResource.addRooms(new ArrayList<>(rooms));
        System.out.println("Rooms added successfully!");
    }


    /**
     * Loads rooms from a CSV file and adds them to the system.
     *
     * @param scanner Scanner for user input.
     */
    private static void loadRoomsFromCsv(Scanner scanner) {
        System.out.println("\n*** Load Rooms from CSV File ***");
        System.out.print("Enter the CSV file path: ");
        String filePath = scanner.nextLine();

        try {
            List<IRoom> rooms = readRoomsFromCsv(filePath);
            adminResource.addRooms(rooms);
            System.out.println("Rooms added successfully!");
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid data in CSV file: " + e.getMessage());
        }
    }

    /**
     * Reads rooms from a CSV file and returns them as a list of IRoom objects.
     *
     * @param filePath The path of the CSV file containing the room data.
     * @return A list of IRoom objects read from the CSV file.
     * @throws IOException If there's an error reading the file.
     */
    private static List<IRoom> readRoomsFromCsv(String filePath) throws IOException, IllegalArgumentException {
        List<IRoom> rooms = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 3) {
                    throw new IllegalArgumentException("Each line must have 3 values separated by commas:" +
                            " roomNumber, price, roomType");
                }

                String roomNumber = values[0];
                double roomPrice = Double.parseDouble(values[1]);
                RoomType roomType = RoomType.valueOf(values[2].toUpperCase());

                IRoom room;
                if (roomPrice == 0.0) {
                    room = new FreeRoom(roomNumber, roomType);
                } else {
                    room = new Room(roomNumber, roomPrice, roomType);
                }

                rooms.add(room);
            }
        }
        return rooms;
    }


}
