package it.unina.dietiestates25.listing.infrastructure.adapter.in.dto;

import it.unina.dietiestates25.model.EnergyClass;
import it.unina.dietiestates25.model.ListingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record HouseListingDto(

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
        int nRooms,

        @NotNull
        int nBathrooms,

        @NotNull
        int floor,

        @NotNull
        EnergyClass energyClass,

        Map<String, String> otherFeatures
) {
}
