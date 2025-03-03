package it.unina.dietiestates25.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("VISIT_REQUEST")
public class VisitRequestNotification extends Notification {
    @OneToOne
    @JoinColumn(
            name = "visitrequest_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "notification_visitrequest_fk"
            )
    )
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
