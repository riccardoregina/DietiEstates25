package it.unina.dietiestates25.listing.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocationDto(
        @NotNull
        @NotBlank
        String region,

        @NotNull
        @NotBlank
        String city,

        @NotNull
        @NotBlank
        String address,

        @NotNull
        Double longitude,

        @NotNull
        Double latitude
) {
}
