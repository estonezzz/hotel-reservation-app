package model;

import java.util.Objects;
import java.util.regex.Pattern;

public class Customer {
    private final String firstName;
    private final String lastName;
    private final String email;
    private static final String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern pattern = Pattern.compile(emailRegex);

    public Customer(String firstName, String lastName, String email) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }

        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }

        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format! Please use the format: example@domain.com, " +
                    "example@domain.com.uk, etc. Note: top-level domain must be at least 2 word-characters long.");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Override the equals() method to compare customers based on their unique email address
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return email.equals(customer.email);
    }

    // Override the hashCode() method to match the overridden equals() method
    @Override
    public  int hashCode() {
        return Objects.hash(email);
    }

    public final String getFirstName() {
        return firstName;
    }

    public final String getLastName() {
        return lastName;
    }

    public final String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Customer: " + firstName + " " + lastName + ", Email: " + email;
    }
}
