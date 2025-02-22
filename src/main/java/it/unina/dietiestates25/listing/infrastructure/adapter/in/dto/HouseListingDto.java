package it.unina.dietiestates25.listing.infrastructure.adapter.in.dto;

import it.unina.dietiestates25.model.EnergyClass;
import it.unina.dietiestates25.model.ListingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Map;

public record HouseListingDto(

        @NotNull
        @NotBlank
        String title,

        @NotNull
        @Positive
        int price,

        String description,

        @NotNull
        @Positive
        int squareMeters,

        @NotNull
        ListingType listingType,

        @NotNull
        LocationDto locationDto,

        @NotNull
        @Positive
        int nRooms,

        @NotNull
        @Positive
        int nBathrooms,

        @NotNull
        int floor,

        @NotNull
        EnergyClass energyClass,

        Map<String, String> otherFeatures
) {
}
