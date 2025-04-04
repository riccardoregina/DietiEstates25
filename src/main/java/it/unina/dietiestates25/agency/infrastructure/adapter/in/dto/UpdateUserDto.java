package it.unina.dietiestates25.agency.infrastructure.adapter.in.dto;

import java.time.LocalDate;

import it.unina.dietiestates25.validation.NullOrNotBlank;
import it.unina.dietiestates25.validation.NullOrPast;

public record UpdateUserDto(
    @NullOrNotBlank
    String firstName,
    @NullOrNotBlank
    String lastName,
    @NullOrNotBlank
    String email,
    @NullOrPast
    LocalDate dob,
    @NullOrNotBlank
    String password,
    @NullOrNotBlank
    String profilePicUrl
) {
}
