package it.unina.dietiestates25.manager.port.out;

import it.unina.dietiestates25.auth.port.out.UserRepository;
import it.unina.dietiestates25.model.Manager;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerRepository extends UserRepository<Manager> {
    @Query("SELECT m FROM Manager m WHERE m.agency = :agency")
    List<Manager> findAllByAgency(String agency);
}
