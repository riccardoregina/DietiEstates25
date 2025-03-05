package it.unina.dietiestates25.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@DiscriminatorValue("LISTING")
public class ListingNotification extends Notification {
    @ManyToOne
    @JoinColumn(
            name = "listing_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "notification_listing_fk"
            )
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Listing listing;

    public ListingNotification() {}
    public ListingNotification(User user, String message, Listing listing) {
        super(user, message);
        this.listing = listing;
    }

    public Listing getListing() {
        return listing;
    }
}
