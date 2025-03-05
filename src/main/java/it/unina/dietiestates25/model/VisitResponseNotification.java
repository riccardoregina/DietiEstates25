package it.unina.dietiestates25.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@DiscriminatorValue("VISIT_RESPONSE")
public class VisitResponseNotification extends Notification {
    @OneToOne
    @JoinColumn(
            name = "visit_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "notification_visit_fk"
            )
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
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
