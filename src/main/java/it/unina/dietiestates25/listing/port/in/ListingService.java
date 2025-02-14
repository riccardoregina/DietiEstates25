package it.unina.dietiestates25.listing.port.in;

import it.unina.dietiestates25.agency.port.in.AgencyService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.exception.ForbiddenException;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.BuildingListingDto;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.GarageListingDto;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.HouseListingDto;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.LandListingDto;
import it.unina.dietiestates25.listing.port.out.*;
import it.unina.dietiestates25.model.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListingService {
    public static final String ENTITY_NOT_EXIST_LISTING_MSG = "Listing does not exist, id: %s";
    private final AgencyService agencyService;
    private final ListingRepository<Listing> listingRepository;
    @Qualifier("houseListingRepository")
    private final HouseListingRepository houseListingRepository;
    @Qualifier("garageListingRepository")
    private final GarageListingRepository garageListingRepository;
    @Qualifier("landListingRepository")
    private final LandListingRepository landListingRepository;
    @Qualifier("buildingListingRepository")
    private final BuildingListingRepository buildingListingRepository;
    private final RecentSearchService recentSearchService;

    public ListingService(AgencyService agencyService,
                          ListingRepository<Listing> listingRepository,
                          @Qualifier("houseListingRepository") HouseListingRepository houseListingRepository,
                          @Qualifier("garageListingRepository") GarageListingRepository garageListingRepository,
                          @Qualifier("landListingRepository") LandListingRepository landListingRepository,
                          @Qualifier("buildingListingRepository") BuildingListingRepository buildingListingRepository,
                          RecentSearchService recentSearchService) {
        this.agencyService = agencyService;
        this.listingRepository = listingRepository;
        this.houseListingRepository = houseListingRepository;
        this.garageListingRepository = garageListingRepository;
        this.landListingRepository = landListingRepository;
        this.buildingListingRepository = buildingListingRepository;
        this.recentSearchService = recentSearchService;
    }

    public HouseListing createHouseListing(HouseListingDto houseListingDto,
                                 String agentEmail)
            throws EntityNotExistsException{
        Agent agent = agencyService.getAgentByEmail(agentEmail);
        return listingRepository.save(new HouseListing(
                agent,
                houseListingDto.title(),
                houseListingDto.price(),
                houseListingDto.description(),
                houseListingDto.squareMeters(),
                houseListingDto.listingType(),
                new Location(houseListingDto.locationDto().region(),
                        houseListingDto.locationDto().city(),
                        houseListingDto.locationDto().address(),
                        Location.createPoint(houseListingDto.locationDto().longitude(),
                                houseListingDto.locationDto().latitude())),
                houseListingDto.nRooms(),
                houseListingDto.nBathrooms(),
                houseListingDto.floor(),
                houseListingDto.energyClass(),
                houseListingDto.otherFeatures()
        ));
    }

    public GarageListing createGarageListing(GarageListingDto garageListingDto,
                                            String agentEmail)
            throws EntityNotExistsException {
        Agent agent = agencyService.getAgentByEmail(agentEmail);
        return listingRepository.save(new GarageListing(
                agent,
                garageListingDto.title(),
                garageListingDto.price(),
                garageListingDto.description(),
                garageListingDto.squareMeters(),
                garageListingDto.listingType(),
                new Location(garageListingDto.locationDto().region(),
                        garageListingDto.locationDto().city(),
                        garageListingDto.locationDto().address(),
                        Location.createPoint(garageListingDto.locationDto().longitude(),
                                garageListingDto.locationDto().latitude())),
                garageListingDto.floor(),
                garageListingDto.otherFeatures()
        ));
    }

    public LandListing createLandListing(LandListingDto landListingDto,
                                         String agentEmail)
            throws EntityNotExistsException {
        Agent agent = agencyService.getAgentByEmail(agentEmail);
        return listingRepository.save(new LandListing(
                agent,
                landListingDto.title(),
                landListingDto.price(),
                landListingDto.description(),
                landListingDto.squareMeters(),
                landListingDto.listingType(),
                new Location(landListingDto.locationDto().region(),
                        landListingDto.locationDto().city(),
                        landListingDto.locationDto().address(),
                        Location.createPoint(landListingDto.locationDto().longitude(),
                                landListingDto.locationDto().latitude())),
                landListingDto.building(),
                landListingDto.otherFeatures()
        ));
    }

    public BuildingListing createBuildingListing(BuildingListingDto buildingListingDto,
                                               String agentEmail)
            throws EntityNotExistsException {
        Agent agent = agencyService.getAgentByEmail(agentEmail);
        return listingRepository.save(new BuildingListing(
                agent,
                buildingListingDto.title(),
                buildingListingDto.price(),
                buildingListingDto.description(),
                buildingListingDto.squareMeters(),
                buildingListingDto.listingType(),
                new Location(buildingListingDto.locationDto().region(),
                        buildingListingDto.locationDto().city(),
                        buildingListingDto.locationDto().address(),
                        Location.createPoint(buildingListingDto.locationDto().longitude(),
                                buildingListingDto.locationDto().latitude())),
                buildingListingDto.otherFeatures()
        ));
    }

    @Transactional
    public void updateHouseListing(String listingId,
                                   HouseListingDto houseListingDto,
                                   String agentEmail)
            throws EntityNotExistsException, ForbiddenException {
        Agent agent = agencyService.getAgentByEmail(agentEmail);
        HouseListing houseListing = houseListingRepository.findById(listingId)
                .orElseThrow(() -> new EntityNotExistsException(String.format(ENTITY_NOT_EXIST_LISTING_MSG, listingId)));
        validateAgent(agent, houseListing);
        houseListing.setTitle(houseListingDto.title());
        houseListing.setPrice(houseListingDto.price());
        houseListing.setDescription(houseListingDto.description());
        houseListing.setSquareMeters(houseListingDto.squareMeters());
        houseListing.setListingType(houseListingDto.listingType());
        houseListing.setLocation(new Location(houseListingDto.locationDto().region(),
                houseListingDto.locationDto().city(),
                houseListingDto.locationDto().address(),
                Location.createPoint(houseListingDto.locationDto().longitude(),
                        houseListingDto.locationDto().latitude())));
        houseListing.setnRooms(houseListingDto.nRooms());
        houseListing.setnBathrooms(houseListingDto.nBathrooms());
        houseListing.setFloor(houseListingDto.floor());
        houseListing.setEnergyClass(houseListingDto.energyClass());
        houseListing.setOtherFeatures(houseListingDto.otherFeatures());
    }

    @Transactional
    public void updateGarageListing(String listingId,
                                   GarageListingDto garageListingDto,
                                   String agentEmail)
            throws EntityNotExistsException, ForbiddenException {
        Agent agent = agencyService.getAgentByEmail(agentEmail);
        GarageListing garageListing = garageListingRepository.findById(listingId)
                .orElseThrow(() -> new EntityNotExistsException(String.format(ENTITY_NOT_EXIST_LISTING_MSG, listingId)));
        validateAgent(agent, garageListing);
        garageListing.setTitle(garageListingDto.title());
        garageListing.setPrice(garageListingDto.price());
        garageListing.setDescription(garageListingDto.description());
        garageListing.setSquareMeters(garageListingDto.squareMeters());
        garageListing.setListingType(garageListingDto.listingType());
        garageListing.setLocation(new Location(garageListingDto.locationDto().region(),
                garageListingDto.locationDto().city(),
                garageListingDto.locationDto().address(),
                Location.createPoint(garageListingDto.locationDto().longitude(),
                        garageListingDto.locationDto().latitude())));
        garageListing.setFloor(garageListingDto.floor());
        garageListing.setOtherFeatures(garageListingDto.otherFeatures());
    }

    @Transactional
    public void updateLandListing(String listingId,
                                    LandListingDto landListingDto,
                                    String agentEmail)
            throws EntityNotExistsException, ForbiddenException {
        Agent agent = agencyService.getAgentByEmail(agentEmail);
        LandListing landListing = landListingRepository.findById(listingId)
                .orElseThrow(() -> new EntityNotExistsException(String.format(ENTITY_NOT_EXIST_LISTING_MSG, listingId)));
        validateAgent(agent, landListing);
        landListing.setTitle(landListingDto.title());
        landListing.setPrice(landListingDto.price());
        landListing.setDescription(landListingDto.description());
        landListing.setSquareMeters(landListingDto.squareMeters());
        landListing.setListingType(landListingDto.listingType());
        landListing.setLocation(new Location(landListingDto.locationDto().region(),
                landListingDto.locationDto().city(),
                landListingDto.locationDto().address(),
                Location.createPoint(landListingDto.locationDto().longitude(),
                        landListingDto.locationDto().latitude())));
        landListing.setBuilding(landListingDto.building());
        landListing.setOtherFeatures(landListingDto.otherFeatures());
    }

    @Transactional
    public void updateBuildingListing(String listingId,
                                    BuildingListingDto buildingListingDto,
                                    String agentEmail)
            throws EntityNotExistsException, ForbiddenException {
        Agent agent = agencyService.getAgentByEmail(agentEmail);
        BuildingListing buildingListing = buildingListingRepository.findById(listingId)
                .orElseThrow(() -> new EntityNotExistsException(String.format(ENTITY_NOT_EXIST_LISTING_MSG, listingId)));
        validateAgent(agent, buildingListing);
        buildingListing.setTitle(buildingListingDto.title());
        buildingListing.setPrice(buildingListingDto.price());
        buildingListing.setDescription(buildingListingDto.description());
        buildingListing.setSquareMeters(buildingListingDto.squareMeters());
        buildingListing.setListingType(buildingListingDto.listingType());
        buildingListing.setLocation(new Location(buildingListingDto.locationDto().region(),
                buildingListingDto.locationDto().city(),
                buildingListingDto.locationDto().address(),
                Location.createPoint(buildingListingDto.locationDto().longitude(),
                        buildingListingDto.locationDto().latitude())));
        buildingListing.setOtherFeatures(buildingListingDto.otherFeatures());
    }

    @Transactional
    public void deleteListing(String listingId, String agentEmail)
            throws EntityNotExistsException, ForbiddenException {
        Agent agent = agencyService.getAgentByEmail(agentEmail);
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new EntityNotExistsException(String.format(ENTITY_NOT_EXIST_LISTING_MSG, listingId)));
        validateAgent(agent, listing);
        listingRepository.delete(listing);
    }

    private void validateAgent(Agent agent, Listing listing) throws ForbiddenException {
        if (!agent.equals(listing.getAgent())) {
            throw new ForbiddenException("Agents can only modify their listings");
        }
    }

    public Listing getListing(String listingId) throws EntityNotExistsException {
        return listingRepository.findById(listingId)
                .orElseThrow(() -> new EntityNotExistsException(String.format(ENTITY_NOT_EXIST_LISTING_MSG, listingId)));
    }

    @Transactional
    public List<HouseListing> getHouseListings(HouseSearch houseSearch, Pageable pageable) {
        Page<HouseListing> listings;
        if (houseSearch.getCenterCoordinates() != null && houseSearch.getRadius() != null) {
            listings = houseListingRepository
                    .findAllByFiltersAndLocation(houseSearch.getListingType(),
                            houseSearch.getPriceMin(),
                            houseSearch.getPriceMax(),
                            houseSearch.getSquareMetersMin(),
                            houseSearch.getSquareMetersMax(),
                            houseSearch.getnRoomsMin(),
                            houseSearch.getnRoomsMax(),
                            houseSearch.getnBathroomsMin(),
                            houseSearch.getnBathroomsMax(),
                            houseSearch.getFloorMin(),
                            houseSearch.getFloorMax(),
                            houseSearch.getEnergyClassMin(),
                            houseSearch.getAgentId(),
                            houseSearch.getCenterCoordinates(),
                            houseSearch.getRadius(),
                            pageable);
        } else {
            listings = houseListingRepository
                    .findAllByFilters(houseSearch.getListingType(),
                            houseSearch.getRegion(),
                            houseSearch.getCity(),
                            houseSearch.getPriceMin(),
                            houseSearch.getPriceMax(),
                            houseSearch.getSquareMetersMin(),
                            houseSearch.getSquareMetersMax(),
                            houseSearch.getnRoomsMin(),
                            houseSearch.getnRoomsMax(),
                            houseSearch.getnBathroomsMin(),
                            houseSearch.getnBathroomsMax(),
                            houseSearch.getFloorMin(),
                            houseSearch.getFloorMax(),
                            houseSearch.getEnergyClassMin(),
                            houseSearch.getAgentId(),
                            pageable);
        }

        if (houseSearch.getUser() != null) {
            recentSearchService.saveSearch(houseSearch);
        }
        return listings.getContent();
    }

    @Transactional
    public List<GarageListing> getGarageListings(GarageSearch garageSearch, Pageable pageable) {
        Page<GarageListing> listings;
        if (garageSearch.getCenterCoordinates() != null && garageSearch.getRadius() != null) {
            listings = garageListingRepository
                    .findAllByFiltersAndLocation(garageSearch.getListingType(),
                            garageSearch.getPriceMin(),
                            garageSearch.getPriceMax(),
                            garageSearch.getSquareMetersMin(),
                            garageSearch.getSquareMetersMax(),
                            garageSearch.getFloorMin(),
                            garageSearch.getFloorMax(),
                            garageSearch.getAgentId(),
                            garageSearch.getCenterCoordinates(),
                            garageSearch.getRadius(),
                            pageable);
        } else {
            listings = garageListingRepository
                    .findAllByFilters(garageSearch.getListingType(),
                            garageSearch.getRegion(),
                            garageSearch.getCity(),
                            garageSearch.getPriceMin(),
                            garageSearch.getPriceMax(),
                            garageSearch.getSquareMetersMin(),
                            garageSearch.getSquareMetersMax(),
                            garageSearch.getFloorMin(),
                            garageSearch.getFloorMax(),
                            garageSearch.getAgentId(),
                            pageable);
        }

        if (garageSearch.getUser() != null) {
            recentSearchService.saveSearch(garageSearch);
        }
        return listings.getContent();
    }

    @Transactional
    public List<LandListing> getLandListings(LandSearch landSearch, Pageable pageable) {
        Page<LandListing> listings;
        if (landSearch.getCenterCoordinates() != null && landSearch.getRadius() != null) {
            listings = landListingRepository
                    .findAllByFiltersAndLocation(landSearch.getListingType(),
                            landSearch.getPriceMin(),
                            landSearch.getPriceMax(),
                            landSearch.getSquareMetersMin(),
                            landSearch.getSquareMetersMax(),
                            landSearch.getBuilding(),
                            landSearch.getAgentId(),
                            landSearch.getCenterCoordinates(),
                            landSearch.getRadius(),
                            pageable);
        } else {
            listings = landListingRepository
                    .findAllByFilters(landSearch.getListingType(),
                            landSearch.getRegion(),
                            landSearch.getCity(),
                            landSearch.getPriceMin(),
                            landSearch.getPriceMax(),
                            landSearch.getSquareMetersMin(),
                            landSearch.getSquareMetersMax(),
                            landSearch.getBuilding(),
                            landSearch.getAgentId(),
                            pageable);
        }

        if (landSearch.getUser() != null) {
            recentSearchService.saveSearch(landSearch);
        }
        return listings.getContent();
    }

    @Transactional
    public List<BuildingListing> getBuildingListings(BuildingSearch buildingSearch, Pageable pageable) {
        Page<BuildingListing> listings;
        if (buildingSearch.getCenterCoordinates() != null && buildingSearch.getRadius() != null) {
            listings = buildingListingRepository
                    .findAllByFiltersAndLocation(buildingSearch.getListingType(),
                            buildingSearch.getPriceMin(),
                            buildingSearch.getPriceMax(),
                            buildingSearch.getSquareMetersMin(),
                            buildingSearch.getSquareMetersMax(),
                            buildingSearch.getAgentId(),
                            buildingSearch.getCenterCoordinates(),
                            buildingSearch.getRadius(),
                            pageable);
        } else {
            listings = buildingListingRepository
                    .findAllByFilters(buildingSearch.getListingType(),
                            buildingSearch.getRegion(),
                            buildingSearch.getCity(),
                            buildingSearch.getPriceMin(),
                            buildingSearch.getPriceMax(),
                            buildingSearch.getSquareMetersMin(),
                            buildingSearch.getSquareMetersMax(),
                            buildingSearch.getAgentId(),
                            pageable);
        }

        if (buildingSearch.getUser() != null) {
            recentSearchService.saveSearch(buildingSearch);
        }
        return listings.getContent();
    }
}
