package it.unina.dietiestates25.manager.infrastructure.adapter.in;

import it.unina.dietiestates25.manager.infrastructure.adapter.in.dto.SignUpAgencyRequest;
import it.unina.dietiestates25.manager.infrastructure.adapter.in.dto.SignUpAgencyResponse;
import it.unina.dietiestates25.manager.port.in.AdminService;
import it.unina.dietiestates25.model.Admin;
import it.unina.dietiestates25.model.Agency;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/signup-agency")
public class RestAdminController {
    private final AdminService service;
    private final PasswordEncoder passwordEncoder;

    public RestAdminController(AdminService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<SignUpAgencyResponse> signUpAgency(@RequestBody SignUpAgencyRequest requestDto) {
        service.createAdminAndAgency(new Admin(
                requestDto.firstName(),
                requestDto.lastName(),
                requestDto.email(),
                requestDto.dob(),
                passwordEncoder.encode(requestDto.tempPassword()),
                new Agency(
                        requestDto.ragioneSociale(),
                        requestDto.partitaIva()
                )
        ));
        return ResponseEntity.ok(new SignUpAgencyResponse("Admin and agency created successfully"));
    }
}
