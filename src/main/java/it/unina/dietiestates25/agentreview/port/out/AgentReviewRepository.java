package it.unina.dietiestates25.agentreview.port.out;

import it.unina.dietiestates25.model.Agent;
import it.unina.dietiestates25.model.AgentReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentReviewRepository extends JpaRepository<AgentReview, String> {
    List<AgentReview> findAllByAgent(Agent agent);
}
