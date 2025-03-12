package it.unina.dietiestates25.notification.model;

import it.unina.dietiestates25.auth.model.User;
import it.unina.dietiestates25.visit.model.VisitRequest;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@DiscriminatorValue("VISIT_REQUEST")
public class VisitRequestNotification extends Notification {
    @OneToOne
    @JoinColumn(
            name = "visitrequest_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "notification_visitrequest_fk"
            )
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private VisitRequest visitRequest;

    public VisitRequestNotification() {}

    public VisitRequestNotification(User user,
                                    String message,
                                    VisitRequest visitRequest) {
        super(user, message);
        this.visitRequest = visitRequest;
    }

    public VisitRequest getVisitRequest() {
        return visitRequest;
    }
}
