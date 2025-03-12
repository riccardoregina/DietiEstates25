package it.unina.dietiestates25.notification.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class NotificationSettings {

    @Column(
            name = "starred_listings",
            nullable = false
    )
    private boolean starredListings;

    @Column(
            name = "visit",
            nullable = false
    )
    private boolean visit;

    @Column(
            name = "recommended_listings",
            nullable = false
    )
    private boolean recommendedListings;

    public NotificationSettings() {
    }

    public NotificationSettings(boolean starredListings,
                                boolean visit,
                                boolean recommendedListings) {
        this.starredListings = starredListings;
        this.visit = visit;
        this.recommendedListings = recommendedListings;
    }

    public boolean isStarredListings() {
        return starredListings;
    }

    public boolean isVisit() {
        return visit;
    }

    public boolean isRecommendedListings() {
        return recommendedListings;
    }
}
