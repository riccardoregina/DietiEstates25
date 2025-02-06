package it.unina.dietiestates25.agency.infrastructure.adapter.in;

import it.unina.dietiestates25.agency.infrastructure.adapter.in.dto.ManagerDto;
import it.unina.dietiestates25.agency.infrastructure.adapter.in.dto.SignUpAgencyRequest;
import it.unina.dietiestates25.agency.port.in.AgencyService;
import it.unina.dietiestates25.exception.UserAlreadyExistsException;
import it.unina.dietiestates25.exception.AgencyAlreadyExistsException;
import it.unina.dietiestates25.model.Admin;
import it.unina.dietiestates25.model.Agency;
import it.unina.dietiestates25.model.Manager;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api/agencies")
public class RestAgencyController {

    private final AgencyService agencyService;
    private final PasswordEncoder passwordEncoder;

    public RestAgencyController(AgencyService agencyService, PasswordEncoder passwordEncoder) {
        this.agencyService = agencyService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<Admin> signUpAgency(@Valid @RequestBody SignUpAgencyRequest requestDto)
            throws AgencyAlreadyExistsException, UserAlreadyExistsException {
        Admin admin = agencyService.createAdminAndAgency(new Admin(
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
        return ResponseEntity.created(URI.create("/api/agencies/" +
                admin.getAgency().getId() + "/managers/" + admin.getId())).body(admin);
    }

    @PostMapping("/{agency-id}/managers")
    public ResponseEntity<Manager> createManager(@Valid @RequestBody ManagerDto managerDto,
                                                 @PathVariable("agency-id") String agencyId,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        Agency agency = agencyService.getAgencyByManagerEmail(userDetails.getUsername());
        if (!agency.getId().equals(agencyId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Manager createdManager = agencyService.createManager(new Manager(
                managerDto.firstName(),
                managerDto.lastName(),
                managerDto.email(),
                managerDto.dob(),
                passwordEncoder.encode(managerDto.password()),
                agency
        ));
        return ResponseEntity.created(URI.create("/api/agencies/" +
                agency.getId() + "/managers/" + createdManager.getId())).body(createdManager);
    }

    @PutMapping("/{agency-id}/managers/{manager-id}")
    public ResponseEntity<Void> updateManager(@PathVariable("agency-id") String agencyId,
                                              @PathVariable("manager-id") String managerId,
                                              @Valid @RequestBody ManagerDto managerDto,
                                              @AuthenticationPrincipal UserDetails userDetails)
            throws UserAlreadyExistsException {
        Agency agency = agencyService.getAgencyByManagerEmail(userDetails.getUsername());
        if (!agency.getId().equals(agencyId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Manager manager = agencyService.getManager(agency, managerId);
        agencyService.updateManager(managerDto, manager.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{agency-id}/managers")
    public ResponseEntity<List<Manager>> getManagers(@PathVariable("agency-id") String agencyId,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        Agency agency = agencyService.getAgencyByManagerEmail(userDetails.getUsername());
        if (!agency.getId().equals(agencyId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(agencyService.getManagers(agency.getId(), userDetails.getUsername()));
    }

    @GetMapping("/{agency-id}/managers/{manager-id}")
    public ResponseEntity<Manager> getManager(@PathVariable("agency-id") String agencyId,
                                              @PathVariable("manager-id") String managerId,
                                              @AuthenticationPrincipal UserDetails userDetails)
            throws UserAlreadyExistsException {
        Agency agency = agencyService.getAgencyByManagerEmail(userDetails.getUsername());
        if (!agency.getId().equals(agencyId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Manager manager = agencyService.getManager(agency, managerId);
        return ResponseEntity.ok(manager);
    }

    @DeleteMapping("/{agency-id}/managers/{manager-id}")
    public ResponseEntity<Void> deleteManager(@PathVariable("agency-id") String agencyId,
                                              @PathVariable("manager-id") String managerId,
                                              @AuthenticationPrincipal UserDetails userDetails)
            throws UserAlreadyExistsException {
        Agency agency = agencyService.getAgencyByManagerEmail(userDetails.getUsername());
        if (!agency.getId().equals(agencyId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Manager manager = agencyService.getManager(agency, managerId);
        agencyService.deleteManager(manager);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(AgencyAlreadyExistsException.class)
    public ResponseEntity<String> handleAgencyAlreadyExistsException(AgencyAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
