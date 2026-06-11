package ua.com.havrcode.bookingvr.service;


import org.springframework.stereotype.Service;
import ua.com.havrcode.bookingvr.dto.DayViewDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class CalendarService {

    public List<DayViewDto> getMockDaysForMonth() {
        List<DayViewDto> days = new ArrayList<>();

        for (int day = 1; day <= 30; day++) {
            boolean active = day % 7 != 0;
            int freePlaces = active ? 2 : 0;
            int bookedPlaces = active ? 0 : 2;

            days.add(new DayViewDto(day, active, freePlaces, bookedPlaces));
        }

        return days;
    }
}
