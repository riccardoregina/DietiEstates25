package it.unina.dietiestates25.manager.infrastructure.adapter.in;

import it.unina.dietiestates25.manager.infrastructure.adapter.in.dto.ManagerDto;
import it.unina.dietiestates25.manager.port.in.ManagerService;
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
@RequestMapping(path = "/api/managers")
public class RestManagerController {

    private final ManagerService managerService;
    private final PasswordEncoder passwordEncoder;

    public RestManagerController(ManagerService managerService, PasswordEncoder passwordEncoder) {
        this.managerService = managerService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<Manager> createManager(@Valid @RequestBody ManagerDto managerDto,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        Agency agency = managerService.getAgencyByManagerEmail(userDetails.getUsername());
        Manager createdManager = managerService.createManager(new Manager(
                managerDto.firstName(),
                managerDto.lastName(),
                managerDto.email(),
                managerDto.dob(),
                passwordEncoder.encode(managerDto.password()),
                agency
        ));
        return ResponseEntity.created(URI.create("/api/managers/" + createdManager.getId())).body(createdManager);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateManager(@PathVariable String id,
                                              @Valid @RequestBody ManagerDto managerDto,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        Agency agency = managerService.getAgencyByManagerEmail(userDetails.getUsername());
        Manager manager = managerService.getManager(agency, id);
        managerService.updateManager(managerDto, manager.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Manager>> getManagers(@AuthenticationPrincipal UserDetails userDetails) {
        Agency agency = managerService.getAgencyByManagerEmail(userDetails.getUsername());
        return ResponseEntity.ok(managerService.getManagers(agency.getId(), userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manager> getManager(@PathVariable String id,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        Agency agency = managerService.getAgencyByManagerEmail(userDetails.getUsername());
        Manager manager = managerService.getManager(agency, id);
        return ResponseEntity.ok(manager);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable String id,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        Agency agency = managerService.getAgencyByManagerEmail(userDetails.getUsername());
        Manager manager = managerService.getManager(agency, id);
        managerService.deleteManager(manager);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
