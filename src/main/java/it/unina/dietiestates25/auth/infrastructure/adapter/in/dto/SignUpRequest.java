package it.unina.dietiestates25.auth.infrastructure.adapter.in.dto;

import java.time.LocalDate;

public record SignUpRequest(
        String firstName,
        String lastName,
        LocalDate dob,
        String email,
        String password
) {
}
