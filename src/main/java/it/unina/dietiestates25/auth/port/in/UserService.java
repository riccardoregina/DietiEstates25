package it.unina.dietiestates25.auth.port.in;

import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import it.unina.dietiestates25.auth.port.out.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  @Qualifier("userRepository")
  private final UserRepository<User> repository;

  public UserService(UserRepository<User> repository) {
    this.repository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String email)
          throws UsernameNotFoundException {

    User user = repository.findUserByEmail(email).orElseThrow(() ->
        new UsernameNotFoundException("User not found"));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getPasswordHash())
        .roles(user.getClass().getSimpleName().toUpperCase())
        .build();
  }

    public User getUser(String email) throws EntityNotExistsException {
        return repository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotExistsException(String.format("User does not exist, email: %s", email)));
    }

    public static boolean hasRole(UserDetails userDetails, String role) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(role));
    }
}
