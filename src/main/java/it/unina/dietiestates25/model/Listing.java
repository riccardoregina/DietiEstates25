package it.unina.dietiestates25.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

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

    @Column(
            name = "timestamp",
            nullable = false
    )
    private LocalDateTime timestamp;

    @Column(
            name = "price_per_square_meter",
            nullable = false
    )
    private Integer pricePerSquareMeter;
    @Type(JsonType.class)
    @Column(
            name = "photos",
            columnDefinition = "jsonb"
    )
    private List<String> photos = new LinkedList<>();

    @JsonIgnore
    private static final List<String> BASIC_SORTING_CRITERIA = List.of(
            "timestamp",
            "price",
            "squareMeters",
            "pricePerSquareMeter");

    @JsonIgnore
    private static final Map<Class<? extends Listing>, List<String>> SORTING_CRITERIA_MAP = Map.of(
            HouseListing.class, Stream.concat(BASIC_SORTING_CRITERIA.stream(),
                    Stream.of("nRooms", "nBathrooms", "floor", "energyClass")).toList(),
            GarageListing.class, Stream.concat(BASIC_SORTING_CRITERIA.stream(), Stream.of("floor")).toList(),
            LandListing.class, BASIC_SORTING_CRITERIA,
            BuildingListing.class, BASIC_SORTING_CRITERIA
    );

    @ManyToMany(mappedBy = "starredListings")
    @JsonIgnore
    private Set<User> followingUsers = new HashSet<>();

    public Listing() {}

    public Listing(Agent agent,
                   String title,
                   int price,
                   String description,
                   int squareMeters,
                   ListingType listingType,
                   Location location,
                   Map<String, String> otherFeatures,
                   List<String> photos) {
        this.agent = agent;
        this.title = title;
        this.price = price;
        this.description = description;
        this.squareMeters = squareMeters;
        this.listingType = listingType;
        this.location = location;
        this.otherFeatures = (otherFeatures == null) ? new HashMap<>() : otherFeatures;
        this.pricePerSquareMeter = price / squareMeters;
        this.timestamp = LocalDateTime.now();
        this.photos = photos;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Integer getPricePerSquareMeter() {
        return pricePerSquareMeter;
    }

    public Set<User> getFollowingUsers() {
        return followingUsers;
    }

    public void addFollowingUser(User user) {
        followingUsers.add(user);
        user.getStarredListings().add(this);
    }

    public void removeFollowingUser(User user) {
        followingUsers.remove(user);
        user.getStarredListings().remove(this);
    }

    @PreUpdate
    private void updatePricePerMeter() {
        if (squareMeters != 0) {
            this.pricePerSquareMeter = (int) Math.round((double) price / squareMeters);
        } else {
            this.pricePerSquareMeter = 0;
        }
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public List<String> getPhotos() {
        return this.photos;
    }

    public static boolean isValidSortingCriteria(String sortBy, Class<? extends Listing> listingClass) {
        List<String> criteria = SORTING_CRITERIA_MAP.get(listingClass);
        return criteria != null && criteria.contains(sortBy);
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
                ", timestamp=" + timestamp +
                ", pricePerSquareMeter=" + pricePerSquareMeter +
                '}';
    }
}

