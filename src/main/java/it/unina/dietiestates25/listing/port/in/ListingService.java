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
import org.springframework.stereotype.Service;

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

    public ListingService(AgencyService agencyService,
                          ListingRepository<Listing> listingRepository,
                          @Qualifier("houseListingRepository") HouseListingRepository houseListingRepository,
                          @Qualifier("garageListingRepository") GarageListingRepository garageListingRepository,
                          @Qualifier("landListingRepository") LandListingRepository landListingRepository,
                          @Qualifier("buildingListingRepository") BuildingListingRepository buildingListingRepository) {
        this.agencyService = agencyService;
        this.listingRepository = listingRepository;
        this.houseListingRepository = houseListingRepository;
        this.garageListingRepository = garageListingRepository;
        this.landListingRepository = landListingRepository;
        this.buildingListingRepository = buildingListingRepository;
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
                new Location(houseListingDto.locationDto().address(),
                        houseListingDto.locationDto().region(),
                        houseListingDto.locationDto().city(),
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
                new Location(garageListingDto.locationDto().address(),
                        garageListingDto.locationDto().region(),
                        garageListingDto.locationDto().city(),
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
                new Location(landListingDto.locationDto().address(),
                        landListingDto.locationDto().region(),
                        landListingDto.locationDto().city(),
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
                new Location(buildingListingDto.locationDto().address(),
                        buildingListingDto.locationDto().region(),
                        buildingListingDto.locationDto().city(),
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
        houseListing.setLocation(new Location(houseListingDto.locationDto().address(),
                houseListingDto.locationDto().region(),
                houseListingDto.locationDto().city(),
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
        garageListing.setLocation(new Location(garageListingDto.locationDto().address(),
                garageListingDto.locationDto().region(),
                garageListingDto.locationDto().city(),
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
        landListing.setLocation(new Location(landListingDto.locationDto().address(),
                landListingDto.locationDto().region(),
                landListingDto.locationDto().city(),
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
        buildingListing.setLocation(new Location(buildingListingDto.locationDto().address(),
                buildingListingDto.locationDto().region(),
                buildingListingDto.locationDto().city(),
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
}
