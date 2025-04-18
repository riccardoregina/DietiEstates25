package it.unina.dietiestates25.agency.model;

import it.unina.dietiestates25.notification.model.NotificationSettings;
import it.unina.dietiestates25.auth.model.User;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @Column(
            name = "bio",
            columnDefinition = "TEXT"
    )
    private String bio;

    @ManyToOne
    @JoinColumn(
            name = "agency_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "agent_agency_fk"
            )
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
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

	@Column(
            name = "average_review"
    )
    private Float averageReview = 0f;
	@Column(
            name = "num_of_reviews"
    )
    private Integer numOfReviews = 0;

    public Agent() {
    }

    public Agent(String firstName,
                 String lastName,
                 String email,
                 LocalDate dob,
                 String passwordHash,
                 Agency agency,
                 Manager manager,
                 String profilePicUrl,
                 String bio) {
        super(firstName,
                lastName,
                email,
                dob,
                passwordHash,
                new NotificationSettings(true, true, false),
                profilePicUrl);
        this.agency = agency;
        this.manager = manager;
        this.bio = bio;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Float getAverageReview() {
        return averageReview;
    }

	public Integer getNumOfReviews() {
		return numOfReviews;
	}

    public void addReview(Integer review) {
        if (numOfReviews == 0) {
            averageReview = Float.valueOf(review);
        } else {
            averageReview = (averageReview*numOfReviews + review)/(numOfReviews+1);
        }
        numOfReviews++;
    }

//    Not used currently
    public void removeReview(Float review) {
        if (numOfReviews == 0) {
			return;
        }
		if (numOfReviews == 1) {
			averageReview = 0f;
        } else {
			averageReview = (averageReview*numOfReviews - review)/(numOfReviews-1);
		}
		numOfReviews--;
    }

}
