package it.unina.dietiestates25.model;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "search_user_fk"
            )
    )
    private User user;

    @Column(
            name = "listing_type",
            nullable = false
    )
    private ListingType listingType;

    @Column(
            name = "region"
    )
    private String region;

    @Column(
            name = "city"
    )
    private String city;

    @Column(
            name = "center_coordinates",
            columnDefinition = "geography(Point,4326)"
    )
    private Point centerCoordinates;

    @Column(
            name = "radius"
    )
    private Double radius;

    @Column(
            name = "price_min"
    )
    private Double priceMin;

    @Column(
            name = "price_max"
    )
    private Double priceMax;

    @Column(
            name = "square_meters_min"
    )
    private Double squareMetersMin;

    @Column(
            name = "square_meters_max"
    )
    private Double squareMetersMax;

    @Transient
    private String agentId;

    public Search() {
    }

    public Search(User user,
                  ListingType listingType,
                  String region,
                  String city,
                  Point centerCoordinates,
                  Double radius,
                  Double priceMin,
                  Double priceMax,
                  Double squareMetersMin,
                  Double squareMetersMax,
                  String agentId) {
        this.user = user;
        this.listingType = listingType;
        this.region = region;
        this.city = city;
        this.centerCoordinates = centerCoordinates;
        this.radius = radius;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.squareMetersMin = squareMetersMin;
        this.squareMetersMax = squareMetersMax;
        this.agentId = agentId;
    }

    public User getUser() {
        return user;
    }

    public ListingType getListingType() {
        return listingType;
    }

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

    public Point getCenterCoordinates() {
        return centerCoordinates;
    }

    public Double getRadius() {
        return radius;
    }

    public Double getPriceMin() {
        return priceMin;
    }

    public Double getPriceMax() {
        return priceMax;
    }

    public Double getSquareMetersMin() {
        return squareMetersMin;
    }

    public Double getSquareMetersMax() {
        return squareMetersMax;
    }

    public String getAgentId() {
        return agentId;
    }
}
