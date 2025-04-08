package it.unina.dietiestates25.visit.infrastructure.adapter.in.dto;

import it.unina.dietiestates25.visit.model.Availability;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VisitRequestDto(
        @NotNull
        @NotBlank
        String listingId,

        @NotNull
        List<Availability> availabilities
) {
}
