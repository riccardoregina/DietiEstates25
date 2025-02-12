package it.unina.dietiestates25.listing.port.out;

import it.unina.dietiestates25.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository<T extends Listing> extends JpaRepository<T, String> {
}
