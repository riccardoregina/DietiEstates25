package it.unina.dietiestates25.auth.infrastructure.adapter.in;

import it.unina.dietiestates25.auth.infrastructure.adapter.in.dto.LogInRequest;
import it.unina.dietiestates25.auth.infrastructure.adapter.in.dto.LogInResponse;
import it.unina.dietiestates25.auth.infrastructure.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
public class RestAuthController {
    private final AuthenticationManager authenticationManager;

    public RestAuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LogInResponse> logIn(@Valid @RequestBody LogInRequest requestDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                requestDto.email(), requestDto.password()));
        String token = JwtUtil.generateToken(requestDto.email());
        return ResponseEntity.ok(new LogInResponse(requestDto.email(), token));
    }

}
