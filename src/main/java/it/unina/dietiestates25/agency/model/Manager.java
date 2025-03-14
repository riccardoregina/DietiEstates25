package it.unina.dietiestates25.agency.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.unina.dietiestates25.notification.model.NotificationSettings;
import it.unina.dietiestates25.auth.model.User;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Agency agency;
    public Manager() {}
    public Manager(String firstName,
                   String lastName,
                   String email,
                   LocalDate dob,
                   String passwordHash,
                   Agency agency,
                   String profilePicUrl) {
        super(firstName,
                lastName,
                email,
                dob,
                passwordHash,
                new NotificationSettings(true, true, false),
                profilePicUrl);
        this.agency = agency;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }
}
