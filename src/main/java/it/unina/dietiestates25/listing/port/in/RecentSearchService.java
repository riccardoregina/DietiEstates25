package it.unina.dietiestates25.listing.port.in;

import it.unina.dietiestates25.listing.port.out.SearchRepository;
import it.unina.dietiestates25.model.Search;
import org.springframework.stereotype.Service;

@Service
public class RecentSearchService {
    private final SearchRepository searchRepository;

    public RecentSearchService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public void saveSearch(Search search) {
        searchRepository.save(search);
    }
}
