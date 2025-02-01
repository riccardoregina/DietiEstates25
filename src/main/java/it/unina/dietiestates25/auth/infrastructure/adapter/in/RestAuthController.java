package it.unina.dietiestates25.auth.infrastructure.adapter.in;

import it.unina.dietiestates25.auth.port.in.UserService;
import it.unina.dietiestates25.auth.infrastructure.adapter.in.dto.LogInRequest;
import it.unina.dietiestates25.auth.infrastructure.adapter.in.dto.LogInResponse;
import it.unina.dietiestates25.auth.infrastructure.adapter.in.dto.SignUpRequest;
import it.unina.dietiestates25.auth.infrastructure.util.JwtUtil;
import it.unina.dietiestates25.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
public class RestAuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RestAuthController(AuthenticationManager authenticationManager,
                              UserService userService,
                              PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest requestDto) {
        userService.signUp(new User(
                requestDto.firstName(),
                requestDto.lastName(),
                requestDto.email(),
                requestDto.dob(),
                passwordEncoder.encode(requestDto.password())));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LogInResponse> logIn(@RequestBody LogInRequest requestDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.email(), requestDto.password()));
        String token = JwtUtil.generateToken(requestDto.email());
        return ResponseEntity.ok(new LogInResponse(requestDto.email(), token));
    }

}
