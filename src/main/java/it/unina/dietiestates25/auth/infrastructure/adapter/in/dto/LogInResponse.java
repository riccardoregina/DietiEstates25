package it.unina.dietiestates25.auth.infrastructure.adapter.in.dto;

public record LogInResponse(
        String email,
        String token,
        String id,
        String role
) {
}
