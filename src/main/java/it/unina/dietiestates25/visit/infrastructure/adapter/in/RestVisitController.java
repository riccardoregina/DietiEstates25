package it.unina.dietiestates25.visit.infrastructure.adapter.in;

import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.exception.ForbiddenException;
import it.unina.dietiestates25.visit.model.Visit;
import it.unina.dietiestates25.visit.port.in.VisitService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
public class RestVisitController {
    private final VisitService visitService;

    public RestVisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<List<Visit>> getVisits(@RequestParam String agentId,
                                                 @RequestParam int year,
                                                 @RequestParam int month,
                                                 @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        List<Visit> visits = visitService.getVisits(agentId, year, month, userDetails);
        return ResponseEntity.ok().body(visits);
    }
}
