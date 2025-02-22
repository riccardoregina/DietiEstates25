package it.unina.dietiestates25.visit.port.out;

import it.unina.dietiestates25.model.VisitRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRequestRepository extends JpaRepository<VisitRequest, String> {
}
