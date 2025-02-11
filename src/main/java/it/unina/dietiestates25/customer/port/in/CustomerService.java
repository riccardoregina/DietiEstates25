package it.unina.dietiestates25.customer.port.in;

import it.unina.dietiestates25.customer.port.out.CustomerRepository;
import it.unina.dietiestates25.exception.EntityAlreadyExistsException;
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

    public Customer signUp(Customer customer)
            throws EntityAlreadyExistsException {
        String email = customer.getEmail();
        Optional<User> existingUser = repository.findUserByEmail(email);
        if (existingUser.isPresent()) {
            throw new EntityAlreadyExistsException(String.format("User with the email address '%s' already exists", email));
        }
        return repository.save(customer);
    }
}
