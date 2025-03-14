package it.unina.dietiestates25.visit.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TimeSlot {
    FROM_08_TO_10("08-10"),
    FROM_10_TO_12("10-12"),
    FROM_12_TO_14("12-14"),
    FROM_14_TO_16("14-16"),
    FROM_16_TO_18("16-18"),
    FROM_18_TO_20("18-20");

    private final String value;

    TimeSlot(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TimeSlot fromValue(String value) {
        for (TimeSlot slot : values()) {
            if (slot.getValue().equals(value)) {
                return slot;
            }
        }
        throw new IllegalArgumentException("Invalid time slot: " + value);
    }
}