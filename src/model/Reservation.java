package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation {
    private final Customer customer;
    private final IRoom room;
    private final Date checkinDate;
    private final Date checkoutDate;

    public Reservation(Customer customer, IRoom room, Date checkinDate, Date checkoutDate) {
        if (checkinDate.after(checkoutDate) || checkinDate.equals(checkoutDate)) {
            throw new IllegalArgumentException("Check-in date must be before the check-out date.");
        }
        this.customer = customer;
        this.room = room;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
    }

    public final IRoom getRoom() {
        return room;
    }

    public final Date getCheckinDate() {
        return checkinDate;
    }

    public final Date getCheckoutDate() {
        return checkoutDate;
    }

    public final Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("EEEE MMMM dd yyyy");
        String checkinDateString = dateFormat.format(checkinDate);
        String checkoutDateString = dateFormat.format(checkoutDate);
        return "Reservation{" +
                "customer=" + customer.getFirstName() + " " + customer.getLastName() +
                ", room=" + room +
                ", checkinDate=" + checkinDateString +
                ", checkoutDate=" + checkoutDateString +
                '}';
    }
}
