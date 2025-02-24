package it.unina.dietiestates25.agentreview.port.in;

import it.unina.dietiestates25.agency.port.in.AgencyService;
import it.unina.dietiestates25.agentreview.infrastructure.adapter.in.dto.AgentReviewDto;
import it.unina.dietiestates25.agentreview.port.out.AgentReviewRepository;
import it.unina.dietiestates25.customer.port.in.CustomerService;
import it.unina.dietiestates25.exception.EntityNotExistsException;
import it.unina.dietiestates25.model.Agent;
import it.unina.dietiestates25.model.AgentReview;
import it.unina.dietiestates25.model.Customer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentReviewService {
    private final AgentReviewRepository agentReviewRepository;
    private final AgencyService agencyService;
    private final CustomerService customerService;

    public AgentReviewService(AgentReviewRepository agentReviewRepository, AgencyService agencyService, CustomerService customerService) {
        this.agentReviewRepository = agentReviewRepository;
        this.agencyService = agencyService;
        this.customerService = customerService;
    }

    public AgentReview createAgentReview(AgentReviewDto agentReviewDto, UserDetails userDetails)
            throws EntityNotExistsException {
        Customer customer = customerService.getCustomer(userDetails.getUsername());
        Agent agent = agencyService.getAgentById(agentReviewDto.agentId());
        return agentReviewRepository.save(new AgentReview(
                customer,
                agent,
                agentReviewDto.value(),
                agentReviewDto.comment()
        ));
    }

    public List<AgentReview> getAgentReviews(String agentId)
            throws EntityNotExistsException {
        Agent agent = agencyService.getAgentById(agentId);
        return agentReviewRepository.findAllByAgent(agent);
    }
}
