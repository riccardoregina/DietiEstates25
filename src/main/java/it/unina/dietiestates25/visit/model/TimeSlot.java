package it.unina.dietiestates25.visit.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlot {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate day;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime start;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime end;

    public TimeSlot() {
    }

    public TimeSlot(LocalDate day, LocalTime start, LocalTime end) {
        if (end.isAfter(start)) {
            throw new IllegalArgumentException("The end time must be after the start time");
        }
        this.day = day;
        this.start = start;
        this.end = end;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }
}
