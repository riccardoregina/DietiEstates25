package it.unina.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Map;

@Entity
@DiscriminatorValue("HOUSE")
public class HouseListing extends Listing {
    @Column(
            name = "n_rooms"
    )
    private int nRooms;
    @Column(
            name = "n_bathrooms"
    )
    private int nBathrooms;
    @Column(
            name = "floor"
    )
    private int floor;
    @Column(
            name = "energy_class"
    )
    private EnergyClass energyClass;

    public HouseListing() {}

    public HouseListing(Agent agent,
                        String title,
                        int price,
                        String description,
                        int squareMeters,
                        ListingType listingType,
                        Location location,
                        int nRooms,
                        int nBathrooms,
                        int floor,
                        EnergyClass energyClass,
                        Map<String, String> otherFeatures) {
        super(agent,
                title,
                price,
                description,
                squareMeters,
                listingType,
                location,
                otherFeatures);
        this.nRooms = nRooms;
        this.nBathrooms = nBathrooms;
        this.floor = floor;
        this.energyClass = energyClass;
    }

    public int getnRooms() {
        return nRooms;
    }

    public void setnRooms(int nRooms) {
        this.nRooms = nRooms;
    }

    public int getnBathrooms() {
        return nBathrooms;
    }

    public void setnBathrooms(int nBathrooms) {
        this.nBathrooms = nBathrooms;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public EnergyClass getEnergyClass() {
        return energyClass;
    }

    public void setEnergyClass(EnergyClass energyClass) {
        this.energyClass = energyClass;
    }
}
