package it.unina.dietiestates25.customer.port.in;

import it.unina.dietiestates25.agency.infrastructure.adapter.in.dto.UserDto;
import it.unina.dietiestates25.customer.port.out.CustomerRepository;
import it.unina.dietiestates25.exception.EntityAlreadyExistsException;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.customer.model.Customer;
import it.unina.dietiestates25.auth.model.User;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository repository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
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

    public Customer getCustomer(String email) throws EntityNotExistsException {
        return repository
                .findByEmail(email).orElseThrow(() ->
                        new EntityNotExistsException(String.format("Customer with email '%s' not found", email)));
    }

    @Transactional
    public void updateCustomer(UserDto userDto, Customer customer) {
        customer.setEmail(userDto.email());
        customer.setFirstName(userDto.firstName());
        customer.setLastName(userDto.lastName());
        customer.setDob(userDto.dob());
        customer.setPasswordHash(passwordEncoder.encode(userDto.password()));
        if (userDto.profilePicUrl() != null) customer.setProfilePicUrl(userDto.profilePicUrl());
    }
}
