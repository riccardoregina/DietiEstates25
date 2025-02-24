package it.unina.dietiestates25.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue("BUILDING")
public class BuildingListing extends Listing {
    public BuildingListing() {
    }

    public BuildingListing(Agent agent,
                           String title,
                           int price,
                           String description,
                           int squareMeters,
                           ListingType listingType,
                           Location location,
                           Map<String, String> otherFeatures,
                           List<String> photos) {
        super(agent,
                title,
                price,
                description,
                squareMeters,
                listingType,
                location,
                otherFeatures,
                photos);
    }
}
