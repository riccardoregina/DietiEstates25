package it.unina.dietiestates25.starredlisting.port.in;

import it.unina.dietiestates25.auth.port.in.UserService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.exception.ForbiddenException;
import it.unina.dietiestates25.listing.port.in.ListingService;
import it.unina.dietiestates25.listing.model.listing.Listing;
import it.unina.dietiestates25.auth.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StarredListingService {

    private final ListingService listingService;
    private final UserService userService;

    public StarredListingService(ListingService listingService, UserService userService) {
        this.listingService = listingService;
        this.userService = userService;
    }

    @Transactional
    public void addStarredListing(String userEmail, String listingId)
            throws EntityNotExistsException {
        User user = userService.getUser(userEmail);
        Listing listing = listingService.getListing(listingId);
        user.addStarredListing(listing);
    }

    @Transactional
    public void removeStarredListing(String userEmail, String listingId, String userId)
            throws EntityNotExistsException, ForbiddenException {
        User user = userService.getUser(userEmail);
        if (!userId.equals(user.getId())) {
            throw new ForbiddenException("Users can only modify their own starred listings");
        }
        Listing listing = listingService.getListing(listingId);
        user.removeStarredListing(listing);
    }

    public List<Listing> getStarredListings(String userId, String userEmail)
            throws ForbiddenException, EntityNotExistsException {
        User user = userService.getUser(userEmail);
        if (!userId.equals(user.getId())) {
            throw new ForbiddenException("Users can only access their own starred listings");
        }
        return user.getStarredListings().stream().toList();
    }
}
