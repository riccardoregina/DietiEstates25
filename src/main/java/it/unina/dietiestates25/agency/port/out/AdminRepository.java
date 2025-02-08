package it.unina.dietiestates25.agency.port.out;

import it.unina.dietiestates25.model.Admin;
import it.unina.dietiestates25.model.Agency;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends ManagerRepository {
    Admin findByAgency(Agency agency);
}
