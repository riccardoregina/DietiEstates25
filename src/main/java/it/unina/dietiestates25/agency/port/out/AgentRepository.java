package it.unina.dietiestates25.agency.port.out;

import it.unina.dietiestates25.auth.port.out.UserRepository;
import it.unina.dietiestates25.agency.model.Agency;
import it.unina.dietiestates25.agency.model.Agent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends UserRepository<Agent> {
    Optional<Agent> findByEmail(String email);

    @Query("SELECT a FROM Agent a WHERE a.manager.id = :managerId")
    List<Agent> findAllByManager(String managerId);

    List<Agent> findAllByAgency(Agency agency);

    @Query("SELECT a.agency FROM Agent a WHERE a.email = :email")
    Optional<Agency> findAgencyByAgentEmail(String email);
}
