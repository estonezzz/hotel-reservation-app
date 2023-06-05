package menu;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    /**
     * Prompts the user for their menu choice and validates the input.
     *
     * @param scanner A Scanner object to read user input
     * @return The valid menu choice as an integer
     */
    public static int getUserChoice(Scanner scanner) {
        System.out.print("\nEnter your choice: ");
        int choice = 0;
        while (choice == 0) {
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Move the scanner to the next line
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // clear the input buffer
            }
        }
        return choice;
    }

}
