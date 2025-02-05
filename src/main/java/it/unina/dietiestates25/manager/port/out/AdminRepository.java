package it.unina.dietiestates25.manager.port.out;

import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends ManagerRepository {
    boolean existsByEmail(String email);
}
