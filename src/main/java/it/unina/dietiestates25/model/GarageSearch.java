package it.unina.dietiestates25.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.locationtech.jts.geom.Point;

@Entity
@DiscriminatorValue("GARAGE")
public class GarageSearch extends Search {

    @Column(
            name = "floor_min"
    )
    private Integer floorMin;

    @Column(
            name = "floor_max"
    )
    private Integer floorMax;

    public GarageSearch() {
    }

    public GarageSearch(User user,
                        ListingType listingType,
                        String region,
                        String city,
                        Point centerCoordinates,
                        Integer radius,
                        Integer priceMin,
                        Integer priceMax,
                        Integer squareMetersMin,
                        Integer squareMetersMax,
                        String agentId,
                        Integer floorMin,
                        Integer floorMax) {
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
        this.floorMin = floorMin;
        this.floorMax = floorMax;
    }

    public Integer getFloorMax() {
        return floorMax;
    }

    public Integer getFloorMin() {
        return floorMin;
    }
}
