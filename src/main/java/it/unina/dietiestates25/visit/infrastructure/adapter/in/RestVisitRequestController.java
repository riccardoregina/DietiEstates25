package it.unina.dietiestates25.visit.infrastructure.adapter.in;

import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.exception.ForbiddenException;
import it.unina.dietiestates25.model.Visit;
import it.unina.dietiestates25.model.VisitRequest;
import it.unina.dietiestates25.visit.infrastructure.adapter.in.dto.VisitRequestDto;
import it.unina.dietiestates25.visit.port.in.VisitService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/visit-requests")
public class RestVisitRequestController {
    private final VisitService visitService;

    public RestVisitRequestController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<VisitRequest> createVisitRequest(
            @Valid @RequestBody VisitRequestDto visitRequestDto,
            @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException {
        VisitRequest visitRequest = visitService.createVisitRequest(visitRequestDto, userDetails);
        return ResponseEntity.created(URI.create("/api/visit-request/" + visitRequest.getId())).body(visitRequest);
    }

    @PostMapping("/{visit-request-id}/accept")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<Visit> acceptVisitRequest(
            @PathVariable("visit-request-id") String visitRequestId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            @AuthenticationPrincipal UserDetails userDetails)
            throws ForbiddenException, EntityNotExistsException {
        Visit visit = visitService.acceptVisitRequest(visitRequestId, userDetails, dateTime);
        return ResponseEntity.ok(visit);
    }

    @PostMapping("/{visit-request-id}/reject")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<Void> rejectVisitRequest(
            @PathVariable("visit-request-id") String visitRequestId,
            @NotBlank @RequestBody String agentMsg,
            @AuthenticationPrincipal UserDetails userDetails)
            throws ForbiddenException, EntityNotExistsException {
        visitService.rejectVisitRequest(visitRequestId, agentMsg, userDetails);
        return ResponseEntity.ok().build();
    }
}
