package it.unina.dietiestates25.listing.model.listing;

import it.unina.dietiestates25.agency.model.Agent;
import it.unina.dietiestates25.listing.model.ListingType;
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
                       List<String> otherFeatures) {
        super(agent,
                title,
                price,
                description,
                squareMeters,
                listingType,
                location,
                otherFeatures);
        this.building = building;
    }

    public boolean isBuilding() {
        return building;
    }

    public void setBuilding(boolean building) {
        this.building = building;
    }
}

