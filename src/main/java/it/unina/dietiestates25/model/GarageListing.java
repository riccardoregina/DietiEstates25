package it.unina.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GARAGE")
public class GarageListing extends Listing {
    @Column(
            name = "floor",
            nullable = false
    )
    private int floor;

    public GarageListing() {}

    public GarageListing(Agent agent, String title, int price, String description, int squareMeters, ListingType listingType, Location location, int floor) {
        super(agent, title, price, description, squareMeters, listingType, location);
        this.floor = floor;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}