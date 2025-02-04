package it.unina.dietiestates25.manager.infrastructure.adapter.in.dto;

import java.time.LocalDate;

public record SignUpAgencyRequest(
        String firstName,
        String lastName,
        String email,
        LocalDate dob,
        String tempPassword,
        String ragioneSociale,
        String partitaIva
) {
}
