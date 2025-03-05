package it.unina.dietiestates25.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
public class AgentReview {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    @JoinColumn(
            name = "customer_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "agentreview_customer_fk"
            )
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Customer customer;

    @ManyToOne
    @JoinColumn(
            name = "agent_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "agentreview_agent_fk"
            )
    )
    @OnDelete(action = OnDeleteAction.CASCADE)

    private Agent agent;
    @Column(
            name = "value",
            nullable = false
    )
    private Integer value;
    @Column(
            name = "comment",
            columnDefinition = "TEXT"
    )
    private String comment;
    @Column(
            name = "timestamp",
            nullable = false
    )
    private LocalDateTime timestamp;

    public AgentReview() {
    }

    public AgentReview(Customer customer, Agent agent, Integer value, String comment) {
        this.customer = customer;
        this.agent = agent;
        this.value = value;
        this.comment = comment;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Agent getAgent() {
        return agent;
    }

    public Integer getValue() {
        return value;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
