package it.unina.dietiestates25.auth.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final UserDetailsService userDetailsService;
  private final JwtAuthFilter jwtAuthFilter;

  public SecurityConfig(UserDetailsService userDetailsService, JwtAuthFilter jwtAuthFilter) {
    this.userDetailsService = userDetailsService;
    this.jwtAuthFilter = jwtAuthFilter;
  }

  private static final String LOGIN_PATH = "/api/auth/**";
  private static final String CUSTOMERS_PATH = "/api/customers/**";
  private static final String AGENCIES_PATH = "/api/agencies/**";

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    return http
//        CORS(Cross-origin resource sharing) is just to avoid if you run javascript across different domains
        .cors(AbstractHttpConfigurer::disable)
//        CSRF(Cross-Site Request Forgery)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//        Set permissions on endpoints
        .authorizeHttpRequests(auth -> auth
//            our public endpoints
            .requestMatchers(HttpMethod.POST, LOGIN_PATH)
                .permitAll()
            .requestMatchers(HttpMethod.POST, CUSTOMERS_PATH)
                .permitAll()
            .requestMatchers(HttpMethod.POST, AGENCIES_PATH)
                .permitAll()
//            our private endpoints
            .anyRequest().authenticated())
        .authenticationManager(authenticationManager)

//        We need jwt filter before the UsernamePasswordAuthenticationFilter.
//        Since we need every request to be authenticated before going through spring security filter.
//        (UsernamePasswordAuthenticationFilter creates a UsernamePasswordAuthenticationToken from a username and password that are submitted in the HttpServletRequest.)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//        .formLogin(login ->
//                login.failureHandler(new AuthenticationFailureHandlerImpl()))
//        .exceptionHandling(exception ->
//                exception.accessDeniedHandler(new AccessDeniedHandlerImpl())
//        )
        .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    return authenticationManagerBuilder.build();
  }
}

