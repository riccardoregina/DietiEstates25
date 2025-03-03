package it.unina.dietiestates25.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("VISIT_RESPONSE")
public class VisitResponseNotification extends Notification {
    @OneToOne
    @JoinColumn(
            name = "visit_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "notification_visit_fk"
            )
    )
    private Visit visit;

    public VisitResponseNotification() {}

    public VisitResponseNotification(User user, String message, Visit visit) {
        super(user, message);
        this.visit = visit;
    }

    public Visit getVisit() {
        return visit;
    }
}
