package it.unina.dietiestates25.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("AGENT")
@PrimaryKeyJoinColumn(
        name = "user_id",
        foreignKey = @ForeignKey(
                name = "agent_user_fk"
        )
)
public class Agent extends User {
    @ManyToOne
    @JoinColumn(
            name = "agency_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "agent_agency_fk"
            )
    )
    @JsonIgnore
    private Agency agency;

    @ManyToOne
    @JoinColumn(
            name = "manager_id",
            nullable = false,
            referencedColumnName = "user_id",
            foreignKey = @ForeignKey(
                    name = "agent_manager_fk"
            )
    )
    private Manager manager;

    public Agent() {
    }

    public Agent(String firstName,
                 String lastName,
                 String email,
                 LocalDate dob,
                 String passwordHash,
                 Agency agency,
                 Manager manager) {
        super(firstName, lastName, email, dob, passwordHash);
        this.agency = agency;
        this.manager = manager;
    }

    public Agency getAgency() {
        return agency;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
