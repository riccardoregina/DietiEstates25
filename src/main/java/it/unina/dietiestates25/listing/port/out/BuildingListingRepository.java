package it.unina.dietiestates25.listing.port.out;

import it.unina.dietiestates25.listing.model.listing.BuildingListing;
import it.unina.dietiestates25.listing.model.ListingType;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BuildingListingRepository extends ListingRepository<BuildingListing> {
    @Query("SELECT l FROM BuildingListing l WHERE " +
            "(l.listingType = :listingType) " +
            "AND (:region IS NULL OR l.location.region ILIKE :region) " +
            "AND (:city IS NULL OR l.location.city ILIKE :city) " +
            "AND (:priceMin IS NULL OR l.price >= :priceMin) " +
            "AND (:priceMax IS NULL OR l.price <= :priceMax) " +
            "AND (:squareMetersMin IS NULL OR l.squareMeters >= :squareMetersMin) " +
            "AND (:squareMetersMax IS NULL OR l.squareMeters <= :squareMetersMax) " +
            "AND (:agentId IS NULL OR l.agent.id = :agentId) ")
    Page<BuildingListing> findAllByFilters(
            @Param("listingType") ListingType listingType,
            @Param("region") String region,
            @Param("city") String city,
            @Param("priceMin") Integer priceMin,
            @Param("priceMax") Integer priceMax,
            @Param("squareMetersMin") Integer squareMetersMin,
            @Param("squareMetersMax") Integer squareMetersMax,
            @Param("agentId") String agentId,
            Pageable pageable
    );

    @Query("SELECT l FROM BuildingListing l WHERE " +
            "(l.listingType = :listingType) " +
            "AND (ST_Distance(l.location.coordinates, :center) < :radius)" +
            "AND (:priceMin IS NULL OR l.price >= :priceMin) " +
            "AND (:priceMax IS NULL OR l.price <= :priceMax) " +
            "AND (:squareMetersMin IS NULL OR l.squareMeters >= :squareMetersMin) " +
            "AND (:squareMetersMax IS NULL OR l.squareMeters <= :squareMetersMax) " +
            "AND (:agentId IS NULL OR l.agent.id = :agentId) ")
    Page<BuildingListing> findAllByFiltersAndLocation(
            @Param("listingType") ListingType listingType,
            @Param("priceMin") Integer priceMin,
            @Param("priceMax") Integer priceMax,
            @Param("squareMetersMin") Integer squareMetersMin,
            @Param("squareMetersMax") Integer squareMetersMax,
            @Param("agentId") String agentId,
            @Param("center") Point center,
            @Param("radius") Integer radiusInMeters,
            Pageable pageable
    );
}
