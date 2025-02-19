package it.unina.dietiestates25.visit.infrastructure.adapter.in;

import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.model.VisitRequest;
import it.unina.dietiestates25.visit.infrastructure.adapter.in.dto.VisitRequestDto;
import it.unina.dietiestates25.visit.port.in.VisitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/visit-requests")
public class RestVisitRequestController {
    private final VisitService visitService;

    public RestVisitRequestController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping
    public ResponseEntity<VisitRequest> createVisitRequest(@Valid @RequestBody VisitRequestDto visitRequestDto,
                                                           @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException {
        VisitRequest visitRequest = visitService.createVisitRequest(visitRequestDto, userDetails);
        return ResponseEntity.created(URI.create("/api/visit-request/" + visitRequest.getId())).body(visitRequest);
    }
}
