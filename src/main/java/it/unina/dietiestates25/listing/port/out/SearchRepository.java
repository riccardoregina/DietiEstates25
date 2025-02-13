package it.unina.dietiestates25.listing.port.out;

import it.unina.dietiestates25.model.Search;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<Search, String> {
}
