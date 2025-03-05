package it.unina.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("BUILDING")
public class BuildingListing extends Listing {
    @Column(
            name = "elevator"
    )
    private boolean elevator;

    public BuildingListing() {
    }

    public BuildingListing(Agent agent,
                           String title,
                           int price,
                           String description,
                           int squareMeters,
                           ListingType listingType,
                           Location location,
                           List<String> otherFeatures,
                           List<String> photos,
                           boolean elevator) {
        super(agent,
                title,
                price,
                description,
                squareMeters,
                listingType,
                location,
                otherFeatures,
                photos);
        this.elevator = elevator;
    }

    public boolean getElevator() {
        return elevator;
    }
}
