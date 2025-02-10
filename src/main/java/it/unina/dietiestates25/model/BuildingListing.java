package it.unina.dietiestates25.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BUILDING")
public class BuildingListing extends Listing {
    public BuildingListing() {
    }

    public BuildingListing(Agent agent, String title, int price, String description, int squareMeters, ListingType listingType, Location location) {
        super(agent, title, price, description, squareMeters, listingType, location);
    }
}
