package it.unina.dietiestates25.agency.infrastructure.adapter.in;

import it.unina.dietiestates25.agency.infrastructure.adapter.in.dto.SignUpAgencyResponse;
import it.unina.dietiestates25.agency.infrastructure.adapter.in.dto.UserDto;
import it.unina.dietiestates25.agency.infrastructure.adapter.in.dto.SignUpAgencyRequest;
import it.unina.dietiestates25.agency.port.in.AgencyService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.exception.ForbiddenException;
import it.unina.dietiestates25.exception.EntityAlreadyExistsException;
import it.unina.dietiestates25.model.Admin;
import it.unina.dietiestates25.model.Agency;
import it.unina.dietiestates25.model.Agent;
import it.unina.dietiestates25.model.Manager;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api/agencies")
public class RestAgencyController {

    public static final String FORBIDDEN_EXCEPTION_MSG_AGENCY = "User's agency does not match with provided one";
    public static final String FORBIDDEN_EXCEPTION_MSG_AGENT = "Agent can only modify himself";
    public static final String FORBIDDEN_EXCEPTION_MSG_MANAGER = "Manager can only modify his agents";
    public static final String PATH_AGENCIES = "/api/agencies/";
    private final AgencyService agencyService;
    private final PasswordEncoder passwordEncoder;

