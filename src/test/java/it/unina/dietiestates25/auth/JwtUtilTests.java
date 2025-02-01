package it.unina.dietiestates25.auth;

import com.github.javafaker.Faker;
import it.unina.dietiestates25.auth.infrastructure.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

class JwtUtilTests {
    private final Faker faker = new Faker();

    @Test
    void givenValidToken_whenIsTokenValidCalled_thenTrueIsReturned() {
        var email = faker.internet().emailAddress();
        var password = faker.internet().password();
        var userDetails = User.builder()
                        .username(email)
                        .password(password)
                        .build();
        var token = JwtUtil.generateToken(email);
        Assertions.assertTrue(JwtUtil.isTokenValid(token, userDetails));
    }

    @Test
    void givenInvalidToken_whenIsTokenValidCalled_thenIllegalStateExceptionIsThrown() {
        var email = faker.internet().emailAddress();
        var password = faker.internet().password();
        var userDetails = User.builder()
                .username(email)
                .password(password)
                .build();
        var token = JwtUtil.generateToken(email).toUpperCase();
        Assertions.assertThrows(IllegalStateException.class,() -> JwtUtil.isTokenValid(token, userDetails));
    }
}
