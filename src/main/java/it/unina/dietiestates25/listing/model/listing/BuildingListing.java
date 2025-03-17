package it.unina.dietiestates25.listing.model.listing;

import it.unina.dietiestates25.agency.model.Agent;
import it.unina.dietiestates25.listing.model.ListingType;
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
                           boolean elevator) {
        super(agent,
                title,
                price,
                description,
                squareMeters,
                listingType,
                location,
                otherFeatures);
        this.elevator = elevator;
    }

    public boolean getElevator() {
        return elevator;
    }
}
