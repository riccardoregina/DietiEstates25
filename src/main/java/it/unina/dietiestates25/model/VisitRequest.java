package it.unina.dietiestates25.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
public class VisitRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Type(JsonType.class)
    @Column(
            name = "time_slots",
            columnDefinition = "jsonb"
    )
    List<TimeSlot> timeSlots = new LinkedList<>();

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "visitrequest_user_fk"
            )
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "listing_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "visitrequest_listing_fk"
            )
    )
    private Listing listing;

    @Column(
            name = "timestamp",
            nullable = false
    )
    private LocalDateTime timestamp;

    public VisitRequest(User user, Listing listing, List<TimeSlot> timeSlots) {
        this.user = user;
        this.listing = listing;
        this.timeSlots = timeSlots;
        timestamp = LocalDateTime.now();
    }

    public VisitRequest() {

    }

    public String getId() {
        return id;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public User getUser() {
        return user;
    }

    public Listing getListing() {
        return listing;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
