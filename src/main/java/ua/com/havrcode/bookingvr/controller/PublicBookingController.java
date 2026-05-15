package ua.com.havrcode.bookingvr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicBookingController {

    @GetMapping("/booking")
    public String showBookingCalendar(Model model) {
        model.addAttribute("pageTitle", "Booking VR");
        model.addAttribute("clubName", "Virtum VR");
        model.addAttribute("monthLabel", "Квітень 2026");
        return "booking-calendar";
    }
}
