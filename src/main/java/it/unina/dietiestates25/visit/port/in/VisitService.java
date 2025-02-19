package it.unina.dietiestates25.visit.port.in;

import it.unina.dietiestates25.auth.port.in.UserService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.listing.port.in.ListingService;
import it.unina.dietiestates25.model.Listing;
import it.unina.dietiestates25.model.User;
import it.unina.dietiestates25.model.VisitRequest;
import it.unina.dietiestates25.visit.infrastructure.adapter.in.dto.VisitRequestDto;
import it.unina.dietiestates25.visit.port.out.VisitRequestRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class VisitService {
    private final UserService userService;
    private final ListingService listingService;
    private final VisitRequestRepository visitRequestRepository;

    public VisitService(UserService userService,
                        ListingService listingService,
                        VisitRequestRepository visitRequestRepository) {
        this.userService = userService;
        this.listingService = listingService;
        this.visitRequestRepository = visitRequestRepository;
    }


    public VisitRequest createVisitRequest(@Valid VisitRequestDto visitRequestDto,
                                           UserDetails userDetails)
            throws EntityNotExistsException {
        User user = userService.getUser(userDetails.getUsername());
        Listing listing = listingService.getListing(visitRequestDto.listingId());
        return visitRequestRepository.save(new VisitRequest(user, listing, visitRequestDto.timeSlots()));
    }
}
