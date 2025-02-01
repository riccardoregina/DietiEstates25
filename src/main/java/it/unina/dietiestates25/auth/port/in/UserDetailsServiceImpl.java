package it.unina.dietiestates25.auth.port.in;

import it.unina.dietiestates25.model.User;
import it.unina.dietiestates25.auth.port.out.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Qualifier("userRepository")
  private final UserRepository<User> repository;

  public UserDetailsServiceImpl(UserRepository<User> repository) {
    this.repository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) {

    User user = repository.findUserByEmail(email).orElseThrow(() ->
        new IllegalStateException(String.format("User does not exist, email: %s", email)));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getPasswordHash())
        .build();
  }
}
