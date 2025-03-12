package it.unina.dietiestates25.visit.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import it.unina.dietiestates25.customer.model.Customer;
import it.unina.dietiestates25.listing.model.listing.Listing;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
            name = "customer_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "visitrequest_customer_fk"
            )
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Customer customer;

    @ManyToOne
    @JoinColumn(
            name = "listing_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "visitrequest_listing_fk"
            )
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Listing listing;

    @Column(
            name = "timestamp",
            nullable = false
    )
    private LocalDateTime timestamp;

    public VisitRequest(Customer customer, Listing listing, List<TimeSlot> timeSlots) {
        this.customer = customer;
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

    public Customer getCustomer() {
        return customer;
    }

    public Listing getListing() {
        return listing;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
