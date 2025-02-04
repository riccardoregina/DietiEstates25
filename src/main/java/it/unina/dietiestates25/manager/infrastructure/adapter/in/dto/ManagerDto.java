package it.unina.dietiestates25.manager.infrastructure.adapter.in.dto;

import java.time.LocalDate;

public record ManagerDto(
        String firstName,
        String lastName,
        String email,
        LocalDate dob,
        String password
) {
}
