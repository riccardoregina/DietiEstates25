package it.unina.dietiestates25.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("LAND")
public class LandListing extends Listing {
    private boolean building;

    public LandListing() {}

    public LandListing(Agent agent,
                       String title,
                       int price,
                       String description,
                       int squareMeters,
                       ListingType listingType,
                       Location location,
                       boolean building,
                       List<String> otherFeatures,
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
        this.building = building;
    }

    public boolean isBuilding() {
        return building;
    }

    public void setBuilding(boolean building) {
        this.building = building;
    }
}

