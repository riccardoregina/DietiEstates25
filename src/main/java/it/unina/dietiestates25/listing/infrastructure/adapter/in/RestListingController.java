package it.unina.dietiestates25.listing.infrastructure.adapter.in;

import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.BuildingListingDto;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.GarageListingDto;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.HouseListingDto;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.LandListingDto;
import it.unina.dietiestates25.listing.port.in.ListingService;
import it.unina.dietiestates25.model.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = "/api/listings")
public class RestListingController {
    private final ListingService listingService;

    public RestListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @PostMapping("/houses")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<HouseListing> createHouseListing(@Valid @RequestBody HouseListingDto houseListingDto,
                                                 @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException {
        HouseListing houseListing = listingService.createHouseListing(houseListingDto, userDetails.getUsername());
        return ResponseEntity.created(URI.create("/api/listings/houses" + houseListing.getId())).body(houseListing);
    }

    @PostMapping("/garages")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<GarageListing> createGarageListing(@Valid @RequestBody GarageListingDto garageListingDto,
                                                                     @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException {
        GarageListing garageListing = listingService.createGarageListing(garageListingDto, userDetails.getUsername());
        return ResponseEntity.created(URI.create("/api/listings/garages" + garageListing.getId())).body(garageListing);
    }

    @PostMapping("/lands")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<LandListing> createLandListing(@Valid @RequestBody LandListingDto landListingDto,
                                                         @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException {
        LandListing landListing = listingService.createLandListing(landListingDto, userDetails.getUsername());
        return ResponseEntity.created(URI.create("/api/listings/lands" + landListing.getId())).body(landListing);
    }

    @PostMapping("/buildings")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<BuildingListing> createBuildingListing(@Valid @RequestBody BuildingListingDto buildingListingDto,
                                                               @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException {
        BuildingListing buildingListing = listingService.createBuildingListing(buildingListingDto, userDetails.getUsername());
        return ResponseEntity.created(URI.create("/api/listings/buildings" + buildingListing.getId())).body(buildingListing);
    }
}
