package it.unina.dietiestates25.listing.port.out;

import it.unina.dietiestates25.listing.model.ListingType;
import it.unina.dietiestates25.listing.model.search.Search;
import it.unina.dietiestates25.auth.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search, String> {
    @Query("SELECT s.user FROM Search s WHERE (TYPE(s) = :searchClass) AND " +
            "(s.listingType = :listingType) AND " +
            "(s.city ILIKE :city) AND " +
            "(s.user.notificationSettings.recommendedListings = TRUE)")
    List<User> findNotifiableUsersByListingTypeAndCityAndTypeSearch(ListingType listingType,
                                                          String city,
                                                          Class<? extends Search> searchClass);

    @Query("SELECT s FROM Search s WHERE ((:searchClass = Search) OR (TYPE(s) = :searchClass)) AND " +
            "(s.user = :user)")
    Page<Search> findAllByUserAndSearchType(User user, Class<? extends Search> searchClass, Pageable pageable);
}
