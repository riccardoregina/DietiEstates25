package it.unina.dietiestates25.agency.port.out;

import it.unina.dietiestates25.model.Admin;
import it.unina.dietiestates25.model.Agency;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends ManagerRepository {
    Admin findByAgency(Agency agency);

    @Query("SELECT a FROM Admin a WHERE a.email = :email")
    Optional<Admin> findAdminByEmail(String email);
}
