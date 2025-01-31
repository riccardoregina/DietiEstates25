package it.unina.dietiestates25.auth.domain.port.in;

import it.unina.dietiestates25.auth.domain.model.User;
import it.unina.dietiestates25.auth.domain.port.out.UserRepository;
import it.unina.dietiestates25.auth.infrastructure.adapter.in.dto.SignUpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUp(SignUpRequest request) {
        String email = request.email();
        Optional<User> existingUser = repository.findUserByEmail(email);
        if (existingUser.isPresent()) {
            throw new IllegalStateException(String.format("User with the email address '%s' already exists.", email));
        }

        String passwordHash = passwordEncoder.encode(request.password());
        User user = new User(request.firstName(), request.lastName(), email, request.dob(), passwordHash);
        repository.save(user);
    }
}
