package it.unina.dietiestates25.listing.infrastructure.adapter.in.dto;

import it.unina.dietiestates25.listing.model.ListingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record LandListingDto(
        @NotNull
        @NotBlank
        String title,

        @NotNull
        @Positive
        Integer price,

        String description,

        @NotNull
        @Positive
        Integer squareMeters,

        @NotNull
        ListingType listingType,

        @NotNull
        LocationDto locationDto,

        @NotNull
        Boolean building,

        List<String> otherFeatures
) {
}
