package it.unina.dietiestates25.agency.infrastructure.adapter.in;

import it.unina.dietiestates25.agency.infrastructure.adapter.in.dto.AgentDto;
import it.unina.dietiestates25.agency.infrastructure.adapter.in.dto.SignUpAgencyResponse;
import it.unina.dietiestates25.agency.infrastructure.adapter.in.dto.UserDto;
import it.unina.dietiestates25.agency.infrastructure.adapter.in.dto.SignUpAgencyRequest;
import it.unina.dietiestates25.agency.model.Admin;
import it.unina.dietiestates25.agency.model.Agency;
import it.unina.dietiestates25.agency.model.Agent;
import it.unina.dietiestates25.agency.model.Manager;
import it.unina.dietiestates25.agency.port.in.AgencyService;
import it.unina.dietiestates25.auth.model.User;
import it.unina.dietiestates25.auth.port.in.UserService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.exception.ForbiddenException;
import it.unina.dietiestates25.exception.EntityAlreadyExistsException;
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

    private static final String FORBIDDEN_EXCEPTION_MSG_AGENCY = "User's agency does not match with provided one";
    private static final String FORBIDDEN_EXCEPTION_MSG_AGENT = "Agent can only modify itself";
    private static final String FORBIDDEN_EXCEPTION_MSG_MANAGER = "Manager can only modify his agents";
    private static final String PATH_AGENCIES = "/api/agencies/";
    private final AgencyService agencyService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RestAgencyController(AgencyService agencyService,
                                UserService userService,
                                PasswordEncoder passwordEncoder) {
        this.agencyService = agencyService;
        this.userService = userService;
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
                ),
                requestDto.profilePicUrl()
        ));
        return ResponseEntity.created(URI.create(PATH_AGENCIES +
                admin.getAgency().getId() + "/managers/" + admin.getId()))
                .body(new SignUpAgencyResponse(admin, admin.getAgency()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AGENT')")
    public ResponseEntity<Agency> getAgency(@RequestParam String userId,
                                            @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        User user = userService.getUser(userDetails.getUsername());
        if (!user.getId().equals(userId)) {
            throw new ForbiddenException("User can only access his agency");
        }
        Agency agency = (UserService.hasRole(userDetails, "ROLE_AGENT")) ?
                agencyService.getAgencyByAgentEmail(userDetails.getUsername()) :
                agencyService.getAgencyByManagerEmail(userDetails.getUsername());

        return ResponseEntity.ok(agency);
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
                agency,
                userDto.profilePicUrl()
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
    public ResponseEntity<Agent> createAgent(@Valid @RequestBody AgentDto agentDto,
                                             @PathVariable("agency-id") String agencyId,
                                             @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException, EntityAlreadyExistsException {
        Agency agency = agencyService.getAgencyByManagerEmail(userDetails.getUsername());
        validateAgency(agencyId, agency, FORBIDDEN_EXCEPTION_MSG_AGENCY);
        Manager manager = agencyService.getManagerByEmail(userDetails.getUsername());
        Agent createdAgent = agencyService.createAgent(new Agent(
                agentDto.firstName(),
                agentDto.lastName(),
                agentDto.email(),
                agentDto.dob(),
                passwordEncoder.encode(agentDto.password()),
                agency,
                manager,
                agentDto.profilePicUrl(),
                agentDto.bio()));
        return ResponseEntity.created(URI.create(PATH_AGENCIES +
                agency.getId() + "/agents/" + createdAgent.getId())).body(createdAgent);
    }

    @PutMapping("/{agency-id}/agents/{agent-id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AGENT')")
    public ResponseEntity<Void> updateAgent(@Valid @RequestBody AgentDto agentDto,
                                            @PathVariable("agency-id") String agencyId,
                                            @PathVariable("agent-id") String agentId,
                                            @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        validateUserAccess(agencyId, agentId, userDetails);
        agencyService.updateAgent(agentDto, agentId);
        return ResponseEntity.noContent().build();
    }

    private void validateUserAccess(String agencyId, String agentId, UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        if (UserService.hasRole(userDetails, "ROLE_AGENT")) {
            Agency agency = agencyService.getAgencyByAgentEmail(userDetails.getUsername());
            validateAgency(agencyId, agency, FORBIDDEN_EXCEPTION_MSG_AGENCY);
            Agent agentModifier = agencyService.getAgentByEmail(userDetails.getUsername());
            if (!agentModifier.getId().equals(agentId)) {
                throw new ForbiddenException(FORBIDDEN_EXCEPTION_MSG_AGENT);
            }
        } else if (UserService.hasRole(userDetails, "ROLE_ADMIN")) {
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
