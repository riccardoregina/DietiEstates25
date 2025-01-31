package it.unina.dietiestates25.auth.infrastructure.adapter.in;

import it.unina.dietiestates25.auth.domain.port.in.UserService;
import it.unina.dietiestates25.auth.infrastructure.adapter.in.dto.LogInRequest;
import it.unina.dietiestates25.auth.infrastructure.adapter.in.dto.LogInResponse;
import it.unina.dietiestates25.auth.infrastructure.adapter.in.dto.SignUpRequest;
import it.unina.dietiestates25.auth.infrastructure.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest requestDto) {
        userService.signUp(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LogInResponse> logIn(@RequestBody LogInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        String token = JwtUtil.generateToken(request.email());
        return ResponseEntity.ok(new LogInResponse(request.email(), token));
    }

}
