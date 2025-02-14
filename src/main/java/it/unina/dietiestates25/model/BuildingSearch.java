package it.unina.dietiestates25.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.locationtech.jts.geom.Point;

@Entity
@DiscriminatorValue("BUILDING")
public class BuildingSearch extends Search {
    public BuildingSearch() {
    }

    public BuildingSearch(User user,
                          ListingType listingType,
                          String region,
                          String city,
                          Point centerCoordinates,
                          Integer radius,
                          Integer priceMin,
                          Integer priceMax,
                          Integer squareMetersMin,
                          Integer squareMetersMax,
                          String agentId) {
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
    }
}
