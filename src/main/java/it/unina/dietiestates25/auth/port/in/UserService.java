package it.unina.dietiestates25.auth.port.in;

import it.unina.dietiestates25.model.User;
import it.unina.dietiestates25.auth.port.out.UserRepository;
import it.unina.dietiestates25.auth.infrastructure.adapter.in.dto.SignUpRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Qualifier("userRepository")
    private final UserRepository<User> repository;


    public UserService(UserRepository<User> repository) {
        this.repository = repository;
    }

    public void signUp(User user) {
        String email = user.getEmail();
        Optional<User> existingUser = repository.findUserByEmail(email);
        if (existingUser.isPresent()) {
            throw new IllegalStateException(String.format("User with the email address '%s' already exists.", email));
        }
        repository.save(user);
    }
}
