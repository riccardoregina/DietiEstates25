package it.unina.dietiestates25.customer.port.in;

import it.unina.dietiestates25.customer.port.out.CustomerRepository;
import it.unina.dietiestates25.model.Customer;
import it.unina.dietiestates25.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public void signUp(Customer customer) {
        String email = customer.getEmail();
        Optional<User> existingUser = repository.findUserByEmail(email);
        if (existingUser.isPresent()) {
            throw new IllegalStateException(String.format("User with the email address '%s' already exists.", email));
        }
        repository.save(customer);
    }
}
