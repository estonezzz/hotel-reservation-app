package model;

public enum RoomType {
    SINGLE,
    DOUBLE;


    @Override
    public String toString() {
        switch (this) {
            case SINGLE:
                return "Single Bed Room";
            case DOUBLE:
                return "Double Bed Room";
            default:
                throw new IllegalArgumentException();
        }
    }
}
