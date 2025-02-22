package it.unina.dietiestates25.model;

import jakarta.persistence.*;

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
