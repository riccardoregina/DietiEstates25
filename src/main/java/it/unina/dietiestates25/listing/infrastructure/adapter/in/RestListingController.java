package it.unina.dietiestates25.listing.infrastructure.adapter.in;

import it.unina.dietiestates25.auth.model.User;
import it.unina.dietiestates25.auth.port.in.UserService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.exception.ForbiddenException;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.BuildingListingDto;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.GarageListingDto;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.HouseListingDto;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.LandListingDto;
import it.unina.dietiestates25.listing.model.EnergyClass;
import it.unina.dietiestates25.listing.model.ListingType;
import it.unina.dietiestates25.listing.model.listing.*;
import it.unina.dietiestates25.listing.model.search.BuildingSearch;
import it.unina.dietiestates25.listing.model.search.GarageSearch;
import it.unina.dietiestates25.listing.model.search.HouseSearch;
import it.unina.dietiestates25.listing.model.search.LandSearch;
import it.unina.dietiestates25.listing.port.in.ListingService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api/listings")
public class RestListingController {
    private final ListingService listingService;
    private final UserService userService;
    private static final int PAGE_SIZE = 10;
    private static final String DEFAULT_VALUE_OF_SORT_BY = "timestamp";

    public RestListingController(ListingService listingService,
                                 UserService userService) {
        this.listingService = listingService;
        this.userService = userService;
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

    @PutMapping("/houses/{listing-id}")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<Void> updateHouseListing(@Valid @RequestBody HouseListingDto houseListingDto,
                                                   @PathVariable("listing-id") String listingId,
                                                   @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        listingService.updateHouseListing(listingId, houseListingDto, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/garage/{listing-id}")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<Void> updateGarageListing(@Valid @RequestBody GarageListingDto garageListingDto,
                                                   @PathVariable("listing-id") String listingId,
                                                   @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        listingService.updateGarageListing(listingId, garageListingDto, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/lands/{listing-id}")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<Void> updateLandListing(@Valid @RequestBody LandListingDto landListingDto,
                                                   @PathVariable("listing-id") String listingId,
                                                   @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        listingService.updateLandListing(listingId, landListingDto, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/buildings/{listing-id}")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<Void> updateBuildingListing(@Valid @RequestBody BuildingListingDto buildingListingDto,
                                                   @PathVariable("listing-id") String listingId,
                                                   @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        listingService.updateBuildingListing(listingId, buildingListingDto, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping({
            "/houses/{listing-id}",
            "/garages/{listing-id}",
            "/lands/{listing-id}",
            "/buildings/{listing-id}"
    })
    @PreAuthorize("hasAnyRole('AGENT', 'ADMIN')")
    public ResponseEntity<Void> deleteListing(@PathVariable("listing-id") String listingId,
                                              @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {

        if (UserService.hasRole(userDetails, "ROLE_ADMIN")) {
            listingService.deleteListingAsAdmin(listingId, userDetails.getUsername());
        } else {
            listingService.deleteListingAsAgent(listingId, userDetails.getUsername());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping({
            "/houses/{listing-id}",
            "/garages/{listing-id}",
            "/lands/{listing-id}",
            "/buildings/{listing-id}"
    })
    public ResponseEntity<Listing> getListing(@PathVariable("listing-id") String listingId)
            throws EntityNotExistsException {
        return ResponseEntity.ok().body(listingService.getListing(listingId));
    }

    @GetMapping("/houses")
    public ResponseEntity<Page<HouseListing>> getHouseListings(
            @RequestParam(required = false) ListingType listingType,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double centerLongitude,
            @RequestParam(required = false) Double centerLatitude,
            @RequestParam(required = false) Integer radius,
            @RequestParam(required = false) Integer priceMin,
            @RequestParam(required = false) Integer priceMax,
            @RequestParam(required = false) Integer squareMetersMin,
            @RequestParam(required = false) Integer squareMetersMax,
            @RequestParam(required = false) Integer nRoomsMin,
            @RequestParam(required = false) Integer nRoomsMax,
            @RequestParam(required = false) Integer nBathroomsMin,
            @RequestParam(required = false) Integer nBathroomsMax,
            @RequestParam(required = false) Integer floorMin,
            @RequestParam(required = false) Integer floorMax,
            @RequestParam(required = false) EnergyClass energyClassMin,
            @RequestParam(required = false) String agentId,
            @RequestParam(required = false) Boolean elevator,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "timestamp") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String ordering,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws EntityNotExistsException {
        User user = (userDetails == null) ? null : userService.getUser(userDetails.getUsername());
        HouseSearch houseSearch = new HouseSearch(
                user,
                listingType,
                region,
                city,
                (centerLongitude != null && centerLatitude != null) ?
                        Location.createPoint(centerLongitude, centerLatitude) : null,
                radius,
                priceMin,
                priceMax,
                squareMetersMin,
                squareMetersMax,
                nRoomsMin,
                nRoomsMax,
                nBathroomsMin,
                nBathroomsMax,
                floorMin,
                floorMax,
                energyClassMin,
                agentId,
                elevator
        );
        if (!Listing.isValidSortingCriteria(sortBy, HouseListing.class)) {
            sortBy = DEFAULT_VALUE_OF_SORT_BY;
        }
        Sort sort = ordering.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<HouseListing> listings = listingService
                .getHouseListings(houseSearch, PageRequest.of(page, PAGE_SIZE, sort));
        return ResponseEntity.ok().body(listings);
    }

    @GetMapping("/garages")
    public ResponseEntity<Page<GarageListing>> getGarageListings(
            @RequestParam(required = false) ListingType listingType,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double centerLongitude,
            @RequestParam(required = false) Double centerLatitude,
            @RequestParam(required = false) Integer radius,
            @RequestParam(required = false) Integer priceMin,
            @RequestParam(required = false) Integer priceMax,
            @RequestParam(required = false) Integer squareMetersMin,
            @RequestParam(required = false) Integer squareMetersMax,
            @RequestParam(required = false) Integer floorMin,
            @RequestParam(required = false) Integer floorMax,
            @RequestParam(required = false) String agentId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "timestamp") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String ordering,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws EntityNotExistsException {
        User user = (userDetails == null) ? null : userService.getUser(userDetails.getUsername());
        GarageSearch garageSearch = new GarageSearch(
                user,
                listingType,
                region,
                city,
                (centerLongitude != null && centerLatitude != null) ?
                        Location.createPoint(centerLongitude, centerLatitude) : null,
                radius,
                priceMin,
                priceMax,
                squareMetersMin,
                squareMetersMax,
                agentId,
                floorMin,
                floorMax
        );
        if (!Listing.isValidSortingCriteria(sortBy, GarageListing.class)) {
            sortBy = DEFAULT_VALUE_OF_SORT_BY;
        }
        Sort sort = ordering.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<GarageListing> listings = listingService
                .getGarageListings(garageSearch, PageRequest.of(page, PAGE_SIZE, sort));
        return ResponseEntity.ok().body(listings);
    }

    @GetMapping("/lands")
    public ResponseEntity<Page<LandListing>> getLandListings(
            @RequestParam(required = false) ListingType listingType,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double centerLongitude,
            @RequestParam(required = false) Double centerLatitude,
            @RequestParam(required = false) Integer radius,
            @RequestParam(required = false) Integer priceMin,
            @RequestParam(required = false) Integer priceMax,
            @RequestParam(required = false) Integer squareMetersMin,
            @RequestParam(required = false) Integer squareMetersMax,
            @RequestParam(required = false) String agentId,
            @RequestParam(required = false) Boolean building,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "timestamp") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String ordering,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws EntityNotExistsException {
        User user = (userDetails == null) ? null : userService.getUser(userDetails.getUsername());
        LandSearch landSearch = new LandSearch(
                user,
                listingType,
                region,
                city,
                (centerLongitude != null && centerLatitude != null) ?
                        Location.createPoint(centerLongitude, centerLatitude) : null,
                radius,
                priceMin,
                priceMax,
                squareMetersMin,
                squareMetersMax,
                agentId,
                building
        );
        if (!Listing.isValidSortingCriteria(sortBy, LandListing.class)) {
            sortBy = DEFAULT_VALUE_OF_SORT_BY;
        }
        Sort sort = ordering.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<LandListing> listings = listingService
                .getLandListings(landSearch, PageRequest.of(page, PAGE_SIZE, sort));
        return ResponseEntity.ok().body(listings);
    }

    @GetMapping("/buildings")
    public ResponseEntity<Page<BuildingListing>> getBuildingListings(
            @RequestParam(required = false) ListingType listingType,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double centerLongitude,
            @RequestParam(required = false) Double centerLatitude,
            @RequestParam(required = false) Integer radius,
            @RequestParam(required = false) Integer priceMin,
            @RequestParam(required = false) Integer priceMax,
            @RequestParam(required = false) Integer squareMetersMin,
            @RequestParam(required = false) Integer squareMetersMax,
            @RequestParam(required = false) String agentId,
            @RequestParam(required = false) Boolean elevator,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "timestamp") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String ordering,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws EntityNotExistsException {
        User user = (userDetails == null) ? null : userService.getUser(userDetails.getUsername());
        BuildingSearch buildingSearch = new BuildingSearch(
                user,
                listingType,
                region,
                city,
                (centerLongitude != null && centerLatitude != null) ?
                        Location.createPoint(centerLongitude, centerLatitude) : null,
                radius,
                priceMin,
                priceMax,
                squareMetersMin,
                squareMetersMax,
                agentId,
                elevator
        );
        if (!Listing.isValidSortingCriteria(sortBy, BuildingListing.class)) {
            sortBy = DEFAULT_VALUE_OF_SORT_BY;
        }
        Sort sort = ordering.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<BuildingListing> listings = listingService
                .getBuildingListings(buildingSearch, PageRequest.of(page, PAGE_SIZE, sort));
        return ResponseEntity.ok().body(listings);
    }

}
