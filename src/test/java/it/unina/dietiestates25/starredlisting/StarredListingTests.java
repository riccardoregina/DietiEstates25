package it.unina.dietiestates25.starredlisting;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.javafaker.Faker;

import it.unina.dietiestates25.agency.model.Admin;
import it.unina.dietiestates25.agency.model.Agency;
import it.unina.dietiestates25.agency.model.Agent;
import it.unina.dietiestates25.agency.port.in.AgencyService;
import it.unina.dietiestates25.customer.model.Customer;
import it.unina.dietiestates25.customer.port.in.CustomerService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.GarageListingDto;
import it.unina.dietiestates25.listing.infrastructure.adapter.in.dto.LocationDto;
import it.unina.dietiestates25.listing.model.ListingType;
import it.unina.dietiestates25.listing.model.listing.Listing;
import it.unina.dietiestates25.listing.port.in.ListingService;
import it.unina.dietiestates25.starredlisting.port.in.StarredListingService;
import jakarta.transaction.Transactional;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StarredListingTests {
    private final Faker faker = new Faker();

    @Autowired
    AgencyService agencyService;
    @Autowired
    ListingService listingService;
    @Autowired
    CustomerService customerService;
    @Autowired
    StarredListingService starredListingService;

    Agency agency;
    Admin admin;
    Agent agent;
    Customer customer;
    UserDetails userDetails;
    Listing listing;

    @BeforeAll
    void setup() throws Exception {
        agency = new Agency(faker.internet().domainName(), faker.number().toString());
        admin = agencyService.createAdminAndAgency(new Admin(faker.name().toString(), faker.name().toString(), faker.internet().emailAddress(), LocalDate.of(1987, 5, 10), faker.internet().password(), agency, ""));
        agent = agencyService.createAgent(new Agent(faker.name().toString(), faker.name().toString(), faker.internet().emailAddress(), LocalDate.of(1987, 5, 10), faker.internet().password(), agency, admin, "", ""));
        customer = customerService.signUp(new Customer(faker.name().toString(), faker.name().toString(), faker.internet().emailAddress(), LocalDate.of(1987, 5, 10), faker.internet().password(), ""));
        userDetails = User.builder()
                        .username(customer.getEmail())
                        .password(customer.getPasswordHash())
                        .build();
        listing = listingService.createGarageListing(new GarageListingDto(
                                                        faker.name().toString(), 
                                                        Integer.valueOf(100000), 
                                                        faker.pokemon().name(), 
                                                        Integer.valueOf(100), 
                                                        ListingType.BUY, 
                                                        new LocationDto(
                                                            faker.address().country(), 
                                                            faker.address().city(), 
                                                            faker.address().fullAddress(), 
                                                            Double.valueOf(14.193), 
                                                            Double.valueOf(40.827)), 
                                                        Integer.valueOf(0), 
                                                        null), agent.getEmail());
    }

    @Test
    void givenWrongListingId_whenAddStarredListingCalled_thenEntityNotExistsExceptionIsThrown() {
        Assertions.assertThrows(EntityNotExistsException.class, () ->
            starredListingService.addStarredListing(customer.getEmail(), listing.getId().toUpperCase()));
    }

    @Test
    @Transactional
    void givenValidListingId_whenAddStarredListingCalled_thenListingIsAddedToStarredListingsOfUser() throws Exception {
        starredListingService.addStarredListing(userDetails.getUsername(), listing.getId());
        var starredListing = starredListingService.getStarredListings(customer.getId(), customer.getEmail()).getLast();
        Assertions.assertEquals(listing.getId(), starredListing.getId());
    }
}
