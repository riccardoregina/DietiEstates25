package it.unina.dietiestates25.agentreview.infrastructure.adapter.in;

import it.unina.dietiestates25.agentreview.infrastructure.adapter.in.dto.AgentReviewDto;
import it.unina.dietiestates25.agentreview.port.in.AgentReviewService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.agentreview.model.AgentReview;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/agent-review")
public class RestAgentReviewController {
    private final AgentReviewService agentReviewService;

    public RestAgentReviewController(AgentReviewService agentReviewService) {
        this.agentReviewService = agentReviewService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<AgentReview> createAgentReview(@Valid @RequestBody AgentReviewDto agentReviewDto,
                                                         @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException {
        AgentReview agentReview = agentReviewService.createAgentReview(agentReviewDto, userDetails);
        return ResponseEntity.created(URI.create("/api/agent-review/" + agentReview.getId())).body(agentReview);
    }

    @GetMapping("/{agent-id}")
    public ResponseEntity<List<AgentReview>> getAgentReviews(@PathVariable("agent-id") String agentId)
            throws EntityNotExistsException {
        List<AgentReview> reviews = agentReviewService.getAgentReviews(agentId);
        return ResponseEntity.ok().body(reviews);
    }
}