package it.unina.dietiestates25.auth.port.in;

import it.unina.dietiestates25.auth.port.out.UserRepository;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository<User> userRepository;

    public UserService(UserRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String email) throws EntityNotExistsException {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotExistsException(String.format("User does not exist, email: %s", email)));
    }
}
