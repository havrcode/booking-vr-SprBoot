package ua.com.havrcode.bookingvr.dto;

public class DayViewDto {

    private final int dayNumber;
    private final boolean active;
    private final int freePlaces;
    private final int bookedPlaces;

    public DayViewDto(int dayNumber, boolean active, int freePlaces, int bookedPlaces) {
        this.dayNumber = dayNumber;
        this.active = active;
        this.freePlaces = freePlaces;
        this.bookedPlaces = bookedPlaces;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public boolean isActive() {
        return active;
    }

    public int getFreePlaces() {
        return freePlaces;
    }

    public int getBookedPlaces() {
        return bookedPlaces;
    }
    
}
