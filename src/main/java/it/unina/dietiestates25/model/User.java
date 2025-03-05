package it.unina.dietiestates25.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
//          user is not a valid table name for most dbs
        name = "users"
)
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @Column(
            name = "first_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String lastName;
    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT",
            unique = true
    )
    private String email;

    @Column(
            name = "dob",
            nullable = false,
            columnDefinition = "DATE"
    )
    private LocalDate dob;
    @Column(
            name = "password_hash",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @JsonIgnore
    private String passwordHash;
    @Column(
            name = "profile_pic_url",
            columnDefinition = "TEXT"
    )
    private String profilePicUrl;

    @Embedded
    @JsonIgnore
    private NotificationSettings notificationSettings;

    @ManyToMany
    @JoinTable(
            name = "user_listing",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    foreignKey = @ForeignKey(name = "userlisting_listing_fk")),
            inverseJoinColumns = @JoinColumn(
                    name = "listing_id",
                    foreignKey = @ForeignKey(name = "userlisting_user"))
    )
    @JsonIgnore
    private Set<Listing> starredListings = new HashSet<>();

    @PreRemove
    private void emptyStarredListings() {
        starredListings.forEach(listing -> listing.getFollowingUsers().remove(this));
    }

    public User() {}

    public User(String firstName,
                String lastName,
                String email,
                LocalDate dob,
                String passwordHash,
                NotificationSettings notificationSettings,
                String profilePicUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
        this.passwordHash = passwordHash;
        this.notificationSettings = notificationSettings;
        this.profilePicUrl = profilePicUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public NotificationSettings getNotificationSettings() {
        return notificationSettings;
    }

    public Set<Listing> getStarredListings() {
        return starredListings;
    }

    public void addStarredListing(Listing listing) {
        starredListings.add(listing);
        listing.getFollowingUsers().add(this);
    }

    public void removeStarredListing(Listing listing) {
        starredListings.remove(listing);
        listing.getFollowingUsers().remove(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                '}';
    }
}
