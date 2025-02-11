package it.unina.dietiestates25.listing.port.in;

import it.unina.dietiestates25.agency.port.in.AgencyService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.BuildingListingDto;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.GarageListingDto;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.HouseListingDto;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.LandListingDto;
import it.unina.dietiestates25.listing.port.out.ListingRepository;
import it.unina.dietiestates25.model.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
public class ListingService {
    private final AgencyService agencyService;
    private final ListingRepository listingRepository;

    public ListingService(AgencyService agencyService,
                          ListingRepository listingRepository) {
        this.agencyService = agencyService;
        this.listingRepository = listingRepository;
    }

    public HouseListing createHouseListing(HouseListingDto houseListingDto,
                                 String agentEmail)
            throws EntityNotExistsException{
        Agent agent = agencyService.getAgentByEmail(agentEmail);
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(houseListingDto.locationDto().longitude(),
                houseListingDto.locationDto().latitude()));
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
                        point),
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
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(garageListingDto.locationDto().longitude(),
                garageListingDto.locationDto().latitude()));
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
                        point),
                garageListingDto.floor(),
                garageListingDto.otherFeatures()
        ));
    }

    public LandListing createLandListing(LandListingDto landListingDto,
                                         String agentEmail)
            throws EntityNotExistsException {
        Agent agent = agencyService.getAgentByEmail(agentEmail);
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(landListingDto.locationDto().longitude(),
                landListingDto.locationDto().latitude()));
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
                        point),
                landListingDto.building(),
                landListingDto.otherFeatures()
        ));
    }

    public BuildingListing createBuildingListing(BuildingListingDto buildingListingDto,
                                               String agentEmail)
            throws EntityNotExistsException {
        Agent agent = agencyService.getAgentByEmail(agentEmail);
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(buildingListingDto.locationDto().longitude(),
                buildingListingDto.locationDto().latitude()));
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
                        point),
                buildingListingDto.otherFeatures()
        ));
    }
}
