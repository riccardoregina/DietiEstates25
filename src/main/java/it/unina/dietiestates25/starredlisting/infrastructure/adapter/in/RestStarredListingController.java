package it.unina.dietiestates25.starredlisting.infrastructure.adapter.in;

import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.exception.ForbiddenException;
import it.unina.dietiestates25.listing.model.listing.Listing;
import it.unina.dietiestates25.starredlisting.port.in.StarredListingService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/starred-listings")
public class RestStarredListingController {

    private final StarredListingService starredListingService;

    public RestStarredListingController(StarredListingService starredListingService) {
        this.starredListingService = starredListingService;
    }

    @PostMapping
    public ResponseEntity<Void> addStarredListing(@NotBlank @RequestBody String listingId,
                                                  @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException {
        starredListingService.addStarredListing(userDetails.getUsername(), listingId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeStarredListing(@RequestParam String userId,
                                                     @RequestParam String listingId,
                                                     @AuthenticationPrincipal UserDetails userDetails)
            throws ForbiddenException, EntityNotExistsException {
        starredListingService.removeStarredListing(userDetails.getUsername(), listingId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Listing>> getStarredListings(@RequestParam String userId,
                                                            @AuthenticationPrincipal UserDetails userDetails)
            throws ForbiddenException, EntityNotExistsException {
        List<Listing> starredListings = starredListingService.getStarredListings(userId, userDetails.getUsername());
        return ResponseEntity.ok().body(starredListings);
    }
}