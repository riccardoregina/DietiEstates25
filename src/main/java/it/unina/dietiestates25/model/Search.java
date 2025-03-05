package it.unina.dietiestates25.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.locationtech.jts.geom.Point;
import java.time.LocalDateTime;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
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
    private Integer radius;

    @Column(
            name = "price_min"
    )
    private Integer priceMin;

    @Column(
            name = "price_max"
    )
    private Integer priceMax;

    @Column(
            name = "square_meters_min"
    )
    private Integer squareMetersMin;

    @Column(
            name = "square_meters_max"
    )
    private Integer squareMetersMax;

    @Transient
    private String agentId;

    @Column(
            name = "timestamp",
            nullable = false
    )
    private LocalDateTime timestamp;

    public Search() {
    }

    public Search(User user,
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
        this.timestamp = LocalDateTime.now();
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

    public Integer getRadius() {
        return radius;
    }

    public Integer getPriceMin() {
        return priceMin;
    }

    public Integer getPriceMax() {
        return priceMax;
    }

    public Integer getSquareMetersMin() {
        return squareMetersMin;
    }

    public Integer getSquareMetersMax() {
        return squareMetersMax;
    }

    public String getAgentId() {
        return agentId;
    }

    public LocalDateTime getTimestamp() {return timestamp;}
}
