package model;

import java.util.Objects;

/**
 * A class representing a hotel room.
 */
public class Room implements IRoom{
    private final String roomNumber;
    private final Double price;
    private final RoomType enumeration;

    /**
     * Creates a new Room object with the given room number, price, and room type.
     * @param roomNumber the room number
     * @param price the room price
     * @param enumeration the room type
     */
    public Room(String roomNumber, Double price, RoomType enumeration) {
        if (!(isConvertibleToInt(roomNumber) && Integer.parseInt(roomNumber) > 0)) {
            throw new IllegalArgumentException("Room number must be a positive integer number");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Room price must be 0 or a positive value");
        }
        this.roomNumber = roomNumber;
        this.price = price;
        this.enumeration = enumeration;
    }

    private boolean isConvertibleToInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    // Override the equals() method to compare rooms based on their unique room number
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Room room = (Room) obj;
        return Objects.equals(roomNumber, room.roomNumber);
    }

    // Override the hashCode() method to match the overridden equals() method
    @Override
    public int hashCode() {
        return Objects.hash(roomNumber);
    }

    @Override
    public String toString() {
        return "Room number: " + roomNumber + ", Room type: " + enumeration + ", Price: $" + String.format("%.2f", price);
    }


    @Override
    public final String getRoomNumber() {
        return roomNumber;
    }


    @Override
    public final Double getRoomPrice() {
        return price;
    }


    @Override
    public final RoomType getRoomType() {
        return enumeration;
    }


    @Override
    public final boolean isFree() {
        if(price==0) {
            return true;
        }
        else {
            return false;
        }
    }
}