    public RestAgencyController(AgencyService agencyService, PasswordEncoder passwordEncoder) {
        this.agencyService = agencyService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<SignUpAgencyResponse> signUpAgency(@Valid @RequestBody SignUpAgencyRequest requestDto)
            throws EntityAlreadyExistsException {
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
        return ResponseEntity.created(URI.create(PATH_AGENCIES +
                admin.getAgency().getId() + "/managers/" + admin.getId()))
                .body(new SignUpAgencyResponse(admin, admin.getAgency()));
    }

    @PostMapping("/{agency-id}/managers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Manager> createManager(@Valid @RequestBody UserDto userDto,
                                                 @PathVariable("agency-id") String agencyId,
                                                 @AuthenticationPrincipal UserDetails userDetails)
            throws ForbiddenException, EntityAlreadyExistsException, EntityNotExistsException {
        Agency agency = agencyService.getAgencyByManagerEmail(userDetails.getUsername());
        validateAgency(agencyId, agency, FORBIDDEN_EXCEPTION_MSG_AGENCY);
        Manager createdManager = agencyService.createManager(new Manager(
                userDto.firstName(),
                userDto.lastName(),
                userDto.email(),
                userDto.dob(),
                passwordEncoder.encode(userDto.password()),
                agency
        ));
        return ResponseEntity.created(URI.create(PATH_AGENCIES +
                agency.getId() + "/managers/" + createdManager.getId())).body(createdManager);
    }

    private void validateAgency(String agencyId, Agency agency, String errorMsg) throws ForbiddenException {
        if (!agency.getId().equals(agencyId)) {
            throw new ForbiddenException(errorMsg);
        }
    }

    @PutMapping("/{agency-id}/managers/{manager-id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> updateManager(@PathVariable("agency-id") String agencyId,
                                              @PathVariable("manager-id") String managerId,
                                              @Valid @RequestBody UserDto userDto,
                                              @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        Agency agency = agencyService.getAgencyByManagerEmail(userDetails.getUsername());
        validateAgency(agencyId, agency, FORBIDDEN_EXCEPTION_MSG_AGENCY);
        Manager manager = agencyService.getManagerById(managerId);
        agencyService.updateManager(userDto, manager.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{agency-id}/managers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Manager>> getManagers(@PathVariable("agency-id") String agencyId,
                                                     @AuthenticationPrincipal UserDetails userDetails)
            throws ForbiddenException, EntityNotExistsException {
        Agency agency = agencyService.getAgencyByManagerEmail(userDetails.getUsername());
        validateAgency(agencyId, agency, FORBIDDEN_EXCEPTION_MSG_AGENCY);
        return ResponseEntity.ok(agencyService.getManagers(agency.getId(), userDetails.getUsername()));
    }

    @GetMapping("/{agency-id}/managers/{manager-id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Manager> getManager(@PathVariable("agency-id") String agencyId,
                                              @PathVariable("manager-id") String managerId,
                                              @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        Agency agency = agencyService.getAgencyByManagerEmail(userDetails.getUsername());
        validateAgency(agencyId, agency, FORBIDDEN_EXCEPTION_MSG_AGENCY);
        Manager manager = agencyService.getManagerById(managerId);
        return ResponseEntity.ok(manager);
    }

    @DeleteMapping("/{agency-id}/managers/{manager-id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteManager(@PathVariable("agency-id") String agencyId,
                                              @PathVariable("manager-id") String managerId,
                                              @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        Agency agency = agencyService.getAgencyByManagerEmail(userDetails.getUsername());
        validateAgency(agencyId, agency, FORBIDDEN_EXCEPTION_MSG_AGENCY);
        Manager manager = agencyService.getManagerById(managerId);
        agencyService.deleteManager(manager);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{agency-id}/agents")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Agent> createAgent(@Valid @RequestBody UserDto userDto,
                                             @PathVariable("agency-id") String agencyId,
                                             @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException, EntityAlreadyExistsException {
        Agency agency = agencyService.getAgencyByManagerEmail(userDetails.getUsername());
        validateAgency(agencyId, agency, FORBIDDEN_EXCEPTION_MSG_AGENCY);
        Manager manager = agencyService.getManagerByEmail(userDetails.getUsername());
        Agent createdAgent = agencyService.createAgent(new Agent(
                userDto.firstName(),
                userDto.lastName(),
                userDto.email(),
                userDto.dob(),
                passwordEncoder.encode(userDto.password()),
                agency,
                manager
        ));
        return ResponseEntity.created(URI.create(PATH_AGENCIES +
                agency.getId() + "/agents/" + createdAgent.getId())).body(createdAgent);
    }

    @PutMapping("/{agency-id}/agents/{agent-id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AGENT')")
    public ResponseEntity<Void> updateAgent(@Valid @RequestBody UserDto userDto,
                                            @PathVariable("agency-id") String agencyId,
                                            @PathVariable("agent-id") String agentId,
                                            @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        validateUserAccess(agencyId, agentId, userDetails);
        agencyService.updateAgent(userDto, agentId);
        return ResponseEntity.noContent().build();
    }

    private void validateUserAccess(String agencyId, String agentId, UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        if (hasRole(userDetails, "AGENT")) {
            Agency agency = agencyService.getAgencyByAgentEmail(userDetails.getUsername());
            validateAgency(agencyId, agency, FORBIDDEN_EXCEPTION_MSG_AGENCY);
            Agent agentModifier = agencyService.getAgentByEmail(userDetails.getUsername());
            if (!agentModifier.getId().equals(agentId)) {
                throw new ForbiddenException(FORBIDDEN_EXCEPTION_MSG_AGENT);
            }
        } else if (hasRole(userDetails, "ADMIN")) {
            Agency agency = agencyService.getAgencyByManagerEmail(userDetails.getUsername());
            validateAgency(agencyId, agency, FORBIDDEN_EXCEPTION_MSG_AGENCY);
        } else {
            Agency agency = agencyService.getAgencyByManagerEmail(userDetails.getUsername());
            validateAgency(agencyId, agency, FORBIDDEN_EXCEPTION_MSG_AGENCY);
            Manager managerModifier = agencyService.getManagerByEmail(userDetails.getUsername());
            Agent agent = agencyService.getAgentById(agentId);
            if (!agent.getManager().equals(managerModifier)) {
                throw new ForbiddenException(FORBIDDEN_EXCEPTION_MSG_MANAGER);
            }
        }
    }

    private boolean hasRole(UserDetails userDetails, String role) {
        return userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(role));
    }

    @GetMapping("/{agency-id}/agents")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<Agent>> getAgents(@PathVariable("agency-id") String agencyId,
                                                 @RequestParam(required = false) String managerId,
                                                 @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        Agency agency = agencyService.getAgencyByManagerEmail(userDetails.getUsername());
        validateAgency(agencyId, agency, FORBIDDEN_EXCEPTION_MSG_AGENCY);
        List<Agent> agents = agencyService.getAgents(agency);
        return ResponseEntity.ok().body(
                agents
                .stream()
                .filter(agent -> managerId == null || agent.getManager().getId().equals(managerId))
                .toList());
    }

    @GetMapping("/{agency-id}/agents/{agent-id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AGENT')")
    public ResponseEntity<Agent> getAgent(@PathVariable("agency-id") String agencyId,
                                          @PathVariable("agent-id") String agentId,
                                          @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        Agency agency = agencyService.getAgencyByManagerEmail(userDetails.getUsername());
        validateAgency(agencyId, agency, FORBIDDEN_EXCEPTION_MSG_AGENCY);
        return ResponseEntity.ok().body(agencyService.getAgentById(agentId));
    }

    @DeleteMapping("/{agency-id}/agents/{agent-id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteAgent(@PathVariable("agency-id") String agencyId,
                                            @PathVariable("agent-id") String agentId,
                                            @AuthenticationPrincipal UserDetails userDetails)
            throws ForbiddenException, EntityNotExistsException {
        validateUserAccess(agencyId, agentId, userDetails);
        Agent agent = agencyService.getAgentById(agentId);
        agencyService.deleteAgent(agent);
        return ResponseEntity.noContent().build();
    }
}
