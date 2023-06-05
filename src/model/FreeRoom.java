package model;

/**
 * A class representing a free hotel room.
 */
public class FreeRoom extends Room{

    /**
     * Creates a new FreeRoom object with the given room number, room type and price of 0.0.
     * @param roomNumber the room number
     * @param enumeration the room type
     */
    public FreeRoom(String roomNumber, RoomType enumeration) {
        super(roomNumber, 0.0, enumeration);
    }

    @Override
    public String toString() { return "Room number: " + getRoomNumber() + ", Room type: "
            + getRoomType() + ", Price: (Free)";}

}
