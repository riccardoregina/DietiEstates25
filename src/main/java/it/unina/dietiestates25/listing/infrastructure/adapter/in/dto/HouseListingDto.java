package it.unina.dietiestates25.listing.infrastructure.adapter.in.dto;

import it.unina.dietiestates25.model.EnergyClass;
import it.unina.dietiestates25.model.ListingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record HouseListingDto(

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
        @Positive
        Integer nRooms,

        @NotNull
        @Positive
        Integer nBathrooms,

        @NotNull
        Integer floor,

        @NotNull
        Boolean elevator,

        @NotNull
        EnergyClass energyClass,

        List<String> otherFeatures,

        List<String> photos
) {
}
