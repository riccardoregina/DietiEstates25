package it.unina.dietiestates25.agency.port.out;

import it.unina.dietiestates25.auth.port.out.UserRepository;
import it.unina.dietiestates25.agency.model.Agency;
import it.unina.dietiestates25.agency.model.Manager;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerRepository extends UserRepository<Manager> {
    @Query("SELECT m FROM Manager m WHERE m.agency.id = :agencyId AND m.email <> :adminEmail")
    List<Manager> findAllByAgency(String agencyId, String adminEmail);

    @Query(
            value = "SELECT m.agency FROM Manager m WHERE m.email = :email"
    )
    Optional<Agency> findAgencyByManagerEmail(@Param("email") String email);

    Optional<Manager> findByEmail(String email);
}
