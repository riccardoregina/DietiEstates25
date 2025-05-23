package it.unina.dietiestates25.listing.model.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.unina.dietiestates25.auth.model.User;
import it.unina.dietiestates25.listing.model.ListingType;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.locationtech.jts.geom.Point;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "search_type", discriminatorType = DiscriminatorType.STRING)
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(
            name = "search_type",
            insertable = false,
            updatable = false)
    private String searchType;

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
            name = "listing_type"
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

    @JsonIgnore
    public Point getCenterCoordinates() {
        return centerCoordinates;
    }

    public Double getCenterLongitude() {
        return (centerCoordinates == null) ?
            null : centerCoordinates.getX();
    }

    public Double getCenterLatitude() {
        return (centerCoordinates == null) ?
            null : centerCoordinates.getY();
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getId() {
        return id;
    }

    public String getSearchType() {
        return searchType;
    }
}
