package model;

/**
 * An interface representing a hotel room.
 */
public interface IRoom {
    /**
     * Returns the room number of this room.
     * @return the room number
     */
    public String getRoomNumber();

    /**
     * Returns the price of this room.
     * @return the price
     */
    public Double getRoomPrice();

    /**
     * Returns the type of this room.
     * @return the room type
     */
    public RoomType getRoomType();

    /**
     * Returns whether this room is free (price = $0.00) or not.
     * @return true if the room is free, false otherwise
     */
    public boolean isFree();
}
