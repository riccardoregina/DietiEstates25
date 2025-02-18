package it.unina.dietiestates25.model;

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
                    String passwordHash) {
        super(firstName,
                lastName,
                email,
                dob,
                passwordHash,
                new NotificationSettings(true, true, true));
    }
}
