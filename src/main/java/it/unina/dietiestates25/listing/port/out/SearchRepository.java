package it.unina.dietiestates25.listing.port.out;

import it.unina.dietiestates25.model.ListingType;
import it.unina.dietiestates25.model.Search;
import it.unina.dietiestates25.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search, String> {
    @Query("SELECT s.user FROM Search s WHERE (TYPE(s) = :searchClass) AND" + /* (TYPE(s) = :searchClass) AND*/
            "(s.listingType = :listingType) AND " +
            "(s.city ILIKE :city)")
    List<User> findUsersByListingTypeAndCityAndTypeSearch(ListingType listingType,
                                                          String city,
                                                          Class<? extends Search> searchClass);
}
