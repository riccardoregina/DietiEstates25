package it.unina.dietiestates25.auth.infrastructure.adapter.in;

import it.unina.dietiestates25.auth.infrastructure.adapter.in.dto.LogInRequest;
import it.unina.dietiestates25.auth.infrastructure.adapter.in.dto.LogInResponse;
import it.unina.dietiestates25.auth.infrastructure.util.JwtUtil;
import it.unina.dietiestates25.auth.port.in.UserService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.auth.model.User;
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
    private final UserService userService;

    public RestAuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LogInResponse> logIn(@Valid @RequestBody LogInRequest requestDto)
            throws EntityNotExistsException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                requestDto.email(), requestDto.password()));
        User user = userService.getUser(requestDto.email());
        String token = JwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(
                new LogInResponse(
                        user.getEmail(),
                        token,
                        user.getId(),
                        user.getClass().getSimpleName().toUpperCase()));
    }
}
