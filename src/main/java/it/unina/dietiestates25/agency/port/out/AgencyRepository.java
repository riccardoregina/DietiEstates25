package it.unina.dietiestates25.agency.port.out;

import it.unina.dietiestates25.agency.model.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyRepository extends JpaRepository<Agency, String> {
    boolean existsByPartitaIvaOrRagioneSociale(String partitaIva, String ragioneSociale);
}
