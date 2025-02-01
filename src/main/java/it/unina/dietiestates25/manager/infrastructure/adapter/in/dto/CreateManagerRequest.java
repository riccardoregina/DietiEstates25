package it.unina.dietiestates25.manager.infrastructure.adapter.in.dto;

import it.unina.dietiestates25.model.Agency;

import java.time.LocalDate;

public record CreateManagerRequest(
        String firstName,
        String lastName,
        String email,
        LocalDate dob,
        String tempPassword,
        Agency agency
) {
}
