package it.unina.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("HOUSE")
public class HouseListing extends Listing {
    @Column(
            name = "n_rooms",
            nullable = false
    )
    private int nRooms;
    @Column(
            name = "n_bathrooms",
            nullable = false
    )
    private int nBathrooms;
    @Column(
            name = "floor",
            nullable = false
    )
    private int floor;
    @Column(
            name = "energy_class",
            nullable = false
    )
    private EnergyClass energyClass;

    public HouseListing() {}

    public HouseListing(Agent agent, String title, int price, String description, int squareMeters, ListingType listingType, Location location, int nRooms, int nBathrooms, int floor, EnergyClass energyClass) {
        super(agent, title, price, description, squareMeters, listingType, location);
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
