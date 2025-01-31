package it.unina.dietiestates25.auth.domain.port.in;

import it.unina.dietiestates25.auth.domain.model.User;
import it.unina.dietiestates25.auth.domain.port.out.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository repository;

  public UserDetailsServiceImpl(UserRepository repository) {
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
