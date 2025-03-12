package it.unina.dietiestates25.listing.infrastructure.adapter.in;

import it.unina.dietiestates25.auth.model.User;
import it.unina.dietiestates25.auth.port.in.UserService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.exception.ForbiddenException;
import it.unina.dietiestates25.listing.model.search.*;
import it.unina.dietiestates25.listing.port.in.RecentSearchService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recent-searches")
public class RestRecentSearchController {

    private final RecentSearchService recentSearchService;
    private final UserService userService;


    public RestRecentSearchController(RecentSearchService recentSearchService, UserService userService) {
        this.recentSearchService = recentSearchService;
        this.userService = userService;
    }

    @GetMapping("/{user_id}")
    public List<Search> getRecentSearches(@PathVariable("user_id") String userId,
                                          @RequestParam(
                                                  required = false,
                                                  defaultValue = "") String searchType,
                                          @AuthenticationPrincipal UserDetails userDetails)
            throws EntityNotExistsException, ForbiddenException {
        User user = userService.getUser(userDetails.getUsername());
        if (!user.getId().equals(userId)) {
            throw new ForbiddenException("Users can only read their own recent searches");
        }

        Class<? extends Search> searchClass = switch (searchType.toLowerCase()) {
            case "house" -> HouseSearch.class;
            case "garage" -> GarageSearch.class;
            case "land" -> LandSearch.class;
            case "building" -> BuildingSearch.class;
            default -> Search.class;
        };

        return recentSearchService.getRecentSearches(user, searchClass);
    }
}
