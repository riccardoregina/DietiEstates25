package it.unina.dietiestates25.listing.infrastructure.adapter.in.dto;

import it.unina.dietiestates25.model.ListingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record GarageListingDto(
        @NotNull
        @NotBlank
        String title,

        @NotNull
        int price,

        String description,

        @NotNull
        int squareMeters,

        @NotNull
        ListingType listingType,

        @NotNull
        LocationDto locationDto,

        @NotNull
        int floor,

        Map<String, String> otherFeatures
) {
}
