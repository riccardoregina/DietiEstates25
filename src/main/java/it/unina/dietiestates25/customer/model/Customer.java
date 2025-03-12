package it.unina.dietiestates25.customer.model;

import it.unina.dietiestates25.auth.model.User;
import it.unina.dietiestates25.notification.model.NotificationSettings;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.PrimaryKeyJoinColumn;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("CUSTOMER")
@PrimaryKeyJoinColumn(
        name = "user_id",
        foreignKey = @ForeignKey(
                name = "customer_user_fk"
        )
)
public class Customer extends User {
    public Customer() {
    }

    public Customer(String firstName,
                    String lastName,
                    String email,
                    LocalDate dob,
                    String passwordHash,
                    String profilePicUrl) {
        super(firstName,
                lastName,
                email,
                dob,
                passwordHash,
                new NotificationSettings(true, true, true),
                profilePicUrl);
    }
}
