package it.unina.dietiestates25.manager.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record SignUpAgencyRequest(
        @NotNull
        @NotBlank
        String firstName,
        @NotNull
        @NotBlank
        String lastName,
        @NotNull
        @NotBlank
        String email,
        @NotNull
        @Past
        LocalDate dob,
        @NotNull
        @NotBlank
        String tempPassword,
        @NotNull
        @NotBlank
        String ragioneSociale,
        @NotNull
        @NotBlank
        String partitaIva
) {
}
