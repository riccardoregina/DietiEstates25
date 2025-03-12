package it.unina.dietiestates25.listing.model.search;

import it.unina.dietiestates25.auth.model.User;
import it.unina.dietiestates25.listing.model.ListingType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.locationtech.jts.geom.Point;

@Entity
@DiscriminatorValue("LAND")
public class LandSearch extends Search {
    @Column(
            name = "building"
    )
    private Boolean building;

    public LandSearch() {
    }

    public LandSearch(User user,
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
                      Boolean building) {
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
        this.building = building;
    }

    public Boolean getBuilding() {
        return building;
    }
}
