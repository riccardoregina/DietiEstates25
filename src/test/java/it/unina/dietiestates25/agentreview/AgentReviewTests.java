package it.unina.dietiestates25.agentreview;

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

import it.unina.dietiestates25.agency.model.Agent;
import it.unina.dietiestates25.agency.model.Admin;
import it.unina.dietiestates25.agency.model.Agency;
import it.unina.dietiestates25.agency.port.in.AgencyService;
import it.unina.dietiestates25.agentreview.infrastructure.adapter.in.dto.AgentReviewDto;
import it.unina.dietiestates25.agentreview.port.in.AgentReviewService;
import it.unina.dietiestates25.customer.model.Customer;
import it.unina.dietiestates25.customer.port.in.CustomerService;
import it.unina.dietiestates25.exception.EntityNotExistsException;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AgentReviewTests {
    private final Faker faker = new Faker();

    @Autowired
    AgencyService agencyService;
    @Autowired
    AgentReviewService agentReviewService;
    @Autowired
    CustomerService customerService;

    Agency agency;
    Admin admin;
    Agent agent;
    Customer customer;
    UserDetails userDetails;

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
    }

    @Test
    void givenCorrectAgentId_whenCreateAgentReviewCalled_thenAgentReviewIsCreated() throws Exception {
        var agentReview = agentReviewService.createAgentReview(new AgentReviewDto(agent.getId(), 5, "Very good"), userDetails);
        var fetchedAgentReview = agentReviewService.getAgentReviews(agent.getId()).getLast();
        Assertions.assertAll(
            () -> Assertions.assertEquals(agentReview.getId(), fetchedAgentReview.getId()),
            () -> Assertions.assertEquals(agentReview.getValue(), fetchedAgentReview.getValue()),
            () -> Assertions.assertEquals(agentReview.getComment(), fetchedAgentReview.getComment())
        );
    }

    @Test
    void givenWrongAgentId_whenCreateAgentCalled_thenEntityNotExistsExceptionIsThrown() {
        Assertions.assertThrows(EntityNotExistsException.class, () -> 
            agentReviewService.createAgentReview(new AgentReviewDto(admin.getId(), 5, "Very good"), userDetails));
    }
}
