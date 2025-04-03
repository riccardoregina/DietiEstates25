package it.unina.dietiestates25.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.unina.dietiestates25.exception.InvalidTokenException;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtUtilClassForTests {
  private static final String SECRET = "MOCK_SECRET_SHALALALALALALALALALALALALALALALA";
  private static final int MINUTES = 60;
  private JwtUtilClassForTests() {}

  public static String generateToken(String email) {
    var now = Instant.now();
    var secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());
    return Jwts.builder()
        .subject(email)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plus(MINUTES, ChronoUnit.MINUTES)))
        .signWith(secretKey)
        .compact();
  }

  public static String generateExpiredToken(String email) {
    var instantInThePast = Instant.now().minus(60, ChronoUnit.MINUTES);
    var secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());
    return Jwts.builder()
        .subject(email)
        .issuedAt(Date.from(instantInThePast))
        .expiration(Date.from(instantInThePast.plus(MINUTES, ChronoUnit.MINUTES)))
        .signWith(secretKey)
        .compact();
  }

  public static String extractUsername(String token)
          throws InvalidTokenException {
    return getTokenBody(token).getSubject();
  }

  public static Boolean isTokenValid(String token, UserDetails userDetails)
          throws InvalidTokenException {
    final String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private static Claims getTokenBody(String token)
          throws InvalidTokenException {
    var secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());
    try {
      return Jwts
          .parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (JwtException e) { // Invalid signature or expired token
        throw new InvalidTokenException("Access denied: " + e.getMessage());
    } catch (Exception e) {
        throw new InvalidTokenException("Probably malformed token: " + e.getMessage());
    }
  }

  private static boolean isTokenExpired(String token)
          throws InvalidTokenException {
    Claims claims = getTokenBody(token);
    return claims.getExpiration().before(new Date());
  }
}
