package it.unina.dietiestates25.listing.infrastructure.adapter.in.dto;

import it.unina.dietiestates25.model.ListingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.Map;

public record BuildingListingDto(
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
        Boolean elevator,

        @NotNull
        ListingType listingType,

        @NotNull
        LocationDto locationDto,

        Map<String, String> otherFeatures,

        List<String> photos
) {
}
