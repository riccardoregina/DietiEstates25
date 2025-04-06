package it.unina.dietiestates25.image.infrastructure.adapter.in;

import it.unina.dietiestates25.auth.port.in.UserService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.exception.ForbiddenException;
import it.unina.dietiestates25.listing.port.in.ListingService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class RestImageController {
    private final ListingService listingService;
    private final UserService userService;

    public RestImageController(ListingService listingService,
                               UserService userService) {
        this.listingService = listingService;
        this.userService = userService;
    }

    @PostMapping(value = "/listings/{listing-id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<List<String>> addListingImages(@RequestParam List<MultipartFile> images,
                                             @PathVariable("listing-id") String listingId,
                                             @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {

        List<String> imagesUrls = listingService.addListingImages(images, listingId, userDetails.getUsername());
        return ResponseEntity.ok(imagesUrls);
    }

    @DeleteMapping("/listings/{listing-id}")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<Void> removeListingImages(@RequestBody List<String> paths,
                                                         @PathVariable("listing-id") String listingId,
                                                         @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {

        listingService.removeListingImages(paths, listingId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/users/{user-id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> setProfilePic(@RequestParam MultipartFile file,
                                                @PathVariable("user-id") String userId,
                                                @AuthenticationPrincipal UserDetails userDetails)
            throws ForbiddenException, EntityNotExistsException {
        String imageUrl = userService.setProfilePic(file, userId, userDetails.getUsername());
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(imageUrl);
    }

    @DeleteMapping("/users/{user-id}")
    public ResponseEntity<String> removeProfilePic(@PathVariable("user-id") String userId,
                                                @AuthenticationPrincipal UserDetails userDetails)
            throws ForbiddenException, EntityNotExistsException {
        userService.removeProfilePic(userId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
