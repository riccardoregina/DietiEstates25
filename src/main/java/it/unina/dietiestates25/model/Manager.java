package it.unina.dietiestates25.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("MANAGER")
@PrimaryKeyJoinColumn(
        name = "user_id",
        foreignKey = @ForeignKey(
                name = "manager_user_fk"
        )
)
public class Manager extends User {
    @ManyToOne
    @JoinColumn(
            name = "agency_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "manager_agency_fk"
            )
    )
    @JsonIgnore
    private Agency agency;
    public Manager() {}
    public Manager(String firstName,
                   String lastName,
                   String email,
                   LocalDate dob,
                   String passwordHash,
                   Agency agency) {
        super(firstName,
                lastName,
                email,
                dob,
                passwordHash,
                new NotificationSettings(true, true, false));
        this.agency = agency;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }
}
