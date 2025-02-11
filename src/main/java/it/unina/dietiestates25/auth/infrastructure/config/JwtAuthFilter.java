package it.unina.dietiestates25.auth.infrastructure.config;

import it.unina.dietiestates25.auth.infrastructure.util.JwtUtil;
import it.unina.dietiestates25.exception.ErrorDetails;
import it.unina.dietiestates25.exception.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * The JWT Auth filter used in Spring Security's filter chain
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final UserDetailsService userDetailsService;

  public JwtAuthFilter(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,  HttpServletResponse response,  FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String authHeader = request.getHeader("Authorization");
      String token = null;
      String username = null;
//  Get, if present, the JWT and the contained username
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        token = authHeader.substring(7);
        username = JwtUtil.extractUsername(token);
      }

//  If the accessToken is null. It will pass the request to next filter in the chain.
//  Any login and signup requests will not have jwt token in their header, therefore they will be passed to next filter chain.
      if (token == null) {
        filterChain.doFilter(request, response);
        return;
      }

//  If any accessToken is present, then it will check if the token is valid and then authenticate the request in security context
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (Boolean.TRUE.equals(JwtUtil.isTokenValid(token, userDetails))) {
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      }

      filterChain.doFilter(request, response);
    } catch (InvalidTokenException e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      var errorDetails = new ErrorDetails(
              HttpStatus.UNAUTHORIZED.value(),
              HttpStatus.UNAUTHORIZED.name(),
              e.getMessage(),
              request.getRequestURI());
      response.getWriter().write(errorDetails.toJson());
    }
  }

}
