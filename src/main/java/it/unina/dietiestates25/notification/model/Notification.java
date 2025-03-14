package it.unina.dietiestates25.notification.model;

import it.unina.dietiestates25.auth.model.User;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "notification_type", discriminatorType = DiscriminatorType.STRING)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "notification_user_fk"
            )
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(
            name = "notification_type",
            insertable = false,
            updatable = false)
    private String type;

    @Column(
            name = "timestamp",
            nullable = false
    )
    private LocalDateTime timestamp;

    @Column(
            name = "message"
    )
    private String message;

    @Column(
            name = "read"
    )
    private boolean read;

    public Notification() {}

    public Notification(User user, String message) {
        this.user = user;
        this.message = message;
        this.read = false;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isRead() {
        return read;
    }

    public String getType() {
        return type;
    }
}
