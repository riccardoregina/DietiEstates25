package it.unina.dietiestates25.listing.model.listing;

import it.unina.dietiestates25.agency.model.Agent;
import it.unina.dietiestates25.listing.model.ListingType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("GARAGE")
public class GarageListing extends Listing {
    @Column(
            name = "floor"
    )
    private int floor;

    public GarageListing() {}

    public GarageListing(Agent agent,
                         String title,
                         int price,
                         String description,
                         int squareMeters,
                         ListingType listingType,
                         Location location,
                         int floor,
                         List<String> otherFeatures) {
        super(agent,
                title,
                price,
                description,
                squareMeters,
                listingType,
                location,
                otherFeatures);
        this.floor = floor;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}