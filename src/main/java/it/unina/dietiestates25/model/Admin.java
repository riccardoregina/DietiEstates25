package it.unina.dietiestates25.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.PrimaryKeyJoinColumn;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("ADMIN")
@PrimaryKeyJoinColumn(
        name = "manager_id",
        foreignKey = @ForeignKey(
                name = "admin_manager_fk"
        )
)
public class Admin extends Manager {
    public Admin() {
    }

    public Admin(String firstName,
                 String lastName,
                 String email,
                 LocalDate dob,
                 String passwordHash,
                 Agency agency,
                 String profilePicUrl) {
        super(firstName, lastName, email, dob, passwordHash, agency, profilePicUrl);
    }
}
