package it.unina.dietiestates25.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "listing")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @ManyToOne
    @JoinColumn(
            name = "agent_id",
            nullable = false,
            referencedColumnName = "user_id",
            foreignKey = @ForeignKey(
                    name = "listing_agent_fk"
            )
    )
    private Agent agent;
    @Column(
            name = "title",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String title;
    @Column(
            name = "price",
            nullable = false
    )
    private int price;
    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;
    @Column(
            name = "square_meters",
            nullable = false
    )
    private int squareMeters;
    @Column(
            name = "listing_type",
            nullable = false
    )
    private ListingType listingType;

    @Embedded
    private Location location;

    @Type(JsonType.class)
    @Column(
            name = "other_features",
            columnDefinition = "jsonb"
    )
    private Map<String, String> otherFeatures = new HashMap<>();

    public Listing() {}

    public Listing(Agent agent,
                   String title,
                   int price,
                   String description,
                   int squareMeters,
                   ListingType listingType,
                   Location location,
                   Map<String, String> otherFeatures) {
        this.agent = agent;
        this.title = title;
        this.price = price;
        this.description = description;
        this.squareMeters = squareMeters;
        this.listingType = listingType;
        this.location = location;
        this.otherFeatures = (otherFeatures == null) ? new HashMap<>() : otherFeatures;
    }

    public String getId() {
        return id;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(int squareMeters) {
        this.squareMeters = squareMeters;
    }

    public ListingType getListingType() {
        return listingType;
    }

    public void setListingType(ListingType listingType) {
        this.listingType = listingType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Map<String, String> getOtherFeatures() {
        return otherFeatures;
    }

    public void setOtherFeatures(Map<String, String> otherFeatures) {
        this.otherFeatures = otherFeatures;
    }

    @Override
    public String toString() {
        return "Listing{" +
                "id='" + id + '\'' +
                ", agent=" + agent +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", squareMeters=" + squareMeters +
                ", listingType=" + listingType +
                ", location=" + location +
                ", otherFeatures=" + otherFeatures +
                '}';
    }
}

