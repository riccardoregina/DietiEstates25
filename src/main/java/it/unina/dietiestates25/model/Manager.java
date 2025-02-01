package it.unina.dietiestates25.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("MANAGER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Manager extends User {
    @ManyToOne
    private Agency agency;
    public Manager() {}
    public Manager(String firstName,
                   String lastName,
                   String email,
                   LocalDate dob,
                   String passwordHash,
                   Agency agency) {
        super(firstName, lastName, email, dob, passwordHash);
        this.agency = agency;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    @Override
    public void update(User manager) {
        if (!(manager instanceof Manager)) {
            throw new IllegalArgumentException("Cannot update a Manager with a User.");
        }
        super.update(manager);
        if (!this.agency.equals(((Manager) manager).agency)) {
            this.agency = ((Manager) manager).agency;
        }
    }
}
