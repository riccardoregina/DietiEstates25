package it.unina.dietiestates25.listing.model.search;

import it.unina.dietiestates25.auth.model.User;
import it.unina.dietiestates25.listing.model.EnergyClass;
import it.unina.dietiestates25.listing.model.ListingType;
import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

@Entity
@DiscriminatorValue("HOUSE")
public class HouseSearch extends Search {
    @Column(
            name = "n_rooms_min"
    )
    private Integer nRoomsMin;

    @Column(
            name = "n_rooms_max"
    )
    private Integer nRoomsMax;

    @Column(
            name = "n_bathrooms_min"
    )
    private Integer nBathroomsMin;

    @Column(
            name = "n_bathrooms_max"
    )
    private Integer nBathroomsMax;

    @Column(
            name = "floor_min"
    )
    private Integer floorMin;

    @Column(
            name = "floor_max"
    )
    private Integer floorMax;

    @Column(
            name = "energy_class_min"
    )
    private EnergyClass energyClassMin;

    @Column(
            name = "elevator"
    )
    private Boolean elevator;

    public HouseSearch() {
    }

    public HouseSearch(User user,
                       ListingType listingType,
                       String region,
                       String city,
                       Point centerCoordinates,
                       Integer radius,
                       Integer priceMin,
                       Integer priceMax,
                       Integer squareMetersMin,
                       Integer squareMetersMax,
                       Integer nRoomsMin,
                       Integer nRoomsMax,
                       Integer nBathroomsMin,
                       Integer nBathroomsMax,
                       Integer floorMin,
                       Integer floorMax,
                       EnergyClass energyClassMin,
                       String agentId,
                       Boolean elevator) {
        super(user,
                listingType,
                region,
                city,
                centerCoordinates,
                radius,
                priceMin,
                priceMax,
                squareMetersMin,
                squareMetersMax,
                agentId);
        this.nRoomsMin = nRoomsMin;
        this.nRoomsMax = nRoomsMax;
        this.nBathroomsMin = nBathroomsMin;
        this.nBathroomsMax = nBathroomsMax;
        this.floorMin = floorMin;
        this.floorMax = floorMax;
        this.energyClassMin = energyClassMin;
        this.elevator = elevator;
    }

    public Integer getnRoomsMin() {
        return nRoomsMin;
    }

    public Integer getnRoomsMax() {
        return nRoomsMax;
    }

    public Integer getnBathroomsMin() {
        return nBathroomsMin;
    }

    public Integer getnBathroomsMax() {
        return nBathroomsMax;
    }

    public Integer getFloorMin() {
        return floorMin;
    }

    public Integer getFloorMax() {
        return floorMax;
    }

    public EnergyClass getEnergyClassMin() {
        return energyClassMin;
    }

    public Boolean hasElevator() {
        return elevator;
    }
}
