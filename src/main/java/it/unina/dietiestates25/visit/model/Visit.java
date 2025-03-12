package it.unina.dietiestates25.visit.model;

import it.unina.dietiestates25.customer.model.Customer;
import it.unina.dietiestates25.listing.model.listing.Listing;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(
            name = "customer_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "visit_customer_fk"
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
                    name = "visit_listing_fk"
            )
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Listing listing;

    @Column(
            name = "date_time",
            nullable = false
    )
    private LocalDateTime dateTime;

    public Visit(Customer customer,
                 Listing listing,
                 LocalDateTime dateTime) {
        this.customer = customer;
        this.listing = listing;
        this.dateTime = dateTime;
    }

    public Visit() {

    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Listing getListing() {
        return listing;
    }
}
