package it.unina.dietiestates25.auth.infrastructure.adapter.in.dto;

public record LogInRequest(
        String email,
        String password
) {
}
