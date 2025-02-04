package it.unina.dietiestates25.manager.port.out;

import it.unina.dietiestates25.model.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyRepository extends JpaRepository<Agency, String> {
}
