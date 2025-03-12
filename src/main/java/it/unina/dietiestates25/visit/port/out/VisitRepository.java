package it.unina.dietiestates25.visit.port.out;

import it.unina.dietiestates25.visit.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, String> {
    @Query("SELECT v FROM Visit v WHERE (v.listing.agent.id = :agentId) AND" +
            "(EXTRACT(YEAR FROM v.dateTime) = :year) AND" +
            "(EXTRACT(MONTH FROM v.dateTime) = :month)" +
            "ORDER BY v.dateTime")
    List<Visit> findAllByAgentIdAndMonth(String agentId, int year, int month);
}
