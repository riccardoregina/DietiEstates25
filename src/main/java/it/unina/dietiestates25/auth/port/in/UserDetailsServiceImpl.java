package it.unina.dietiestates25.auth.port.in;

import it.unina.dietiestates25.model.User;
import it.unina.dietiestates25.auth.port.out.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Qualifier("userRepository")
  private final UserRepository<User> repository;

  public UserDetailsServiceImpl(UserRepository<User> repository) {
    this.repository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String email)
          throws UsernameNotFoundException {

    User user = repository.findUserByEmail(email).orElseThrow(() ->
        new UsernameNotFoundException(String.format("User does not exist, email: %s", email)));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getPasswordHash())
        .roles(user.getClass().getSimpleName().toUpperCase())
        .build();
  }
}
