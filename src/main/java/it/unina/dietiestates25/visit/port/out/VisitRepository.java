package it.unina.dietiestates25.visit.port.out;

import it.unina.dietiestates25.visit.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, String> {
    @Query("SELECT v FROM Visit v WHERE (v.listing.agent.id = :agentId) AND" +
            "(v.dateTime BETWEEN :start AND :end)" +
            "ORDER BY v.dateTime")
    List<Visit> findAllBetweenStartAndEndByAgentId(String agentId, LocalDateTime start, LocalDateTime end);
}
