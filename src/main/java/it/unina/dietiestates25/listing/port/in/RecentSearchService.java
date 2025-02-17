package it.unina.dietiestates25.listing.port.in;

import it.unina.dietiestates25.listing.port.out.SearchRepository;
import it.unina.dietiestates25.model.ListingType;
import it.unina.dietiestates25.model.Search;
import it.unina.dietiestates25.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecentSearchService {
    private final SearchRepository searchRepository;

    public RecentSearchService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public void saveSearch(Search search) {
        searchRepository.save(search);
    }

    public List<User> getSearchers(ListingType listingType,
                                   String city,
                                   Class<? extends Search> searchClass) {
        return searchRepository.findUsersByListingTypeAndCityAndTypeSearch(listingType, city, searchClass);
    }

}
