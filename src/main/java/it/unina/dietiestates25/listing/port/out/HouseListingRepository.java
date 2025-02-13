package it.unina.dietiestates25.listing.port.out;

import it.unina.dietiestates25.model.EnergyClass;
import it.unina.dietiestates25.model.HouseListing;
import it.unina.dietiestates25.model.ListingType;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseListingRepository extends ListingRepository<HouseListing> {

    @Query("SELECT l FROM HouseListing l WHERE " +
            "(l.listingType = :listingType) " +
            "AND (:region IS NULL OR l.location.region ILIKE :region) " +
            "AND (:city IS NULL OR l.location.city ILIKE :city) " +
            "AND (:priceMin IS NULL OR l.price >= :priceMin) " +
            "AND (:priceMax IS NULL OR l.price <= :priceMax) " +
            "AND (:squareMetersMin IS NULL OR l.squareMeters >= :squareMetersMin) " +
            "AND (:squareMetersMax IS NULL OR l.squareMeters <= :squareMetersMax) " +
            "AND (:nRoomsMin IS NULL OR l.nRooms >= :nRoomsMin) " +
            "AND (:nRoomsMax IS NULL OR l.nRooms <= :nRoomsMax) " +
            "AND (:nBathroomsMin IS NULL OR l.nBathrooms >= :nBathroomsMin) " +
            "AND (:nBathroomsMax IS NULL OR l.nBathrooms <= :nBathroomsMax) " +
            "AND (:floorMin IS NULL OR l.floor >= :floorMin) " +
            "AND (:floorMax IS NULL OR l.floor <= :floorMax) " +
            "AND (:energyClassMin IS NULL OR l.energyClass >= :energyClassMin)" +
            "AND (:agentId IS NULL OR l.agent.id = :agentId) ")
    List<HouseListing> findAllByFilters(
            @Param("listingType") ListingType listingType,
            @Param("region") String region,
            @Param("city") String city,
            @Param("priceMin") Double priceMin,
            @Param("priceMax") Double priceMax,
            @Param("squareMetersMin") Double squareMetersMin,
            @Param("squareMetersMax") Double squareMetersMax,
            @Param("nRoomsMin") Integer nRoomsMin,
            @Param("nRoomsMax") Integer nRoomsMax,
            @Param("nBathroomsMin") Integer nBathroomsMin,
            @Param("nBathroomsMax") Integer nBathroomsMax,
            @Param("floorMin") Integer floorMin,
            @Param("floorMax") Integer floorMax,
            @Param("energyClassMin") EnergyClass energyClassMin,
            @Param("agentId") String agentId
    );

    @Query("SELECT l FROM HouseListing l WHERE " +
            "(l.listingType = :listingType) " +
            "AND (ST_Distance(l.location.coordinates, :center) < :radius)" +
            "AND (:priceMin IS NULL OR l.price >= :priceMin) " +
            "AND (:priceMax IS NULL OR l.price <= :priceMax) " +
            "AND (:squareMetersMin IS NULL OR l.squareMeters >= :squareMetersMin) " +
            "AND (:squareMetersMax IS NULL OR l.squareMeters <= :squareMetersMax) " +
            "AND (:nRoomsMin IS NULL OR l.nRooms >= :nRoomsMin) " +
            "AND (:nRoomsMax IS NULL OR l.nRooms <= :nRoomsMax) " +
            "AND (:nBathroomsMin IS NULL OR l.nBathrooms >= :nBathroomsMin) " +
            "AND (:nBathroomsMax IS NULL OR l.nBathrooms <= :nBathroomsMax) " +
            "AND (:floorMin IS NULL OR l.floor >= :floorMin) " +
            "AND (:floorMax IS NULL OR l.floor <= :floorMax) " +
            "AND (:energyClassMin IS NULL OR l.energyClass >= :energyClassMin)" +
            "AND (:agentId IS NULL OR l.agent.id = :agentId) ")
    List<HouseListing> findAllByFiltersAndLocation(
            @Param("listingType") ListingType listingType,
            @Param("priceMin") Double priceMin,
            @Param("priceMax") Double priceMax,
            @Param("squareMetersMin") Double squareMetersMin,
            @Param("squareMetersMax") Double squareMetersMax,
            @Param("nRoomsMin") Integer nRoomsMin,
            @Param("nRoomsMax") Integer nRoomsMax,
            @Param("nBathroomsMin") Integer nBathroomsMin,
            @Param("nBathroomsMax") Integer nBathroomsMax,
            @Param("floorMin") Integer floorMin,
            @Param("floorMax") Integer floorMax,
            @Param("energyClassMin") EnergyClass energyClassMin,
            @Param("agentId") String agentId,
            @Param("center") Point center,
            @Param("radius") Integer radiusInMeters
    );
}
