package it.unina.dietiestates25.visit.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public class Availability {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate day;

    private List<TimeSlot> timeSlots;

    public Availability() {
    }

    public Availability(LocalDate day, List<TimeSlot> timeSlots) {
        this.day = day;
        this.timeSlots = timeSlots;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
