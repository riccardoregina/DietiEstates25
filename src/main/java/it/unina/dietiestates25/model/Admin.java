package it.unina.dietiestates25.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Manager {
    private String palle;
    public Admin() {}

    public Admin(String firstName, String lastName, String email, LocalDate dob, String passwordHash, Agency agency, String palle) {
        super(firstName, lastName, email, dob, passwordHash, agency);
        this.palle = palle;
    }

    public String getPalle() {
        return palle;
    }

    public void setPalle(String palle) {
        this.palle = palle;
    }
}
