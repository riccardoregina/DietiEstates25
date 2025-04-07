package it.unina.dietiestates25.auth;

import com.github.javafaker.Faker;

import it.unina.dietiestates25.exception.InvalidTokenException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

class JwtUtilTests {
    private final Faker faker = new Faker();

    @Test
    void givenValidToken_whenIsTokenValidCalled_thenTrueIsReturned() throws InvalidTokenException {
        var email = faker.internet().emailAddress();
        var password = faker.internet().password();
        var userDetails = User.builder()
                        .username(email)
                        .password(password)
                        .build();
        var token = JwtUtilClassForTests.generateToken(email);
        Assertions.assertTrue(JwtUtilClassForTests.isTokenValid(token, userDetails));
    }

    @Test
    void givenInvalidToken_whenIsTokenValidCalled_thenInvalidTokenExceptionIsThrown() {
        var email = faker.internet().emailAddress();
        var password = faker.internet().password();
        var userDetails = User.builder()
                .username(email)
                .password(password)
                .build();
        var token = JwtUtilClassForTests.generateToken(email).toUpperCase();
        Assertions.assertThrows(InvalidTokenException.class,() -> JwtUtilClassForTests.isTokenValid(token, userDetails));
    }

    @Test
    void givenEmptyStringAsToken_whenIsTokenValidCalled_thenInvalidTokenExceptionIsThrown() {
        var email = faker.internet().emailAddress();
        var password = faker.internet().password();
        var userDetails = User.builder()
                .username(email)
                .password(password)
                .build();
        var token = "";
        Assertions.assertThrows(InvalidTokenException.class,() -> JwtUtilClassForTests.isTokenValid(token, userDetails));
    }

    @Test
    void givenExpiredToken_whenIsTokenValidCalled_thenInvalidTokenExceptionIsThrown() {
        var email = faker.internet().emailAddress();
        var password = faker.internet().password();
        var userDetails = User.builder()
                .username(email)
                .password(password)
                .build();
        var token = JwtUtilClassForTests.generateExpiredToken(email);
        Assertions.assertThrows(InvalidTokenException.class,() -> JwtUtilClassForTests.isTokenValid(token, userDetails));
    }
}
