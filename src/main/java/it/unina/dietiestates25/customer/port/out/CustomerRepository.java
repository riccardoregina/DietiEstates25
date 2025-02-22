package it.unina.dietiestates25.customer.port.out;

import it.unina.dietiestates25.auth.port.out.UserRepository;
import it.unina.dietiestates25.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends UserRepository<Customer> {
    Optional<Customer> findByEmail(String email);
}
