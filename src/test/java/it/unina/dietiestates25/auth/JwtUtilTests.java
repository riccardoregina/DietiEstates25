package it.unina.dietiestates25.auth;

import it.unina.dietiestates25.auth.infrastructure.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

class JwtUtilTests {
    @Test
    void givenValidToken_whenIsTokenValidCalled_thenTrueIsReturned() {
        var email = "foo@bar.com";
        var password = "verySecretPassword";
        var userDetails = User.builder()
                        .username(email)
                        .password(password)
                        .build();
        var token = JwtUtil.generateToken(email);
        Assertions.assertTrue(JwtUtil.isTokenValid(token, userDetails));
    }

    @Test
    void givenInvalidToken_whenIsTokenValidCalled_thenIllegalStateExceptionIsThrown() {
        var email = "foo@bar.com";
        var password = "verySecretPassword";
        var userDetails = User.builder()
                .username(email)
                .password(password)
                .build();
        var token = JwtUtil.generateToken(email).toUpperCase();
        Assertions.assertThrows(IllegalStateException.class,() -> JwtUtil.isTokenValid(token, userDetails));
    }
}
